package com.enricojr.coollang.semantic.symboltable;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

public class SymbolTableBuilder implements AstVisitor {
    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        SymbolTable current = camd.getSymbols();
        CoolExpr lhs = camd.getLhs();
        lhs.setSymbols(new SymbolTable(current));
        lhs.accept(this);
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        SymbolTable current = ca.getSymbols();
        CoolExpr expr = ca.getInitExpression();
        expr.setSymbols(new SymbolTable(current));
        expr.accept(this);
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        SymbolTable current = cas.getSymbols();
        CoolExpr expr = cas.getExpression();
        expr.setSymbols(new SymbolTable(current));
        expr.accept(this);

    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        SymbolTable current = cbo.getSymbols();
        CoolExpr lhs = cbo.getLhs();
        lhs.setSymbols(new SymbolTable(current));

        CoolExpr rhs = cbo.getRhs();
        rhs.setSymbols(new SymbolTable(current));
        rhs.accept(this);

    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        SymbolTable current = cb.getSymbols();
        for (CoolExpr ce : cb.getExpressions()) {
            ce.setSymbols(new SymbolTable(current));
            ce.accept(this);
        }
    }


    @Override
    public void visitCoolCase(CoolCase cca) {
        SymbolTable current = cca.getSymbols();

        CoolExpr predicate = cca.getPredicate();
        predicate.setSymbols(new SymbolTable(current));
        predicate.accept(this);

        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.setSymbols(new SymbolTable(current));
            ccb.accept(this);
        }
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        SymbolTable current = ccb.getSymbols();
        CoolFormal cf = ccb.getFormal();

        CoolClass formalType = current.getSymbolType(cf.getType());
        current.addSymbolType(cf.getName(), formalType);

        CoolExpr expr = ccb.getExpression();
        expr.setSymbols(new SymbolTable(current));
        expr.accept(this);
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        SymbolTable current = cc.getSymbols();

        current.addSymbolType(new CoolIdentifier("SELF_TYPE"), cc);
        current.addSymbolType(new CoolIdentifier("self"), cc);

        for (CoolAttribute ca : cc.getAttributes()) {
            CoolClass type = current.getSymbolType(ca.getTypeName());
            current.addSymbolType(ca.getIdentifier(), type);
        }

        for (CoolMethod cm : cc.getMethods()) {
            SymbolTable method = new SymbolTable(current);
            cm.setSymbols(method);

            cm.accept(this);
        }

        for (CoolClass child : cc.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {
        SymbolTable current = cdmd.getSymbols();

        for (CoolExpr ce : cdmd.getArguments()) {
            ce.setSymbols(new SymbolTable(current));
            ce.accept(this);
        }

        CoolExpr expr = cdmd.getClassName();
        expr.setSymbols(new SymbolTable(current));
        expr.accept(this);
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
        SymbolTable current = cif.getSymbols();

        CoolExpr predicate = cif.getGuard();
        predicate.setSymbols(new SymbolTable(current));
        predicate.accept(this);

        CoolExpr thenExpr = cif.getConsequent();
        thenExpr.setSymbols(new SymbolTable(current));
        thenExpr.accept(this);

        CoolExpr elseExpr = cif.getAlternative();
        elseExpr.setSymbols(new SymbolTable(current));
        thenExpr.accept(this);

    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        CoolExpr expr = civ.getExpression();
        expr.accept(this);
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        SymbolTable current = cl.getSymbols();
        for (CoolAttribute ca : cl.getAttributes()) {
            System.out.println(ca);
            CoolIdentifier id = ca.getIdentifier();
            CoolClass type = current.getSymbolType(ca.getTypeName());
            current.addSymbolType(id, type);
        }
        CoolExpr expr = cl.getExpression();
        expr.setSymbols(new SymbolTable(current));
        expr.accept(this);
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        SymbolTable current = cm.getSymbols();

        CoolClass returnType = current.getSymbolType(cm.getReturnType());
        SymbolTable params = new SymbolTable(current);
        for (CoolFormal cf : cm.getParameters().getParameters()) {
            CoolClass type = current.getSymbolType(cf.getType());
            params.addSymbolType(cf.getName(), type);
        }
        current.addMethod(cm.getName(), params, returnType, cm);

        for (CoolExpr ce : cm.getBody()) {
            SymbolTable next = new SymbolTable(current);
            ce.setSymbols(next);
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {
        SymbolTable current = cmd.getSymbols();

        for (CoolExpr ce : cmd.getArguments()) {
            ce.setSymbols(new SymbolTable(current));
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {
    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {
        SymbolTable current = cpe.getSymbols();
        CoolExpr ce = cpe.getExpression();
        ce.setSymbols(new SymbolTable(current));
        ce.accept(this);
    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        SymbolTable current = cp.getSymbols();

        for (CoolClass cc : cp.getClasses()) {
            current.addSymbolType(cc.getName(), cc);
        }

        cp.getRoot().accept(this);
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        SymbolTable current = cuo.getSymbols();
        CoolExpr ce = cuo.getExpression();
        ce.setSymbols(new SymbolTable(current));
        ce.accept(this);
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        SymbolTable current = cw.getSymbols();
        CoolExpr predicate = cw.getPredicate();
        predicate.setSymbols(new SymbolTable(current));
        predicate.accept(this);

        CoolExpr body = cw.getBody();
        body.setSymbols(new SymbolTable(current));
        body.accept(this);
    }
}
