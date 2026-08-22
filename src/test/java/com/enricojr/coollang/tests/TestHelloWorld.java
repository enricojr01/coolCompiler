package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.AstBuilder;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.parser.CoolLexer;
import com.enricojr.coollang.parser.CoolParser;
import com.enricojr.coollang.parser.CoolParser.ProgContext;
import com.enricojr.coollang.semantic.InheritanceGraph;
import com.enricojr.coollang.semantic.SemanticAnalyzer;
import com.enricojr.coollang.semantic.exceptions.ClassDefinedTwiceException;
import com.enricojr.coollang.semantic.exceptions.CoolClassInheritanceCycleException;
import com.enricojr.coollang.semantic.exceptions.InvalidClassNameException;
import com.enricojr.coollang.semantic.exceptions.ParentClassNotDefinedException;
import org.antlr.v4.runtime.*;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
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
            System.out.println("Testing parser/lexer on file: " + sample);
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

            // TODO: Maybe reconsider this, tests should only check one thing at a time.
            try {
                if (sample.getName().equals("badclassparent")) {
                    igb = new InheritanceGraphBuilder(top);
                    assertThrows(ParentClassNotDefinedException.class, () -> new InheritanceGraphBuilder(top));
                }
                if (sample.getName().equals("badclasstwice")) {
                    igb = new InheritanceGraphBuilder(top);
                    assertThrows(ClassDefinedTwiceException.class, () -> new InheritanceGraphBuilder(top));
                }
            } catch (InvalidClassNameException | ParentClassNotDefinedException | CoolClassInheritanceCycleException | ClassDefinedTwiceException _) {
            }
        }
    }

    @Test
    public void TestCycleDetection() throws
            InvalidClassNameException, ClassDefinedTwiceException, ParentClassNotDefinedException,
            CoolClassInheritanceCycleException {
        ANTLRInputStream ais = new ANTLRInputStream("class A inherits B {}; class B inherits A {};");
        CoolLexer cl = new CoolLexer(ais);
        CommonTokenStream cts = new CommonTokenStream(cl);
        CoolParser cpa = new CoolParser(cts);
        AstBuilder ab = new AstBuilder();
        CoolProgram top = (CoolProgram) ab.visitProg(cpa.prog());
        SemanticAnalyzer sa = new SemanticAnalyzer(top);
        HashMap<CoolIdentifier, LinkedList<CoolClass>> ig = sa.getGraph();
        assertThrows(CoolClassInheritanceCycleException.class, ig::hasCycles);
    }

    @Test
    public void TestSelfCycleDetection() {
        ANTLRInputStream ais = new ANTLRInputStream("class A inherits A {};");
        CoolLexer cl = new CoolLexer(ais);
        CommonTokenStream cts = new CommonTokenStream(cl);
        CoolParser cpa = new CoolParser(cts);
        AstBuilder ab = new AstBuilder();
        CoolProgram top = (CoolProgram) ab.visitProg(cpa.prog());
        assertThrows(CoolClassInheritanceCycleException.class, () -> new InheritanceGraphBuilder(top));
    }
}
