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

        if (!(declaredType.equalOrSubrelation(computedValueType))) {
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

        if (!(computedType.equalOrSubrelation(declaredType))) {
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

                if (!(rhs.getComputedType().equalOrSubrelation(expected)) ||
                        !(lhs.getComputedType().equalOrSubrelation(expected))) {
                    String msg = String.format(
                            "Left hand side %s and/or right hand side %s expression does not match expected value %s",
                            lhs.getComputedType(),
                            rhs.getComputedType(),
                            expected
                    );
                    throw new RuntimeException(msg);
                }

                CoolClass computedType = current.getSymbolType(new CoolIdentifier("Int"));
                cbo.setComputedType(computedType);
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

                if (!(rhs.getComputedType().equalOrSubrelation(expected)) ||
                        !(lhs.getComputedType().equalOrSubrelation(expected))) {
                    String msg = String.format(
                            "Left hand side %s and/or right hand side %s expression does not match expected value %s",
                            lhs.getComputedType(),
                            rhs.getComputedType(),
                            expected
                    );
                    throw new RuntimeException(msg);
                }

                CoolClass computedType = current.getSymbolType(new CoolIdentifier("Bool"));
                cbo.setComputedType(computedType);
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
        ce.accept(this);
    }

    @Override
    public void visitCoolFormal(CoolFormal cf) {
        SymbolTable current = cf.getSymbols();
        CoolClass target = current.getSymbolType(cf.getType());
        if (target == null) {
            String msg = String.format(
                    "Variable %s declares a type %s that does not exist!",
                    cf.getName(),
                    cf.getType()
            );
            throw new RuntimeException(msg);
        }
    }

    @Override
    public void visitCoolIf(CoolIf cif) {
        SymbolTable current = cif.getSymbols();
        CoolClass boolTarget = current.getSymbolType(new CoolIdentifier("Bool"));
        CoolExpr pred = cif.getPredicate();
        pred.accept(this);
        if (!(pred.getComputedType().equalOrSubrelation(boolTarget))) {
            // TODO: need to get line numbers into this somehow these error messages are nigh useless otherwise!
            String msg = String.format("Predicate of an if statement must be a bool");
            throw new RuntimeException(msg);
        }
        CoolExpr then = cif.getThenExpr();
        then.accept(this);
        CoolExpr elseExpr = cif.getElseExpr();
        elseExpr.accept(this);

        // TODO: Figure out exactly what the "join" is, and how it determines the type of the if
        // i.e. given T and F as the static types of the branches, the static type of the conditional is T join F
        // or some shit
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
