package com.enricojr.coollang;
import java.io.FileInputStream;
import java.io.InputStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import com.enricojr.coollang.ast.AstBuilder;
import com.enricojr.coollang.ast.AstPrinter;
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

        System.out.println("Parsing input...");
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CoolParser parser = new CoolParser(tokens);

        System.out.println("Creating AST...");
        AstBuilder ab = new AstBuilder();
        CoolProgram top = (CoolProgram) ab.visitProg(parser.prog());
        AstPrinter ap = new AstPrinter(top);
        System.out.println(ap);
    }
}
