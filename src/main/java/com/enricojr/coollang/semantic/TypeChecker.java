package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.semantic.exceptions.TypeCheckerException;
import com.enricojr.coollang.semantic.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.LinkedList;

public class TypeChecker implements AstVisitor {
    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        SymbolTable current = camd.getSymbols();
        CoolExpr ce = camd.getLhs();
        ce.accept(this);
        CoolClass exprType = ce.getComputedType();
        CoolClass targetClass = current.getSymbolType(camd.getClassName());

        if (!(CoolClass.equalOrSubrelation(exprType, targetClass))) {
            String msg = String.format("Both sides of @ must evaluate to equal or subrelated classes, (%s @ %s).", exprType, targetClass);
            throw TypeCheckerException.factory(msg, camd);
        }

        CoolIdentifier methodName = camd.getMethodName();
        CoolMethod methodObj = exprType.classMethodSearch(methodName);
        if (methodObj == null) {
            String msg = String.format(
                    "Class %s does not have a method named %s.",
                    exprType.getNameString(),
                    methodName.getValueString()
            );
            throw TypeCheckerException.factory(msg, camd);
        }

        ArrayList<CoolExpr> args = camd.getArguments();
        ArrayList<CoolFormal> params = methodObj.getParameters().getParameters();
        for (CoolExpr arg : args) {
            for (CoolFormal cf : params) {
                arg.accept(this);
                CoolClass providedType = arg.getComputedType();
                CoolClass expectedType = current.getSymbolType(cf.getType());
                if (!(providedType.equalOrSubrelation(expectedType))) {
                    String msg = String.format(
                            "In method call %s, argument %s of type %s does not match expected type %s",
                            camd,
                            ce,
                            providedType,
                            expectedType
                    );
                    throw TypeCheckerException.factory(msg, camd);
                }
            }
        }

        CoolClass resultType = current.getSymbolType(methodObj.getReturnType());
        camd.setComputedType(resultType);
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        SymbolTable current = ca.getSymbols();
        CoolClass declaredType = current.getSymbolType(ca.getTypeName());

        CoolExpr value = ca.getInitExpression();
        if (value != null) {
            value.accept(this);

            CoolClass computedValueType = value.getComputedType();

            if (!(declaredType.equalOrSubrelation(computedValueType))) {
                String msg = String.format(
                        "Value assigned to attribute %s does not match declared type %s",
                        computedValueType.getName(),
                        declaredType.getName()
                );
                throw TypeCheckerException.factory(msg, ca);
            }

            ca.setComputedType(computedValueType);
        } else {
            ca.setComputedType(declaredType);
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

        cas.setComputedType(computedType);
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

        LinkedList<CoolClass> stack = (
                (LinkedList<CoolClass>) cca.getBranches()
                        .stream()
                        .map(x -> x.getComputedType())
                        .toList()
        );

        while (stack.size() != 1) {
            CoolClass cc1 = stack.pop();
            CoolClass cc2 = stack.pop();
            CoolClass result = CoolClass.leastCommonAncestor(cc1, cc2);
            stack.push(result);
        }

        cca.setComputedType(stack.getFirst());
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

        cf.setComputedType(target);
    }

    @Override
    public void visitCoolIf(CoolIf cif) {
        SymbolTable current = cif.getSymbols();
        CoolClass boolTarget = current.getSymbolType(new CoolIdentifier("Bool"));
        CoolExpr pred = cif.getGuard();
        pred.accept(this);

        if (!(pred.getComputedType().equals(boolTarget))) {
            String msg = String.format("Predicate of an if statement must be a bool");
            throw TypeCheckerException.factory(msg, cif);
        }

        CoolExpr thenExpr = cif.getConsequent();
        thenExpr.accept(this);
        CoolExpr elseExpr = cif.getAlternative();
        elseExpr.accept(this);

        // the type of an if statement is the least upper bound type between the consequent
        // and alternative. throw an exception if they're not
        CoolClass result = CoolClass.leastCommonAncestor(thenExpr.getComputedType(), elseExpr.getComputedType());
        if (result == null) {
            String msg = String.format(
                    "Type mismatch between consequent and alternative, %s, %s.",
                    thenExpr.getComputedType(),
                    elseExpr.getComputedType()
            );
            throw TypeCheckerException.factory(msg, cif);

        } else {
            cif.setComputedType(result);
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

        cm.setComputedType(returnType);
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {
        SymbolTable current = cmd.getSymbols();
        CoolIdentifier methodName = cmd.getIdentifier();
        CoolClass targetClass = current.getSymbolType(new CoolIdentifier("SELF_TYPE"));

        // check to see if the method exists, this style of dispatch is shorthand for `self.ID(ARG1, ARG2, ..., ARGN)`
        CoolMethod method = targetClass.classMethodSearch(methodName);
        if (method == null) {
            String msg = String.format("Method %s does not exist on class %s!", method.getNameString(), targetClass.getNameString());
            throw TypeCheckerException.factory(msg, cmd);
        }

        // check to see if the # of arguments equals the # of parameters
        ArrayList<CoolExpr> args = cmd.getArguments();
        if (args.size() != method.getParameters().getParameters().size()) {
            String msg = String.format(
                    "Method call %s does not have the right number of arguments, received %s expected %s",
                    cmd,
                    args.size(),
                    method.getParameters().getParameters().size()
            );
            throw TypeCheckerException.factory(msg, cmd);
        }

        // check that each arg matches the type of its corresponding parameter, i.e. args must match positionally
        // according to type.
        for (CoolExpr arg : args) {
            for (CoolFormal param : method.getParameters().getParameters()) {
                arg.accept(this);
                param.accept(this);
                CoolClass argType = arg.getComputedType();
                CoolClass paramType = arg.getComputedType();
                if (!(argType.equals(paramType))) {
                    String msg = String.format(
                            "Argument %s with type %s does not match parameter %s with type %s in call to method %s.%s",
                            arg,
                            argType.getNameString(),
                            param,
                            paramType.getNameString(),
                            targetClass.getNameString(),
                            methodName.getValueString()
                    );
                    throw TypeCheckerException.factory(msg, cmd);
                }
            }
        }
        CoolClass returnType = current.getSymbolType(method.getReturnType());
        cmd.setComputedType(returnType);
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
        SymbolTable current = cuo.getSymbols();
        CoolUnaryOp.OPERATOR op = cuo.getOp();

        switch (op) {
            case NOT: {
                CoolClass result = current.getSymbolType(new CoolIdentifier("Bool"));
                cuo.setComputedType(result);
                break;
            }
            case COMPLEMENT: {
                CoolClass result = current.getSymbolType(new CoolIdentifier("Int"));
                cuo.setComputedType(result);
                break;
            }
            default: {
                String msg = String.format("Expression %s has invalid operator type %s", cuo, op);
                throw TypeCheckerException.factory(msg, cuo);
            }
        }
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        SymbolTable current = cw.getSymbols();
        CoolExpr pred = cw.getPredicate();
        CoolExpr body = cw.getBody();

        pred.accept(this);
        CoolClass predType = pred.getComputedType();
        CoolClass requiredType = current.getSymbolType(new CoolIdentifier("Bool"));
        if (!(predType.equals(requiredType))) {
            String msg = String.format("Predicate of a while loop must have static type Bool");
            throw TypeCheckerException.factory(msg, cw);
        }

        body.accept(this);
        CoolClass bodyType = body.getComputedType();

        cw.setComputedType(bodyType);
    }
}
