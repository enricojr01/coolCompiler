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
            CoolProgram top = (CoolProgram) ab.visit(prog);

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

            System.out.println("Generating class tags...");
            MipsClassTagGenerator mctg = new MipsClassTagGenerator();
            mctg.visitCoolProgram(top);
            HashMap<String, Integer> tagTable = mctg.getTagTable();
            System.out.println(tagTable);

            System.out.println("Generating class name strConsts and their corresponding intConst sizes...");
            MipsSymbolTable symbolTable = new MipsSymbolTable();
            MipsClassNameConstGenerator mcncg = new MipsClassNameConstGenerator(tagTable, symbolTable);
            mcncg.visitCoolProgram(top);
            System.out.println(symbolTable);

            System.out.println("Generating default consts (Bool, String, etc)...");
            MipsDefaultConstGenerator mdcg = new MipsDefaultConstGenerator(tagTable, symbolTable);
            mdcg.visitCoolProgram(top);
            System.out.println(symbolTable);

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
            System.out.println("Filtered: " + filtered);
            System.out.println("Created array of length: " + classObjTab.length);
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
            // str const
            for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
                if (e.getValue() instanceof MipsStrConst) {
                    System.out.print(e.getValue().toCode());
                }
            }
            // int const
            for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
                if (e.getValue() instanceof MipsStrConst) {
                    MipsIntConst size = ((MipsStrConst) e.getValue()).getSize();
                    System.out.print(size.toCode());
                }
                if (e.getValue() instanceof MipsIntConst) {
                    System.out.print(e.getValue().toCode());
                }
            }
            // class nameTables
            System.out.println("class_nameTab:");
            for (String entry : classNameTab) {
                System.out.println(entry);
            }
            // class objTables
            System.out.println("class_objTab:");
            for (String entry : classObjTab) {
                System.out.println(entry);
            }
            // class dispatchTables
            for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
                if (e.getValue() instanceof MipsProtObj) {
                    MipsProtObj obj = (MipsProtObj) e.getValue();
                    System.out.print(obj.getDispTab().toCode());
                }
            }
            // class prototype objects
            for (Map.Entry<String, MipsBaseConst> e : symbolTable.entrySet()) {
                if (e.getValue() instanceof MipsProtObj) {
                    MipsProtObj obj = (MipsProtObj) e.getValue();
                    System.out.print(obj.toCode());
                }
            }
        } catch (StackTraceException e) {
            LinkedList<CoolBaseNode> errorStack = e.getCustomStackTrace();
            while (!(errorStack.isEmpty())) {
                CoolBaseNode next = errorStack.pop();
                System.out.println(
                        String.format("%s (in file %s @ %s:%s)", next, e.getFileName(), next.getLine(), next.getCharPos())
                );
            }
        }
    }
}
