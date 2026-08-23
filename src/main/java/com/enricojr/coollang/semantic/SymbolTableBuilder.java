package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.expressions.CoolCase;
import com.enricojr.coollang.ast.expressions.CoolCaseBranch;
import com.enricojr.coollang.ast.expressions.CoolExpr;
import com.enricojr.coollang.ast.expressions.CoolLet;
import com.enricojr.coollang.ast.program.*;

import java.util.ArrayList;

public class SymbolTableBuilder {
    private ClassTree tree;

    public SymbolTableBuilder(CoolProgram prog) {
        this.prog = prog;
        this.buildGlobalSymbolTable();
    }

    public SymbolTableBuilder(ClassTree tree) {
        this.tree = tree;
        buildGlobalSymbolTable();
    }

    public void buildGlobalSymbolTable() {
        for (ClassTreeNode ctn : this.tree) {
            CoolClass cc = ctn.getCoolClass();
            SymbolTable global = new SymbolTable();

        }
//        ArrayList<CoolClass> classes = prog.getClasses();
//        SymbolTable global = new SymbolTable();
//        for (CoolClass cc : classes) {
//            global.addSymbol(cc.getName(), cc);
//            this.buildClassSymbolTable(cc);
//        }
    }

    public void buildClassSymbolTable(CoolClass cc) {
        ArrayList<CoolMethod> methods = cc.getMethods();
        SymbolTable classLocal = new SymbolTable();

        for (CoolMethod cm : methods) {
            classLocal.addSymbol(cm.getName(), cm);
            SymbolTable methodLocal = new SymbolTable();

            for (CoolExpr ce : cm.getExpressions()) {
                if (ce instanceof CoolLet) {
                    ArrayList<CoolAttribute> attribs = ((CoolLet) ce).getAttributes();
                    for (CoolAttribute ca : attribs) {
                        methodLocal.addSymbol(ca.getIdentifier(), ca);
                    }
                }
                if (ce instanceof CoolCase) {
                    ArrayList<CoolCaseBranch> branches = ((CoolCase) ce).getBranches();
                    for (CoolCaseBranch ccb : branches) {
                        CoolFormal cf = ccb.getFormal();
                        methodLocal.addSymbol(cf.getName(), ccb);
                    }
                }
            }

            cm.setSymbols(methodLocal);
        }
        cc.setSymbols(classLocal);
    }

    public CoolProgram getProg() {
        return prog;
    }
}
