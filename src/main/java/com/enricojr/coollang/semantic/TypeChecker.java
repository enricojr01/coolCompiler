package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.semantic.exceptions.TypeCheckerException;
import com.enricojr.coollang.semantic.symboltable.SymbolTable;

import java.util.ArrayList;

public class TypeChecker implements AstVisitor {
    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        SymbolTable current = camd.getSymbols();
        CoolExpr ce = camd.getLhs();
        ce.accept(this);
        CoolClass exprType = ce.getComputedType();
        CoolClass targetClass = current.getSymbolType(camd.getClassName());

        if (!(exprType.equalOrSubrelation(targetClass))) {
            String msg = String.format("Both sides of @ must evaluate to the same type, (%s @ %s)", exprType, targetClass);
            throw TypeCheckerException.factory(msg, camd);
        }



    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        SymbolTable current = ca.getSymbols();

        CoolExpr value = ca.getInitExpression();
        value.accept(this);

        CoolClass declaredType = current.getSymbolType(ca.getTypeName());
        CoolClass computedValueType = value.getComputedType();

        if (!(declaredType.equalOrSubrelation(computedValueType))) {
            String msg = String.format(
                    "Value assigned to attribute %s does not match declared type %s",
                    computedValueType.getName(),
                    declaredType.getName()
            );
            throw TypeCheckerException.factory(msg, ca);
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
            throw TypeCheckerException.factory(msg, cas);
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
                    throw TypeCheckerException.factory(msg, cbo);
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
                    throw TypeCheckerException.factory(msg, cbo);
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
        SymbolTable current = cca.getSymbols();
        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.accept(this);
        }

        // NOTE: this is horrible
        for (CoolCaseBranch ccb1 : cca.getBranches()) {
            for (CoolCaseBranch ccb2 : cca.getBranches()) {
                if (ccb1.getComputedType().equals(ccb2.getComputedType())) {
                    continue;
                } else {
                    if (!(ccb1.getComputedType().equalOrSubrelation(ccb2.getComputedType()))) {
                        String msg = String.format(
                                "All branches in a case expression must be equal / have a common ancestor! %s != %s",
                                ccb1,
                                ccb2
                        );
                        throw TypeCheckerException.factory(msg, cca);
                    }
                }
            }
        }
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        CoolExpr expr = ccb.getExpression();
        expr.accept(this);
        CoolClass result = expr.getComputedType();
        ccb.setComputedType(result);
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
        SymbolTable current = cdmd.getSymbols();
        CoolExpr className = cdmd.getClassName();
        className.accept(this);
        CoolClass concreteClass = className.getComputedType();

        if (concreteClass == null) {
            String msg = String.format("Static dispatch expression evalutates to invalid type: %s", className.getComputedType());
            throw TypeCheckerException.factory(msg, cdmd);
        }

        // check for the correct method by name
        CoolIdentifier methodName = cdmd.getMethodName();
        CoolMethod methodObj = concreteClass.classMethodSearch(methodName);
        if (methodObj == null) {
            String msg = String.format(
                    "Class %s does not have method named method %s.",
                    concreteClass.getNameString(),
                    methodName.getValueString()
            );
            throw TypeCheckerException.factory(msg, concreteClass);
        }

        // check for the correct # of params/args
        ArrayList<CoolExpr> args = cdmd.getArguments();
        ArrayList<CoolFormal> params = methodObj.getParameters().getParameters();
        if (args.size() != params.size()) {
            String msg = String.format(
                    "Method call %s has the incorrect # of arguments. Expected %s and but only %s were found.",
                    methodObj.getName().getValueString(),
                    params.size(),
                    args.size()
            );
            throw TypeCheckerException.factory(msg, cdmd);
        }

