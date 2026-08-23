package com.enricojr.coollang;

import java.io.FileInputStream;
import java.io.InputStream;

import com.enricojr.coollang.parser.CoolLexer;
import com.enricojr.coollang.parser.CoolParser;
import com.enricojr.coollang.parser.CoolParser.ProgContext;
import com.enricojr.coollang.semantic.SemanticAnalyzer;
import com.enricojr.coollang.util.DetailedErrorListener;
import org.antlr.v4.runtime.*;
import com.enricojr.coollang.ast.AstBuilder;
import com.enricojr.coollang.ast.program.CoolProgram;

import static junit.framework.Assert.fail;

public class Test {

    public static <SemanticAnalyser> void main(String[] args) throws Exception {
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
        com.enricojr.coollang.parser.CoolParser parser = new CoolParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new DetailedErrorListener());

        System.out.println("Creating AST...");
        AstBuilder ab = new AstBuilder();
        ProgContext prog = parser.prog();
        CoolProgram top = (CoolProgram) ab.visitProg(prog);
       
        System.out.println("Performing Semantic Analysis");
        SemanticAnalyzer sa = new SemanticAnalyzer(top);

        System.out.println(sa.reportAbstractSyntaxTree());
        System.out.println(sa.reportInheritanceTree());
        System.out.println(sa.reportInheritanceChains());
        System.out.println(sa.reportSymbolTables());
    }
}
