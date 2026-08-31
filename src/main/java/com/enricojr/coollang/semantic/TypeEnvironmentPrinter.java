package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

import java.util.HashMap;
import java.util.Map;

public class TypeEnvironmentPrinter implements AstVisitor {
    private int indent = 0;
    private String space = " ";
    private int offset = 2;

    public void printSymbol(Map.Entry<CoolIdentifier, CoolClass> entry) {
        String msg = String.format("Symbol - %s : %s", entry.getKey(), entry.getValue());
        System.out.println(this.space.repeat(this.indent) + msg);
    }

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        camd.getLhs().accept(this);

        this.indent += offset;
        for (CoolExpr ce : camd.getArguments()) {
            ce.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
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
        this.indent += offset;
        for (CoolExpr ce : cb.getExpressions()) {
            ce.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolCase(CoolCase cca) {
        System.out.println(this.space.repeat(this.indent) + cca);
        this.indent += offset;
        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        HashMap<CoolIdentifier, CoolClass> types = ccb.getTypes().getEnvironment();
        this.indent += 1;
        for (Map.Entry<CoolIdentifier, CoolClass> entry : types.entrySet()) {
            this.printSymbol(entry);
        }

        ccb.getExpression().accept(this);
        this.indent -= 1;
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        System.out.println(this.space.repeat(this.indent) + cc);
        HashMap<CoolIdentifier, CoolClass> types = cc.getTypes().getEnvironment();
        this.indent += offset;
        for (Map.Entry<CoolIdentifier, CoolClass> entry : types.entrySet())  {
            this.printSymbol(entry);
        }

        for (CoolMethod cm : cc.getMethods()) {
            cm.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {
        cdmd.getLhs().accept(this);
        for (CoolExpr ce : cdmd.getArguments()) {
            ce.accept(this);
        }
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
        cif.getPredicate().accept(this);
        cif.getThenExpr().accept(this);
        cif.getElseExpr().accept(this);
    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        civ.getExpression().accept(this);
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        System.out.println(this.space.repeat(this.indent) + cl);
        HashMap<CoolIdentifier, CoolClass> types = cl.getTypes().getEnvironment();

        this.indent += offset;
        for (Map.Entry<CoolIdentifier, CoolClass> entry : types.entrySet()) {
            this.printSymbol(entry);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        System.out.println(this.space.repeat(this.indent) + cm + " {");
        HashMap<CoolIdentifier, CoolClass> types = cm.getTypes().getEnvironment();

        this.indent += offset;
        for (CoolExpr ce : cm.getExpressions()) {
            ce.accept(this);
        }
        this.indent -= offset;
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
        System.out.println(this.space.repeat(this.indent) + cp);
        HashMap<CoolIdentifier, CoolClass> types = cp.getTypes().getEnvironment();

        this.indent += offset;
        for (Map.Entry<CoolIdentifier, CoolClass> entry : types.entrySet()) {
            this.printSymbol(entry);
        }

        for (CoolClass cc : cp.getClasses()) {
            cc.accept(this);
        }
        this.indent -= offset;
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
