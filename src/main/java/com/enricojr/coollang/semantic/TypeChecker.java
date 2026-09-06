package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;

public class TypeChecker implements AstVisitor {
    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {

    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        SymbolTable current = ca.getSymbols();

        CoolExpr value = ca.getValue();
        value.accept(this);

        CoolClass declaredType = current.getSymbolType(ca.getTypeName());
        CoolClass computedValueType = value.getComputedType();

        if (!(declaredType.equals(computedValueType))) {
            String msg = String.format(
                    "Value assigned to attribute %s does not match declared type %s",
                    computedValueType.getName(),
                    declaredType.getName()
            );
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        SymbolTable current = cas.getSymbols();
        CoolIdentifier variable = cas.getName();
        CoolClass declaredType = current.getSymbolType(variable);

        CoolExpr assignment = cas.getExpression();
        assignment.accept(this);
        CoolClass computedType = assignment.getComputedType();

        if (!(computedType.equals(declaredType))) {
            String msg = String.format(
                    "Value of expression in assignment to %s (%s) does not match type of %s (%s)",
                    variable.getValueString(),
                    computedType,
                    variable.getValueString(),
                    declaredType
            );
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        SymbolTable current = cbo.getSymbols();
        CoolBinaryOp.OPERATOR op = cbo.getOp();

        // add, mul, div, sub all take ints only
        // LT, LTE, GT, GTE all take only bool
        switch (op) {
            case ADD:
            case SUB:
            case MUL:
            case DIV: {
                CoolClass expected = current.getSymbolType(new CoolIdentifier("Int"));
                CoolExpr lhs = cbo.getLhs();
                lhs.accept(this);
                CoolExpr rhs = cbo.getRhs();
                rhs.accept(this);

                if (!(rhs.getComputedType().equals(expected)) || !(lhs.getComputedType().equals(expected))) {
                    String msg = String.format(
                            "Left hand side %s and/or right hand side %s expression does not match expected value %s",
                            lhs.getComputedType(),
                            rhs.getComputedType(),
                            expected
                    );
                    throw new RuntimeException(msg);
                }
                break;
            }
            case GT:
            case GTE:
            case LT:
            case LTE: {
                CoolClass expected = current.getSymbolType(new CoolIdentifier("String"));
                CoolExpr lhs = cbo.getLhs();
                lhs.accept(this);
                CoolExpr rhs = cbo.getRhs();
                rhs.accept(this);

                if (!(rhs.getComputedType().equals(expected)) || !(lhs.getComputedType().equals(expected))) {
                    String msg = String.format(
                            "Left hand side %s and/or right hand side %s expression does not match expected value %s",
                            lhs.getComputedType(),
                            rhs.getComputedType(),
                            expected
                    );
                    throw new RuntimeException(msg);
                }
                break;
            }
        }
    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        // the type of a block is the type of the last expression in that block
        for (CoolExpr ce : cb.getExpressions()) {
            ce.accept(this);
        }
        CoolExpr last = cb.getExpressions().getLast();
        cb.setComputedType(last.getComputedType());
    }

    @Override
    public void visitCoolCase(CoolCase cca) {
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {

    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        for (CoolAttribute ca : cc.getAttributes()) {
            ca.accept(this);
        }

        for (CoolMethod cm : cc.getMethods()) {
            cm.accept(this);
        }

        for (CoolClass child : cc.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {

    }

    @Override
    public void visitCoolExpr(CoolExpr ce) {

    }

    @Override
    public void visitCoolFormal(CoolFormal cf) {

    }

    @Override
    public void visitCoolIf(CoolIf cif) {

    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {

    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {

    }

    @Override
    public void visitCoolLet(CoolLet cl) {

    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        // goal: make sure that the method returns a value that matches its type
        // the type of the method is the type of the final expression in its body
        SymbolTable current = cm.getSymbols();
        CoolClass returnType = current.getMethodType(cm.getName());

        for (CoolExpr ce : cm.getExpressions()) {
            ce.accept(this);
        }

        CoolExpr last = cm.getExpressions().getLast();
        CoolClass lastType = last.getComputedType();
        if (!(lastType.equals(returnType))) {
            // TODO: no really, I need to write better error messages.
            String msg = String.format(
                    "Return value of method %s (%s) does not match its declared return type %s.",
                    cm.getName(),
                    lastType,
                    returnType
            );
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {

    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {

    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {

    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        cp.getRoot().accept(this);
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {

    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {

    }
}
