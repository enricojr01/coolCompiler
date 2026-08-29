package com.enricojr.coollang.ast;

import java.util.ArrayList;

import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

public class AstPrinter implements AstVisitor {
    // oh this is going to be SO bad.
    private int indent = 0;
    private String space = " ";

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        System.out.println(this.space.repeat(this.indent) + camd);
        this.indent += 2;
        camd.getLhs().accept(this);
        this.indent -= 2;
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        System.out.println(this.space.repeat(this.indent) + ca);
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        System.out.println(this.space.repeat(this.indent) + cas);
        this.indent += 2;
        cas.getExpression().accept(this);
        this.indent -= 2;
    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        System.out.println(this.space.repeat(this.indent) + cbo);
        this.indent += 2;
        cbo.getLhs().accept(this);
        cbo.getRhs().accept(this);
        this.indent -= 2;
    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        System.out.println(this.space.repeat(this.indent) + cb);
        this.indent += 2;
        for (CoolExpr ce : cb.getExpressions()) {
            ce.accept(this);
        }
        this.indent -= 2;
    }


    @Override
    public void visitCoolCase(CoolCase cca) {
        System.out.println(this.space.repeat(this.indent) + cca);
        this.indent += 2;
        cca.getPredicate().accept(this);
        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.accept(this);
        }
        this.indent -= 2;
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        System.out.println(this.space.repeat(this.indent) + ccb);
        this.indent += 2;
        ccb.getExpression().accept(this);
        this.indent -= 2;
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        System.out.println(this.space.repeat(this.indent) + cc);
        this.indent += 2;
        for (CoolAttribute ca : cc.getAttributes()) {
            System.out.println(this.space.repeat(this.indent) + ca);
        }
        for (CoolMethod cm : cc.getMethods()) {
            System.out.println(this.space.repeat(this.indent) + cm);
            cm.accept(this);
        }
        this.indent -= 2;
    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {
        System.out.println(this.space.repeat(this.indent) + cdmd);
        this.indent += 2;
        cdmd.getLhs().accept(this);
        for (CoolExpr ce : cdmd.getArguments()) {
            ce.accept(this);
        }
        this.indent -= 2;
    }

    @Override
    public void visitCoolExpr(CoolExpr ce) {
        ce.accept(this);
    }

    @Override
    public void visitCoolFormal(CoolFormal cf) {
        System.out.println(this.space.repeat(this.indent) + cf);
    }

    @Override
    public void visitCoolIf(CoolIf cif) {
        System.out.println(this.space.repeat(this.indent) + cif);
        this.indent += 2;
        cif.getPredicate().accept(this);
        cif.getThenExpr().accept(this);
        cif.getElseExpr().accept(this);
        this.indent -= 2;
    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
        System.out.println(this.space.repeat(this.indent) + ci);
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        System.out.println(this.space.repeat(this.indent) + civ);
        this.indent += 2;
        civ.getExpression().accept(this);
        this.indent -= 2;
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        System.out.println(this.space.repeat(this.indent) + cl);
        this.indent += 2;
        cl.getExpression().accept(this);
        this.indent -= 2;
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        System.out.println(this.space.repeat(this.indent) + cm);
        this.indent += 2;
        for (CoolExpr ce : cm.getExpressions()) {
            ce.accept(this);
        }
        this.indent -= 2;
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {
        System.out.println(this.space.repeat(this.indent) + cmd);
        this.indent += 2;
        for (CoolExpr ce : cmd.getArguments()) {
            ce.accept(this);
        }
        this.indent -= 2;

    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {
        System.out.println(this.space.repeat(this.indent) + cpl);
        this.indent += 2;
        for (CoolFormal cf : cpl.getParameters()) {
            cf.accept(this);
        }
        this.indent -= 2;
    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {
        System.out.println(this.space.repeat(this.indent) + cpe);
        this.indent += 2;
        cpe.getExpression().accept(this);
        this.indent -= 2;
    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        System.out.println(this.space.repeat(this.indent) + cp);
        this.indent += 2;
        for (CoolClass cc : cp.getClasses()) {
            cc.accept(this);
        }
        this.indent -= 2;
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        System.out.println(this.space.repeat(this.indent) + cuo);
        this.indent += 2;
        cuo.getExpression().accept(this);
        this.indent -= 2;
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        System.out.println(this.space.repeat(this.indent) + cw);
        this.indent += 2;
        cw.getPredicate().accept(this);
        cw.getBody().accept(this);
        this.indent -= 2;
    }
}
