package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.semantic.exceptions.TypeCheckerException;
import com.enricojr.coollang.semantic.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.LinkedList;

public class TypeCheckerOld implements AstVisitor {
    private int indent = 0;
    private final String space = " ";
    private final int offset = 2;

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        System.out.println(this.space.repeat(this.indent) + camd);
        SymbolTable current = camd.getSymbols();

        this.indent += offset;
        CoolExpr ce = camd.getLhs();
        ce.accept(this);
        this.indent -= offset;

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
        System.out.println(this.space.repeat(this.indent) + ca);
        SymbolTable current = ca.getSymbols();
        CoolClass declaredType = current.getSymbolType(ca.getTypeName());

        CoolExpr value = ca.getInitExpression();
        if (value != null) {
            this.indent += offset;
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
            this.indent -= offset;
        } else {
            ca.setComputedType(declaredType);
        }
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        System.out.println(this.space.repeat(this.indent) + cas);

        SymbolTable current = cas.getSymbols();
        CoolIdentifier variable = cas.getName();
        CoolClass declaredType = current.getSymbolType(variable);

        CoolExpr assignment = cas.getExpression();

        this.indent += offset;
        assignment.accept(this);
        this.indent -= offset;

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
        System.out.println(this.space.repeat(this.indent) + cbo);

        SymbolTable current = cbo.getSymbols();
        CoolBinaryOp.OPERATOR op = cbo.getOp();

        this.indent += 1;
        CoolExpr lhs = cbo.getLhs();
        lhs.accept(this);

        CoolExpr rhs = cbo.getRhs();
        rhs.accept(this);
        this.indent -= 1;

