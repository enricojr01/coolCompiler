package com.enricojr.coollang.tests.integration;

import com.enricojr.coollang.ast.AstBuilder;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.parser.CoolLexer;
import com.enricojr.coollang.parser.CoolParser;
import com.enricojr.coollang.semantic.classtree.ClassTreeBuilder;
import com.enricojr.coollang.semantic.classtree.ClassTreeLinker;
import com.enricojr.coollang.semantic.classtree.ClassTreeSetup;
import com.enricojr.coollang.semantic.symboltable.SymbolTableBuilder;
import com.enricojr.coollang.semantic.symboltable.SymbolTableLinker;
import com.enricojr.coollang.semantic.typechecker.TypeChecker;
import com.enricojr.coollang.semantic.typechecker.TypeSetter;
import org.antlr.v4.runtime.*;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class TestCodeGenDispatchTables {
    // TODO: Lots of duplicated code in these test files.
    private static class CoolFileFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            String ext = FilenameUtils.getExtension(name);
            return ext.equals("cl");
        }
    }

    public static class TestCancelListener extends BaseErrorListener {
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
        File coolSamplesDir = new File("./coolExamples/compiled");
        File[] files = coolSamplesDir.listFiles(new TestCodeGenDispatchTables.CoolFileFilter());
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
            ClassTreeBuilder ctb = new ClassTreeBuilder(sample.getName());
            ctb.visitCoolProgram(top);

            System.out.println("Initializing/Linking class symbol tables...");
            SymbolTableLinker sLinker = new SymbolTableLinker();
            sLinker.visitCoolProgram(top);

            System.out.println("Populating class symbol tables...");
            SymbolTableBuilder sBuilder = new SymbolTableBuilder(sample.getName());
            sBuilder.visitCoolProgram(top);

            System.out.println("Inferring types...");
            TypeSetter ts = new TypeSetter();
            ts.visitCoolProgram(top);

            System.out.println("Verifying types...");
            TypeChecker tc = new TypeChecker(sample.getName());
            tc.visitCoolProgram(top);

            System.out.println("Generating dispatch tables...");
            for (CoolClass cc : top.getClasses()) {
                System.out.println(cc.codeGenDispatchTable());
            }
        }
    }
}