        // check each arg to see if it matches the formal definition, both position and type must match
        for (CoolExpr ce : args) {
            for (CoolFormal cf : params) {
                ce.accept(this);
                CoolClass providedType = ce.getComputedType();
                CoolClass expectedType = current.getSymbolType(cf.getType());
                if (!(providedType.equalOrSubrelation(expectedType))) {
                    String msg = String.format(
                            "In method call %s, argument %s of type %s does not match expected type %s",
                            cdmd,
                            ce,
                            providedType,
                            expectedType
                    );
                    throw TypeCheckerException.factory(msg, cdmd);
                }
            }
        }

        CoolClass computedType = current.getSymbolType(methodObj.getReturnType());
        cdmd.setComputedType(computedType);
    }

    @Override
    public void visitCoolExpr(CoolExpr ce) {
        ce.accept(this);
    }

    @Override
    public void visitCoolFormal(CoolFormal cf) {
        // TODO: Should this not have been done while building the symbol table
        SymbolTable current = cf.getSymbols();
        CoolClass target = current.getSymbolType(cf.getType());
        if (target == null) {
            String msg = String.format(
                    "Variable %s declares a type %s that does not exist!",
                    cf.getName(),
                    cf.getType()
            );
            throw TypeCheckerException.factory(msg, cf);
        }
    }

    @Override
    public void visitCoolIf(CoolIf cif) {
        // TODO: refactor fields on CoolIf to be "guard", "consequent", "alternative".
        SymbolTable current = cif.getSymbols();
        CoolClass boolTarget = current.getSymbolType(new CoolIdentifier("Bool"));
        CoolExpr pred = cif.getGuard();
        pred.accept(this);

        if (!(pred.getComputedType().equals(boolTarget))) {
            String msg = String.format("Predicate of an if statement must be a bool");
            throw TypeCheckerException.factory(msg, cif);
        }

        CoolExpr then = cif.getConsequent();
        then.accept(this);
        CoolExpr elseExpr = cif.getAlternative();
        elseExpr.accept(this);

        // the type of an if statement is the least upper bound type between the consequent
        // and alternative. throw an exception if they're not
        if (!(then.getComputedType().equalOrSubrelation(elseExpr.getComputedType()))) {
            String msg = String.format(
                    "Type mismatch between consequent and alternative, %s, %s.",
                    then.getComputedType(),
                    elseExpr.getComputedType()
            );
            throw TypeCheckerException.factory(msg, cif);
        }
    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
        SymbolTable current = ci.getSymbols();
        CoolClass cc = current.getSymbolType(ci.getIdentifier());
        if (cc == null) {
            String msg = String.format("Cannot instantiate non-existant type %s", ci.getIdentifier());
            throw TypeCheckerException.factory(msg, ci);
        } else {
            ci.setComputedType(cc);
        }
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        SymbolTable current = civ.getSymbols();
        CoolClass boolType = current.getSymbolType(new CoolIdentifier("Bool"));
        civ.setComputedType(boolType);
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        // the type of any let expression is the type of the last expression in its body.
        for (CoolAttribute ca : cl.getAttributes()) {
            ca.accept(this);
        }
        CoolExpr body = cl.getExpression();
        body.accept(this);
        cl.setComputedType(body.getComputedType());
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        // goal: make sure that the method returns a value that matches its type
        // the type of the method is the type of the final expression in its body
        SymbolTable current = cm.getSymbols();
        CoolClass returnType = current.getMethodType(cm.getName());

        for (CoolExpr ce : cm.getBody()) {
            ce.accept(this);
        }

        CoolExpr last = cm.getBody().getLast();
        CoolClass lastType = last.getComputedType();
        if (!(lastType.equalOrSubrelation(returnType))) {
            String msg = String.format(
                    "Return value of method %s (%s) does not match its declared return type %s.",
                    cm.getName(),
                    lastType,
                    returnType
            );
            throw TypeCheckerException.factory(msg, cm);
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
        CoolExpr expr = cpe.getExpression();
        expr.accept(this);
        CoolClass result = expr.getComputedType();
        cpe.setComputedType(result);
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
