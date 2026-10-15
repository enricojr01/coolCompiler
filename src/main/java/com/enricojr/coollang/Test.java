package com.enricojr.coollang;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.codegen.*;
import com.enricojr.coollang.codegen.models.*;
import com.enricojr.coollang.parser.CoolLexer;
import com.enricojr.coollang.parser.CoolParser;
import com.enricojr.coollang.parser.CoolParser.ProgContext;
import com.enricojr.coollang.semantic.typechecker.TypeChecker;
import com.enricojr.coollang.semantic.typechecker.TypeSetter;
import com.enricojr.coollang.semantic.classtree.ClassTreeBuilder;
import com.enricojr.coollang.semantic.classtree.ClassTreeLinker;
import com.enricojr.coollang.semantic.classtree.ClassTreeSetup;
import com.enricojr.coollang.semantic.exceptions.StackTraceException;
import com.enricojr.coollang.semantic.symboltable.SymbolTableBuilder;
import com.enricojr.coollang.semantic.symboltable.SymbolTableLinker;
import com.enricojr.coollang.util.DetailedErrorListener;
import org.antlr.v4.runtime.*;
import com.enricojr.coollang.ast.AstBuilder;
import com.enricojr.coollang.ast.program.CoolProgram;

public class Test {

    public static void main(String[] args) throws Exception {
        System.out.println("If you see this, it's working!");
        String inputFile = null;

        if (args.length > 0) {
            inputFile = args[0];
        }

        InputStream is = System.in;
        if (inputFile != null) {
            is = new FileInputStream(inputFile);
        }

        CoolProgram top = null;
        try {
            System.out.println("Lexing input...");
            ANTLRInputStream input = new ANTLRInputStream(is);
            CoolLexer lexer = new CoolLexer(input);
            lexer.removeErrorListeners();
            lexer.addErrorListener(new DetailedErrorListener());

            System.out.println("Parsing input...");
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CoolParser parser = new CoolParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(new DetailedErrorListener());

            System.out.println("Creating AST...");
            AstBuilder ab = new AstBuilder();
            ProgContext prog = parser.prog();
            top = (CoolProgram) ab.visit(prog);

            System.out.println("Setting up class tree...");
            ClassTreeSetup cts = new ClassTreeSetup();
            cts.visitCoolProgram(top);

            System.out.println("Linking classes...");
            ClassTreeLinker ctl = new ClassTreeLinker();
            ctl.visitCoolProgram(top);

            System.out.println("Enforcing inheritance rules...");
            ClassTreeBuilder ctb = new ClassTreeBuilder(inputFile);
            ctb.visitCoolProgram(top);

            System.out.println("Initializing/Linking class symbol tables...");
            SymbolTableLinker sLinker = new SymbolTableLinker();
            sLinker.visitCoolProgram(top);

            System.out.println("Populating class symbol tables...");
            SymbolTableBuilder sBuilder = new SymbolTableBuilder(inputFile);
            sBuilder.visitCoolProgram(top);

            System.out.println("Initializing type setter...");
            TypeSetter ts = new TypeSetter();
            ts.visitCoolProgram(top);

            System.out.println("Initializing type checker...");
            TypeChecker tc = new TypeChecker(inputFile);
            tc.visitCoolProgram(top);
            System.out.println("If you see this, the type checker didn't find anything wrong.");

        } catch (StackTraceException e) {
            LinkedList<CoolBaseNode> errorStack = e.getCustomStackTrace();
            while (!(errorStack.isEmpty())) {
                CoolBaseNode next = errorStack.pop();
                System.out.println(
                        String.format("%s (in file %s @ %s:%s)", next, e.getFileName(), next.getLine(), next.getCharPos())
                );
            }
        }

        if (top == null) {
            System.out.println("This shouldn't happen but in case that it does blame the programmer.");
            System.exit(1);
        }

        System.out.println("Generating class tags...");
        MipsClassTagGenerator mctg = new MipsClassTagGenerator();
        mctg.visitCoolProgram(top);
        HashMap<String, Integer> tagTable = mctg.getTagTable();
//        System.out.println(tagTable);

        System.out.println("Generating class name strConsts and their corresponding intConst sizes...");
        MipsSymbolTable symbolTable = new MipsSymbolTable();
        MipsClassNameConstGenerator mcncg = new MipsClassNameConstGenerator(tagTable, symbolTable);
        mcncg.visitCoolProgram(top);
//        System.out.println(symbolTable);

        System.out.println("Generating default consts (Bool, String, etc)...");
        MipsDefaultConstGenerator mdcg = new MipsDefaultConstGenerator(tagTable, symbolTable);
        mdcg.visitCoolProgram(top);
//        System.out.println(symbolTable);

        System.out.println("Generating class_nameTab...");
        String[] classNameTab = new String[tagTable.size()];
        for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
            String label = e.getValue().getLabel();
            if (label.endsWith("_name")) {
                int tag = e.getValue().getTag();
                // I'm not sure if I can just do (tag * 4), so I'm going to leave it as is.
                int pos = (tag * 4) / 4;
//                    System.out.println(String.format("Inserting %s (tag: %s) at pos %s", label, tag, pos));
                classNameTab[pos] = label;
            }
        }

