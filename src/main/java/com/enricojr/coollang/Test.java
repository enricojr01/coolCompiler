package com.enricojr.coollang;

import java.io.FileInputStream;
import java.io.InputStream;

import com.enricojr.coollang.ast.AstPrinter;
import com.enricojr.coollang.parser.CoolLexer;
import com.enricojr.coollang.parser.CoolParser;
import com.enricojr.coollang.parser.CoolParser.ProgContext;
import com.enricojr.coollang.semantic.classtree.ClassTreeBuilder;
import com.enricojr.coollang.semantic.classtree.ClassTreeLinker;
import com.enricojr.coollang.semantic.classtree.ClassTreePrinter;
import com.enricojr.coollang.semantic.classtree.ClassTreeSetup;
import com.enricojr.coollang.semantic.symboltable.SymbolTableBuilder;
import com.enricojr.coollang.semantic.symboltable.SymbolTableLinker;
import com.enricojr.coollang.semantic.symboltable.SymbolTablePrinter;
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

        System.out.println("Printing AST...");
        AstPrinter ap = new AstPrinter();
        ap.visitCoolProgram(top);

        System.out.println("Setting up class tree...");
        ClassTreeSetup cts = new ClassTreeSetup();
        cts.visitCoolProgram(top);

        System.out.println("Linking classes...");
        ClassTreeLinker ctl = new ClassTreeLinker();
        ctl.visitCoolProgram(top);

        System.out.println("Enforcing inheritance rules...");
        ClassTreeBuilder ctb = new ClassTreeBuilder();
        ctb.visitCoolProgram(top);

        System.out.println("Printing class tree...");
        ClassTreePrinter ctp = new ClassTreePrinter();
        ctp.visitCoolProgram(top);

        System.out.println("Initializing/Linking class symbol tables...");
        SymbolTableLinker sLinker = new SymbolTableLinker();
        sLinker.visitCoolProgram(top);

        System.out.println("Populating class symbol tables...");
        SymbolTableBuilder sBuilder = new SymbolTableBuilder();
        sBuilder.visitCoolProgram(top);

        System.out.println("Printing symbol tables...");
        SymbolTablePrinter sPrinter = new SymbolTablePrinter();
        sPrinter.visitCoolProgram(top);
//        System.out.println("Printing AST...");
//        AstPrinter ap = new AstPrinter();
//        ap.visitCoolProgram(top);


//        OldSymbolTableBuilder sBuilder = new OldSymbolTableBuilder();
//        sBuilder.visitCoolProgram(top);
//        System.out.println("Displaying symbol tables...");

//        System.out.println("Building type environments...");
//        TypeEnvironmentBuilder tBuilder = new TypeEnvironmentBuilder();
//        TypeEnvironmentPrinter tPrinter = new TypeEnvironmentPrinter();
//        tBuilder.visitCoolProgram(top);
//        System.out.println("Displaying type environments");
//        tPrinter.visitCoolProgram(top);
    }
}