        // add, mul, div, sub all take ints only
        // LT, LTE, GT, GTE all take only bool
        switch (op) {
            case ADD:
            case SUB:
            case MUL:
            case DIV: {
                CoolClass expected = current.getSymbolType(new CoolIdentifier("Int"));

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
            case EQ: {
                if (!(CoolClass.equalOrSubrelation(lhs.getComputedType(), rhs.getComputedType()))) {
                    String msg = String.format(
                            "Left hand side %s and right hand side of %s operation must conform or be equal",
                            lhs.getComputedType(),
                            rhs.getComputedType(),
                            op
                    );
                    throw TypeCheckerException.factory(msg, cbo);
                }
                CoolClass computedType = current.getSymbolType(new CoolIdentifier("Bool"));
                cbo.setComputedType(computedType);
                break;
            }
            case GT:
            case GTE:
            case LT:
            case LTE: {
                CoolClass expected = current.getSymbolType(new CoolIdentifier("Int"));

                if (!(rhs.getComputedType().equalOrSubrelation(expected)) ||
                        !(lhs.getComputedType().equalOrSubrelation(expected))) {
                    String msg = String.format(
                            "Left hand side %s and/or right hand side %s of %s operation does not match expected value %s",
                            lhs.getComputedType(),
                            rhs.getComputedType(),
                            op,
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
        System.out.println(this.space.repeat(this.indent) + cb);

        this.indent += offset;
        for (CoolExpr ce : cb.getExpressions()) {
            ce.accept(this);
        }
        this.indent -= offset;

        CoolExpr last = cb.getExpressions().getLast();
        cb.setComputedType(last.getComputedType());
    }

    @Override
    public void visitCoolCase(CoolCase cca) {
        System.out.println(this.space.repeat(this.indent) + cca);

        SymbolTable current = cca.getSymbols();
        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.accept(this);
        }

        LinkedList<CoolClass> stack = new LinkedList<>(
                cca.getBranches()
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
        System.out.println(this.space.repeat(this.indent) + ccb);

        this.indent += offset;
        CoolExpr expr = ccb.getExpression();
        expr.accept(this);
        this.indent -= offset;

        CoolClass result = expr.getComputedType();
        ccb.setComputedType(result);
    }

    @Override
    public void visitCoolClass(CoolClass cc) {
        System.out.println(this.space.repeat(this.indent) + cc);

        this.indent += offset;
        for (CoolAttribute ca : cc.getAttributes()) {
            ca.accept(this);
        }

        for (CoolMethod cm : cc.getMethods()) {
            cm.accept(this);
        }

        for (CoolClass child : cc.getChildren()) {
            child.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolDotMethodDispatch(CoolDotMethodDispatch cdmd) {
        System.out.println(this.space.repeat(this.indent) + cdmd);
        SymbolTable current = cdmd.getSymbols();

        this.indent += offset;
        CoolExpr className = cdmd.getClassName();
        className.accept(this);
        this.indent -= offset;

        CoolClass concreteClass = null;

        if (className instanceof CoolIdentifier) {
            CoolIdentifier ci = (CoolIdentifier) className;
            concreteClass = current.getSymbolType(ci);
        } else {
            concreteClass = className.getComputedType();
        }

        if (concreteClass == null) {
            String msg = String.format("" +
                    "Static dot method dispatch expression evalutates to invalid type: %s",
                    className.getComputedType()
            );
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
        for (int i = 0; i < args.size(); i++) {
            CoolExpr ce = args.get(i);
            CoolFormal cf = params.get(i);

            this.indent += offset;
            ce.accept(this);
            this.indent -= offset;

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

        // NOTE: given `(new LambdaListRef).reset()` where reset returns SELF_TYPE,
        // SELF_TYPE should be of type LambdaListRef, not of Main (the calling class)
        // maybe consider rewriting SELF_TYPE at the time its type is computed?
        CoolClass computedType = current.getSymbolType(methodObj.getReturnType());
        cdmd.setComputedType(computedType);
    }

    @Override
    public void visitCoolExpr(CoolExpr ce) {
        ce.accept(this);
    }

    @Override
    public void visitCoolFormal(CoolFormal cf) {
        System.out.println(this.space.repeat(this.indent) + cf);
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
        System.out.println(this.space.repeat(this.indent) + cif);
        SymbolTable current = cif.getSymbols();
        CoolClass boolTarget = current.getSymbolType(new CoolIdentifier("Bool"));

        this.indent += offset;
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
        this.indent -= offset;

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

    // NOTE: check newA2I
    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
        System.out.println(this.space.repeat(this.indent) + ci);
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
        System.out.println(this.space.repeat(this.indent) + civ);

        SymbolTable current = civ.getSymbols();
        CoolClass boolType = current.getSymbolType(new CoolIdentifier("Bool"));
        civ.setComputedType(boolType);
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        System.out.println(this.space.repeat(this.indent) + cl);

        // the type of any let expression is the type of the last expression in its body.
        this.indent += offset;
        for (CoolAttribute ca : cl.getAttributes()) {
            ca.accept(this);
        }
        CoolExpr body = cl.getExpression();
        body.accept(this);
        this.indent -= offset;

        cl.setComputedType(body.getComputedType());
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        // goal: make sure that the method returns a value that matches its type
        // the type of the method is the type of the final expression in its body
        System.out.println(this.space.repeat(this.indent) + cm);
        SymbolTable current = cm.getSymbols();
        CoolClass returnType = current.getMethodType(cm.getName());

        this.indent += offset;
        for (CoolExpr ce : cm.getBody()) {
            ce.accept(this);
        }
        this.indent -= offset;

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
        System.out.println(this.space.repeat(this.indent) + cmd);
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

        for (int i = 0; i < args.size(); i++) {
            CoolExpr arg = args.get(i);
            CoolFormal param = method.getParameters().getParameters().get(i);

            this.indent += offset;
            arg.accept(this);
            param.accept(this);
            this.indent -= offset;

            CoolClass argType = arg.getComputedType();
            CoolClass paramType = param.getComputedType();

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

        // check that each arg matches the type of its corresponding parameter, i.e. args must match positionally
        // according to type.
        CoolClass returnType = current.getSymbolType(method.getReturnType());
        cmd.setComputedType(returnType);
    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {

    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {
        System.out.println(this.space.repeat(this.indent) + cpe);
        CoolExpr expr = cpe.getExpression();

        this.indent += offset;
        expr.accept(this);
        this.indent -= offset;

        CoolClass result = expr.getComputedType();
        cpe.setComputedType(result);
    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        cp.getRoot().accept(this);
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        System.out.println(this.space.repeat(this.indent) + cuo);
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
        System.out.println(this.space.repeat(this.indent) + cw);

        SymbolTable current = cw.getSymbols();
        CoolExpr pred = cw.getPredicate();
        CoolExpr body = cw.getBody();

        this.indent += offset;
        pred.accept(this);
        CoolClass predType = pred.getComputedType();
        CoolClass requiredType = current.getSymbolType(new CoolIdentifier("Bool"));
        if (!(predType.equals(requiredType))) {
            String msg = String.format("Predicate of a while loop must have static type Bool");
            throw TypeCheckerException.factory(msg, cw);
        }

        body.accept(this);
        CoolClass bodyType = body.getComputedType();
        this.indent -= offset;

        cw.setComputedType(bodyType);
    }

    @Override
    public void visitCoolString(CoolString cs) {
        System.out.println(this.space.repeat(this.indent) + cs);
        SymbolTable current = cs.getSymbols();
        cs.setComputedType(current.getSymbolType(new CoolIdentifier("String")));
    }

    @Override
    public void visitCoolBool(CoolBool cb) {
        System.out.println(this.space.repeat(this.indent) + cb);
        SymbolTable current = cb.getSymbols();
        cb.setComputedType(current.getSymbolType(new CoolIdentifier("Bool")));
    }

    @Override
    public void visitCoolInteger(CoolInteger ci) {
        System.out.println(this.space.repeat(this.indent) + ci);
        SymbolTable current = ci.getSymbols();
        ci.setComputedType(current.getSymbolType(new CoolIdentifier("Int")));
    }

    @Override
    public void visitCoolSelf(CoolSelf cs) {
        System.out.println(this.space.repeat(this.indent) + cs);
        SymbolTable current = cs.getSymbols();
        cs.setComputedType(current.getSymbolType(new CoolIdentifier("SELF_TYPE")));
    }

    @Override
    public void visitCoolIdentifier(CoolIdentifier ci) {
        System.out.println(this.space.repeat(this.indent) + ci);
        SymbolTable current = ci.getSymbols();
        CoolClass declaredType = current.getSymbolType(ci);
        if (declaredType == null) {
            String msg = String.format("Identifier %s not found in symbol table.", ci.getValueString());
            throw TypeCheckerException.factory(msg, ci);
        } else {
            ci.setComputedType(declaredType);
        }
    }
}
