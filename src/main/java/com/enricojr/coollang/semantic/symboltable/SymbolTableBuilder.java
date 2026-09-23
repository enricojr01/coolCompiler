package com.enricojr.coollang.semantic.symboltable;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.semantic.exceptions.StackTraceException;
import com.enricojr.coollang.semantic.exceptions.SymbolTableException;

import java.util.ArrayList;
import java.util.LinkedList;

public class SymbolTableBuilder implements AstVisitor {
    private String filename;
    private LinkedList<CoolBaseNode> stack = new LinkedList<>();

    public SymbolTableBuilder(String filename) {
        this.filename = filename;
    }

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        SymbolTable current = camd.getSymbols();

        CoolExpr lhs = camd.getLhs();
        lhs.setSymbols(new SymbolTable(current));
        lhs.accept(this);

        CoolIdentifier className = camd.getClassName();
        if (className.equals(new CoolIdentifier("SELF_TYPE"))) {
            String msg = String.format(
                    "Type name in a static dispatch (%s) cannot be SELF_TYPE.",
                    camd
            );
            throw new RuntimeException(msg);
        }
        className.setSymbols(new SymbolTable(current));

        CoolIdentifier methodName = camd.getMethodName();
        methodName.setSymbols(new SymbolTable(current));

        for (CoolExpr ce : camd.getArguments()) {
            ce.setSymbols(new SymbolTable(current));
            ce.accept(this);
        }
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        SymbolTable current = ca.getSymbols();
        if (ca.getInitExpression() != null) {
            CoolExpr expr = ca.getInitExpression();
            expr.setSymbols(new SymbolTable(current));
            expr.accept(this);
        }
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
        lhs.accept(this);

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
        String location = "SymbolTableBuilder-visitCoolClass";

        current.addSymbolType(new CoolIdentifier("SELF_TYPE"), cc);
        current.addSymbolType(new CoolIdentifier("self"), cc);

        for (CoolAttribute ca : cc.getAttributes()) {
            ca.setSymbols(new SymbolTable(current));
            ca.accept(this);
            CoolClass type = current.getSymbolType(ca.getTypeName());
            current.addSymbolType(ca.getIdentifier(), type);
        }

        for (CoolMethod cm : cc.getMethods()) {
            SymbolTable method = new SymbolTable(current);
            cm.setSymbols(method);
            cm.accept(this);

            CoolIdentifier returnType = cm.getReturnType();
            CoolClass concreteReturnType = current.getSymbolType(returnType);
            current.addMethod(cm.getName(), cm.getSymbols(), concreteReturnType, cm);
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
        elseExpr.accept(this);

    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        SymbolTable current = civ.getSymbols();
        CoolExpr expr = civ.getExpression();
        expr.setSymbols(new SymbolTable(current));
        expr.accept(this);
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        SymbolTable current = cl.getSymbols();
        for (CoolAttribute ca : cl.getAttributes()) {
            ca.setSymbols(new SymbolTable(current));
            // need to recurse into it so that the symbol table is correctly propagated.
            ca.accept(this);
            CoolIdentifier id = ca.getIdentifier();
            CoolClass type = current.getSymbolType(ca.getTypeName());
            if (type == null) {
                String msg = String.format(
                        "Attribute %s references unknown type %s",
                        ca.getIdentifier().getValueString(),
                        ca.getTypeName().getValueString()
                );
                throw SymbolTableException.factory(msg, ca);
            }
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
        cm.getParameters().setSymbols(new SymbolTable(current));
        cm.getParameters().accept(this);
        // TODO: should I not recurse into CoolFormal? The parameters need to be visible at the method level
        for (CoolFormal cf : cm.getParameters().getParameters()) {
            CoolClass type = current.getSymbolType(cf.getType());
            params.addSymbolType(cf.getName(), type);
            current.addSymbolType(cf.getName(), type);
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
        for (CoolFormal cf : cpl.getParameters()) {
            if (cf.getType().equals(new CoolIdentifier("SELF_TYPE"))) {
                String msg = String.format("SELF_TYPE cannot be used as the type of a formal paramter (%s). ", cf);
                throw new RuntimeException(msg);
            }
        }
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

        // "Object" should exist in the symbol table.
        current.addSymbolType(cp.getRoot().getName(), cp.getRoot());

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

    @Override
    public void visitCoolString(CoolString cs) {

    }

    @Override
    public void visitCoolBool(CoolBool cb) {

    }

    @Override
    public void visitCoolInteger(CoolInteger ci) {

    }

    @Override
    public void visitCoolSelf(CoolSelf cs) {

    }

    @Override
    public void visitCoolIdentifier(CoolIdentifier ci) {

    }
}
