package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

import java.util.ArrayList;

public class SymbolTableBuilder implements AstVisitor {
    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        CoolExpr ce = camd.getLhs();
        ce.accept(this);
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        CoolExpr ce = ca.getValue();
        ce.accept(this);
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        CoolExpr ce = cas.getExpression();
        ce.accept(this);
    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        CoolExpr lhs = cbo.getLhs();
        CoolExpr rhs = cbo.getRhs();
        rhs.accept(this);
        lhs.accept(this);
    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        ArrayList<CoolExpr> exprs = cb.getExpressions();
        for (CoolExpr ce : exprs) {
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolCase(CoolCase cca) {
        // This symbol table is empty so that the chain of tables
        // preserved - lookups that start in the branches should basically
        // skip over this one as it travels up the chain.
        SymbolTable st = new SymbolTable();
        cca.setSymbols(st);

        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.accept(this);
            ccb.getSymbols().setParent(st);
        }
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        SymbolTable st = new SymbolTable();
        ccb.setSymbols(st);

        CoolFormal cf = ccb.getFormal();
        CoolExpr ce = ccb.getExpression();
        // NOTE: I will probably need the Type of the formal later, but I don't know if its a good idea to store
        //       just the formal vs the entire branch, so I'll err on the side of caution and shove the whole
        //       branch in there
        st.addSymbol(cf.getName(), ccb);

        ce.accept(this);
        if (ce.getSymbols() != null) {
            ce.getSymbols().setParent(st);
        }
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        SymbolTable st = new SymbolTable();
        cc.setSymbols(st);
        for (CoolAttribute ca : cc.getAttributes()) {
            st.addSymbol(ca.getIdentifier(), ca);
        }

        for (CoolMethod cm : cc.getMethods()) {
            st.addSymbol(cm.getName(), cm);
            cm.accept(this);
            // still kinda weird but OK
            cm.getSymbols().setParent(st);
        }

    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {
        CoolExpr lhs = cdmd.getLhs();
        lhs.accept(this);
    }

    @Override
    public void visitCoolExpr(CoolExpr ce) {
        ce.accept(this);
    }

    @Override
    public void visitCoolFormal(CoolFormal cf) {

    }

    @Override
    public void visitCoolIf(CoolIf cif) {
        CoolExpr predExpr = cif.getPredicate();
        CoolExpr thenExpr = cif.getThenExpr();
        CoolExpr elseExpr = cif.getElseExpr();
        predExpr.accept(this);
        thenExpr.accept(this);
        elseExpr.accept(this);
    }


    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        CoolExpr ce = civ.getExpression();
        ce.accept(this);
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        SymbolTable st = new SymbolTable();
        CoolExpr ce = cl.getExpression();
        cl.setSymbols(st);

        for (CoolAttribute ca : cl.getAttributes()) {
            st.addSymbol(ca.getIdentifier(), ca);
        }

        ce.accept(this);
        if (ce.getSymbols() != null) {
            ce.getSymbols().setParent(st);
        }
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        SymbolTable st = new SymbolTable();
        cm.setSymbols(st);
        CoolParamList cpl = cm.getParameters();

        for (CoolFormal cf : cpl.getParameters()) {
            st.addSymbol(cf.getName(), cf);
        }

        for (CoolExpr ce : cm.getExpressions()) {
            ce.accept(this);
            if (ce.getSymbols() != null) {
                ce.getSymbols().setParent(st);
            }
        }
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {
        for (CoolExpr ce : cmd.getArguments()) {
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {
    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {
        cpe.getExpression().accept(this);
    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        SymbolTable st = new SymbolTable();
        cp.setSymbols(st);

        for (CoolClass cc : cp.getClasses()) {
            cc.accept(this);
        }
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        CoolExpr ce = cuo.getExpression();
        ce.accept(this);
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        CoolExpr pred = cw.getPredicate();
        CoolExpr body = cw.getBody();
        pred.accept(this);
        body.accept(this);
    }
}
