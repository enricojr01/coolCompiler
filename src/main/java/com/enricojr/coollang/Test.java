package com.enricojr.coollang;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.codegen.MipsClassNameConstGenerator;
import com.enricojr.coollang.codegen.MipsClassNameTableGenerator;
import com.enricojr.coollang.codegen.MipsClassTagGenerator;
import com.enricojr.coollang.codegen.MipsDefaultConstGenerator;
import com.enricojr.coollang.codegen.models.MipsAlignedTable;
import com.enricojr.coollang.codegen.models.MipsBaseConst;
import com.enricojr.coollang.codegen.models.MipsIntConst;
import com.enricojr.coollang.codegen.models.MipsStrConst;
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
            MipsClassNameConstGenerator mcncg = new MipsClassNameConstGenerator(tagTable);
            mcncg.visitCoolProgram(top);
            HashMap<String, MipsStrConst> classNameConstTable = mcncg.getClassNameTable();
            System.out.println(classNameConstTable);

            System.out.println("Generating default consts (Bool, String)...");
            MipsDefaultConstGenerator mdcg = new MipsDefaultConstGenerator(tagTable);
            mdcg.visitCoolProgram(top);
            String builtins = mdcg.getBuiltins();
            System.out.println(builtins);

            System.out.println("Generating class_nameTab...");
            MipsClassNameTableGenerator mcntg = new MipsClassNameTableGenerator(classNameConstTable);
            String classNameTab = mcntg.getList();
            String classObjs = mcntg.getConsts();
            System.out.println(classNameTab);
            System.out.println();
            System.out.println(classObjs);

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
