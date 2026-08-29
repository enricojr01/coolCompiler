package com.enricojr.coollang;

import java.io.FileInputStream;
import java.io.InputStream;

import com.enricojr.coollang.ast.AstPrinter;
import com.enricojr.coollang.parser.CoolLexer;
import com.enricojr.coollang.parser.CoolParser;
import com.enricojr.coollang.parser.CoolParser.ProgContext;
import com.enricojr.coollang.semantic.*;
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
        AstPrinter ap = new AstPrinter();
        ProgContext prog = parser.prog();
        CoolProgram top = (CoolProgram) ab.visit(prog);
        ap.visitCoolProgram(top);

        System.out.println("Building class tree...");
        ClassTreeBuilder ctb = new ClassTreeBuilder(top);
        ClassTree tree = ctb.getClassTree();
        System.out.println(tree);

        System.out.println("Displaying symbol tables...");
        SymbolTableBuilder stb = new SymbolTableBuilder();
        SymbolTablePrinter stp = new SymbolTablePrinter();
        stb.visitCoolProgram(top);
        stp.visitCoolProgram(top);
    }
}
