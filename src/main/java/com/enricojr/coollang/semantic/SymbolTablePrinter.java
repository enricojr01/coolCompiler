package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

public class SymbolTablePrinter implements AstVisitor {

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        camd.getLhs().accept(this);
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        ca.getValue().accept(this);
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        cas.getExpression().accept(this);
    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        cbo.getLhs().accept(this);
        cbo.getRhs().accept(this);
    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        for (CoolExpr ce : cb.getExpressions()) {
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolCase(CoolCase cca) {
        System.out.println("CoolCase: ");
        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.accept(this);
        }
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        System.out.println(ccb.getSymbols());
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        System.out.println("========");
        System.out.println("Class: " + cc.getName().getValue());
        System.out.println(cc.getSymbols());

        for (CoolMethod cm : cc.getMethods()) {
            System.out.println("Method: " + cm.getName().getValue() + " {");
            if (cm.getSymbols() != null) {
                System.out.println(cm.getSymbols());
            }
            cm.accept(this);
            System.out.println("}");
        }
        System.out.println("========");
    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {
        CoolExpr ce = cdmd.getLhs();
        ce.accept(this);
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
        System.out.println("CoolLet - Symbol Table:");
        System.out.println(cl.getSymbols());

        cl.getExpression().accept(this);
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        for (CoolExpr ce : cm.getExpressions()) {
            ce.accept(this);
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
        for (CoolClass cc : cp.getClasses()) {
            cc.accept(this);
        }
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        cuo.getExpression().accept(this);
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        cw.getPredicate().accept(this);
        cw.getBody().accept(this);
    }
}
