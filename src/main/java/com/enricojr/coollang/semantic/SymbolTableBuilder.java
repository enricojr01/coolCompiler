package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

import java.util.ArrayList;

public class SymbolTableBuilder {
    private ClassTree tree;

    public SymbolTableBuilder(ClassTree tree) {
        this.tree = tree;
        this.buildGlobalSymbolTable();
    }

    public void buildGlobalSymbolTable() {
        // all classes and their methods go into the symbol table
        // the global table is on the "Object" class, i.e. the root
        // of the tree.
        SymbolTable global = new SymbolTable();

        for (ClassTreeNode ctn : this.tree) {
            CoolClass cc = ctn.getCoolClass();
            this.buildClassSymbolTable(cc);
            global.addSymbol(cc.getName(), cc);
        }
        this.tree.getRoot().getCoolClass().setSymbols(global);
    }

    public void buildClassSymbolTable(CoolClass cc) {
        SymbolTable classLocal = new SymbolTable();
        ArrayList<CoolAttribute> attributes = cc.getAttributes();
        ArrayList<CoolMethod> methods = cc.getMethods();

        if (attributes != null) {
            for (CoolAttribute ca : attributes) {
                classLocal.addSymbol(ca.getIdentifier(), ca);
            }
        }

        if (methods != null) {
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
        }
        cc.setSymbols(classLocal);
    }

    public ClassTree getTree() {
        return this.tree;
    }

    private SymbolTable findDeclarations(SymbolTable st, CoolExpr expr) {
        if (expr instanceof CoolParenthesisExpr) {
            CoolParenthesisExpr cpe = (CoolParenthesisExpr) expr;
            CoolExpr next = cpe.getExpression();
            st.addAll(this.findDeclarations(st, next));
        } else if (expr instanceof CoolLet) {
            CoolLet cl = (CoolLet) expr;
            ArrayList<CoolAttribute> attrs = cl.getAttributes();
            CoolExpr next = cl.getExpression();
            for (CoolAttribute ca : attrs) {
                st.addSymbol(ca.getIdentifier(), ca);
            }
            return this.findDeclarations(st, next);
        } else if (expr instanceof CoolCase) {
            CoolCase cca = (CoolCase) expr;
            ArrayList<CoolCaseBranch> branches = cca.getBranches();
            for (CoolCaseBranch ccb : branches) {
                CoolFormal cf  = ccb.getFormal();
                st.addSymbol(cf.getName(), cf);
            }
        } else if (expr instanceof CoolBlock) {
            CoolBlock cb = (CoolBlock) expr;
            ArrayList<CoolExpr> exprs = cb.getBody();
            for (CoolExpr ce : exprs) {
                st.addAll(this.findDeclarations(st, ce));
            }
        }

        return st;

    }
}
