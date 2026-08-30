package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

import java.util.Map;
import java.util.Set;

public class SymbolTablePrinter implements AstVisitor {
    private int indent = 0;
    private String space = " ";
    private int offset = 2;

    public void printSymbol(Map.Entry<CoolIdentifier, CoolBaseNode> entry) {
        String msg = String.format("%s : %s", entry.getKey(), entry.getValue());
        System.out.println(this.space.repeat(this.indent) + msg);
    }

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        this.indent += offset;
        camd.getLhs().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        this.indent += offset;
        ca.getValue().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        this.indent += offset;
        cas.getExpression().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        this.indent += offset;
        cbo.getLhs().accept(this);
        cbo.getRhs().accept(this);
        this.indent -= offset;
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
        SymbolTable st = cca.getSymbols();
        for (Map.Entry<CoolIdentifier, CoolBaseNode> entry : st.getHashMap().entrySet()) {
            this.printSymbol(entry);
        }
        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        this.indent += offset;
        for (Map.Entry<CoolIdentifier, CoolBaseNode> entry : ccb.getSymbols().getHashMap().entrySet()) {
            this.printSymbol(entry);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        System.out.println(this.space.repeat(this.indent) + cc);
        SymbolTable symbols = cc.getSymbols();

        this.indent += offset;
        for (Map.Entry<CoolIdentifier, CoolBaseNode> entry : symbols.getHashMap().entrySet()) {
            this.printSymbol(entry);
        }

        for (CoolMethod cm : cc.getMethods()) {
            cm.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {
        this.indent += offset;
        CoolExpr ce = cdmd.getLhs();
        ce.accept(this);
        this.indent -= offset;
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
        this.indent += offset;
        CoolExpr predExpr = cif.getPredicate();
        CoolExpr thenExpr = cif.getThenExpr();
        CoolExpr elseExpr = cif.getElseExpr();

        predExpr.accept(this);
        thenExpr.accept(this);
        elseExpr.accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        this.indent += offset;
        CoolExpr ce = civ.getExpression();
        ce.accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        System.out.println(this.space.repeat(this.indent) + cl);

        this.indent += offset;
        for (Map.Entry<CoolIdentifier, CoolBaseNode> entry : cl.getSymbols().getHashMap().entrySet()) {
            this.printSymbol(entry);
        }
        cl.getExpression().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        System.out.println(this.space.repeat(this.indent) + cm.getName().getValue() + "{");
        this.indent += offset;
        SymbolTable st = cm.getSymbols();
        for (Map.Entry<CoolIdentifier, CoolBaseNode> entry : st.getHashMap().entrySet()) {
            this.printSymbol(entry);
        }
        for (CoolExpr ce : cm.getExpressions()) {
            ce.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {
        System.out.println(this.space.repeat(this.indent) + cmd);
        this.indent += offset;
        for (CoolExpr ce : cmd.getArguments()) {
            ce.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {
    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {
        this.indent += offset;
        cpe.getExpression().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        System.out.println(this.space.repeat(indent) + "CoolProgram:");
        this.indent += offset;
        for (CoolClass cc : cp.getClasses()) {
            cc.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        this.indent += offset;
        cuo.getExpression().accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        this.indent += offset;
        cw.getPredicate().accept(this);
        cw.getBody().accept(this);
        this.indent -= offset;
    }
}