        System.out.println("Generating protObjs...");
        MipsClassProtObjGenerator mcpog = new MipsClassProtObjGenerator(symbolTable, tagTable);
        mcpog.visitCoolProgram(top);
        System.out.println(symbolTable);

        System.out.println("Generating class_objTab...");
        Set<Map.Entry<String, MipsBaseConst>> filtered = symbolTable
                .entrySet()
                .stream()
                .filter(x -> x.getKey().endsWith("protObj"))
                .collect(Collectors.toSet());
        String[] classObjTab = new String[filtered.size() * 2];

        for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
            if (e.getKey().endsWith("protObj")) {
                MipsProtObj obj = (MipsProtObj) e.getValue();
                String label = obj.getLabel();
                int tag = obj.getTag();
                int pos = (tag * 8) / 4;

                classObjTab[pos] = label + "_protObj";
                classObjTab[pos + 1] = label + "_init";
            }
        }

        System.out.println("Printing MIPS data section...");
        StringBuilder sb = new StringBuilder();

        // preamble
        sb.append(".data\n");
        sb.append(".align 2\n");
        sb.append(".globl class_nameTab\n");
        sb.append(".globl Main_protObj\n");
        sb.append(".globl Int_protObj\n");
        sb.append(".globl String_protObj\n");
        sb.append(".globl _int_tag\n");
        sb.append(".globl _bool_tag\n");
        sb.append(".globl _string_tag\n");
        sb.append(".globl bool_const0\n");
        sb.append(".globl class_nameTab\n");
        sb.append(".globl class_objTab\n");

        // int, str, and bool tags:
        sb.append("_int_tag:\n");
        sb.append(".word ").append(tagTable.get("Int")).append("\n");
        sb.append("_bool_tag:\n");
        sb.append(".word ").append(tagTable.get("Bool")).append("\n");
        sb.append("_string_tag:\n");
        sb.append(".word ").append(tagTable.get("String")).append("\n");

        // garbage collector config (this will be fixed output for now)
        sb.append(".globl _MemMgr_INITIALIZER\n");
        sb.append("_MemMgr_INITIALIZER:\n");
        sb.append(".word NoGC_Init\n");
        sb.append(".globl _MemMgr_COLLECTOR\n");
        sb.append("_MemMgr_COLLECTOR:\n");
        sb.append(".word NoGC_Collect\n");
        sb.append(".globl _MemMgr_TEST\n");
        sb.append("_MemMgr_TEST\n");
        sb.append(".word 0\n");

        // str const
        for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
            if (e.getValue() instanceof MipsStrConst) {
                sb.append(e.getValue().toCode());
            }
        }

        // int const
        for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
            if (e.getValue() instanceof MipsStrConst) {
                MipsIntConst size = ((MipsStrConst) e.getValue()).getSize();
                sb.append(size.toCode());
            }
            if (e.getValue() instanceof MipsIntConst) {
                sb.append(e.getValue().toCode());
            }
        }

        // class nameTables
        sb.append("class_nameTab:\n");
        for (String entry : classNameTab) {
            sb.append(entry).append("\n");
        }
        // class objTables
        sb.append("class_objTab:\n");
        for (String entry : classObjTab) {
            sb.append(entry).append("\n");
        }

        // class dispatchTables
        for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
            if (e.getValue() instanceof MipsProtObj) {
                MipsProtObj obj = (MipsProtObj) e.getValue();
            }
        }
        // class prototype objects
        for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
            if (e.getValue() instanceof MipsProtObj) {
                MipsProtObj obj = (MipsProtObj) e.getValue();
                sb.append(obj.toCode());
            }
        }

        System.out.println("Printing MIPS heap section:");
        sb.append(".globl heap_start\n");
        sb.append("heap_start:\n");

        System.out.println(sb);
    }
}
