package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.AstBuilder;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.parser.CoolLexer;
import com.enricojr.coollang.parser.CoolParser;
import com.enricojr.coollang.semantic.classtree.ClassTreeBuilder;
import com.enricojr.coollang.semantic.classtree.ClassTreeLinker;
import com.enricojr.coollang.semantic.classtree.ClassTreePrinter;
import com.enricojr.coollang.semantic.classtree.ClassTreeSetup;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TestIntegrationClassTree {
    private static class CoolFileFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            String ext = FilenameUtils.getExtension(name);
            return ext.equals("cl");
        }
    }

    @Test
    public void TestCodeSamplesClassTree() {
        Stack<File> codeSamples = new Stack<>();
        File coolSamplesDir = new File("./coolExamples");
        File[] files = coolSamplesDir.listFiles(new TestIntegrationClassTree.CoolFileFilter());
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
            TestIntegrationParser.TestCancelListener tcl = new TestIntegrationParser.TestCancelListener();

            try {
                fis = new FileInputStream(sample);
            } catch (FileNotFoundException e) {
                fail("Could not find file " + sample);
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

            CoolParser.ProgContext prog = cpa.prog();
            AstBuilder ab = new AstBuilder();
            CoolProgram top = (CoolProgram) ab.visit(prog);

            System.out.println("Setting up class tree...");
            ClassTreeSetup ctset = new ClassTreeSetup();
            ctset.visitCoolProgram(top);

            System.out.println("Linking classes...");
            ClassTreeLinker ctl = new ClassTreeLinker();
            ctl.visitCoolProgram(top);

            System.out.println("Enforcing inheritance rules...");
            ClassTreeBuilder ctb = new ClassTreeBuilder();
            ctb.visitCoolProgram(top);

            System.out.println("Printing class tree...");
            ClassTreePrinter ctp = new ClassTreePrinter();
            ctp.visitCoolProgram(top);
        }
    }
}
