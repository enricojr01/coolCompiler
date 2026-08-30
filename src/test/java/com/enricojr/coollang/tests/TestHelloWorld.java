package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.AstBuilder;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.parser.CoolLexer;
import com.enricojr.coollang.parser.CoolParser;
import com.enricojr.coollang.parser.CoolParser.ProgContext;
import com.enricojr.coollang.semantic.ClassTreeBuilder;
import com.enricojr.coollang.semantic.SemanticAnalyzer;
import com.enricojr.coollang.semantic.SymbolTable;
import com.enricojr.coollang.semantic.exceptions.*;
import org.antlr.v4.runtime.*;
import org.apache.commons.io.FilenameUtils;
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
    public void TestSymbolLookup() {
        SymbolTable st1 = new SymbolTable();

        CoolIdentifier ci = new CoolIdentifier("test1");
        st1.addSymbol(ci, new CoolBaseNode());

        CoolBaseNode target = st1.getSymbol(ci);
        assertNotNull(target);
    }

    @Test
    public void TestSymbolLookupFail() {
        SymbolTable st1 = new SymbolTable();

        CoolIdentifier ci = new CoolIdentifier("test1");

        CoolBaseNode target = st1.getSymbol(ci);
        assertNotNull(target);

    }

    @Test
    public void TestSymbolTableLookupchain() {
        SymbolTable st1 = new SymbolTable();
        SymbolTable st2 = new SymbolTable();
        SymbolTable st3 = new SymbolTable();

        st2.setParent(st1);
        st3.setParent(st2);

        CoolIdentifier ci1 = new CoolIdentifier("test1");
        CoolIdentifier ci2 = new CoolIdentifier("test2");
        CoolIdentifier ci3 = new CoolIdentifier("test3");

        st1.addSymbol(ci1, new CoolBaseNode());
        st2.addSymbol(ci2, new CoolBaseNode());
        st3.addSymbol(ci3, new CoolBaseNode());;

        CoolBaseNode target = st3.getSymbol(ci1);
        assertNotNull(target);

        target = st3.getSymbol(ci2);
        assertNotNull(target);
    }
}
