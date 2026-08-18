package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.AstBuilder;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.parser.CoolLexer;
import com.enricojr.coollang.parser.CoolParser;
import com.enricojr.coollang.parser.CoolParser.ProgContext;
import com.enricojr.coollang.semantic.InheritanceGraph;
import com.enricojr.coollang.semantic.InheritanceGraphBuilder;
import com.enricojr.coollang.semantic.exceptions.ClassDefinedTwiceException;
import com.enricojr.coollang.semantic.exceptions.CoolClassUndefinedException;
import com.enricojr.coollang.semantic.exceptions.InvalidClassNameException;
import com.enricojr.coollang.semantic.exceptions.ParentClassNotDefinedException;
import org.antlr.v4.runtime.*;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class TestHelloWorld {
    private static class CoolFileFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            String ext = FilenameUtils.getExtension(name);
            return ext.equals("cl");
        }
    }

    public class TestCancelListener extends BaseErrorListener {
        @Override
        public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line, int charPositionInLine,
                String msg, RecognitionException e
        ) {
            String errorMsg = String.format("Syntax error on %s:%s (line:charPosition)", line, charPositionInLine);
            fail(errorMsg);
        }
    }

    @Test
    public void TestSetup() {
        assertTrue(true);
    }


    @Test
    public void TestCodeSamplesParse() {
        Stack<File> codeSamples = new Stack<>();
        File coolSamplesDir = new File("./coolExamples");
        File[] files = coolSamplesDir.listFiles(new CoolFileFilter());
        if (files == null) {
            fail("No Cool files found in the ./coolExamples directory.");
        } else {
            codeSamples.addAll(List.of(files));
        }

        while (!codeSamples.isEmpty()) {
            File sample = codeSamples.pop();
            System.out.println("Testing file: " + sample);
            FileInputStream fis = null;
            ANTLRInputStream ais = null;
            TestCancelListener tcl = new TestCancelListener();

            try {
                fis = new FileInputStream(sample);
            } catch (FileNotFoundException e) {
                fail("Could not find file " + sample.toString());
            }

            try {
                ais = new ANTLRInputStream(fis);
            } catch (IOException e) {
                fail("IO failed: " + e.getMessage());
            }

            CoolLexer cl = new CoolLexer(ais);
            cl.removeErrorListeners();
            cl.addErrorListener(tcl);

            CommonTokenStream cts = new CommonTokenStream(cl);

            CoolParser cpa = new CoolParser(cts);
            cpa.removeErrorListeners();
            cpa.addErrorListener(tcl);

            ProgContext prog = cpa.prog();
            assertNotNull(prog);
        }
    }

    @Test
    public void TestCodeSamplesSemanticAnalysis() {
        Stack<File> codeSamples = new Stack<>();
        File coolSamplesDir = new File("./coolExamples/classtests");
        File[] files = coolSamplesDir.listFiles(new CoolFileFilter());
        if (files == null) {
            fail("No Cool files found in the ./coolExamples directory.");
        } else {
            codeSamples.addAll(List.of(files));
        }

        while (!codeSamples.isEmpty()) {
            File sample = codeSamples.pop();
            System.out.println("Testing file: " + sample);
            FileInputStream fis = null;
            ANTLRInputStream ais = null;
//            TestCancelListener tcl = new TestCancelListener();

            try {
                fis = new FileInputStream(sample);
            } catch (FileNotFoundException e) {
                fail("Could not find file " + sample.toString());
            }

            try {
                ais = new ANTLRInputStream(fis);
            } catch (IOException e) {
                fail("IO failed: " + e.getMessage());
            }

            CoolLexer cl = new CoolLexer(ais);
            cl.removeErrorListeners();

            CommonTokenStream cts = new CommonTokenStream(cl);

            CoolParser cpa = new CoolParser(cts);
            cpa.removeErrorListeners();

            ProgContext prog = cpa.prog();
            AstBuilder ab = new AstBuilder();
            CoolProgram top = (CoolProgram) ab.visitProg(prog);
            InheritanceGraphBuilder igb = null;

            try {
                igb = new InheritanceGraphBuilder(top);
            } catch (InvalidClassNameException e) {
                fail("Invalid class identifier: " + e.getMessage());
            } catch (ClassDefinedTwiceException e) {
                fail("Class defined twice: " + e.getMessage());
            }

            assertNotNull(igb);
        }

    }
}
