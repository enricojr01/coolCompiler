package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.semantic.exceptions.TypeCheckerException;
import com.enricojr.coollang.semantic.symboltable.SymbolTable;

import java.util.ArrayList;
import java.util.LinkedList;

public class TypeChecker implements AstVisitor {
    private int indent = 0;
    private final int offset = 2;
    private String space = " ";

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        // method dispatch needs to check a few things
        // first, that the left and right hand sides of the @ both eval to the
        // types that conform to one another (equal or subrelated)
        // second, that the # of arguments matches the number of parameters,
        // third, that the type of each argument matches its corresponding parameter's type.
        CoolExpr lhs = camd.getLhs();
        CoolIdentifier rhs = camd.getClassName();
        CoolClass lhsType = lhs.getComputedType();
        CoolClass rhsType = rhs.getComputedType();

        if (!(CoolClass.equalOrSubrelation(lhsType, rhsType))) {
            String msg = String.format(
                    "In method dispatch %s, the type of left hand side expression %s " +
                            "does not match the class specified on the right hand side %s",
                    camd,
                    lhsType,
                    rhsType
            );
            throw TypeCheckerException.factory(msg, camd);
        }

        CoolIdentifier methodName = camd.getMethodName();
        CoolMethod methodObj = rhsType.classMethodSearch(methodName);

        ArrayList<CoolExpr> args = camd.getArguments();
        ArrayList<CoolFormal> params = methodObj.getParameters().getParameters();

        if (args.size() != params.size()) {
            String msg = String.format(
                    "In method dispatch %s, the number of arguments passed in %s " +
                            "does not match the number of parameters in the signature %s",
                    camd,
                    args.size(),
                    params.size()
            );
            throw TypeCheckerException.factory(msg, camd);
        }

        for (int i = 0; i < args.size(); i++) {
            CoolExpr arg = args.get(i);
            CoolFormal param = params.get(i);

            this.indent += offset;
            arg.accept(this);
            this.indent -= offset;

            CoolClass argType = arg.getComputedType();
            CoolClass paramType = param.getComputedType();

            if (!CoolClass.equalOrSubrelation(argType, paramType)) {
                String msg = String.format(
                        "In method dispatch %s, the type of argument #%s (%s) does " +
                                "not match the type of its corresponding parameter (%s)",
                        camd,
                        i,
                        argType,
                        paramType
                );
                throw TypeCheckerException.factory(msg, camd);
            }
        }
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        // NOTE: verify that the attribute init expression is of the same
        // type as in the declaration.
        System.out.println(this.space.repeat(this.indent) + ca);

        this.indent += offset;
        if (ca.getInitExpression() != null) {
            CoolExpr init = ca.getInitExpression();
            init.accept(this);

            CoolClass declaredType = ca.getComputedType();
            CoolClass initType = init.getComputedType();

            if (!(CoolClass.equalOrSubrelation(initType, declaredType))) {
                String msg = String.format(
                        "Attribute %s initialization does not match its declared type (declared: %s, received: %s)",
                        ca,
                        declaredType,
                        initType
                );
                throw TypeCheckerException.factory(msg, ca);
            }
        }
        // its not that anything needs to be done here I just need to traverse all the way through the tree
        // to type check everything
        this.indent -= offset;

    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        // NOTE: need to check that the assignment expression type matches the type of the variable
        System.out.println(this.space.repeat(this.indent) + cas);
        SymbolTable current = cas.getSymbols();

        CoolIdentifier varName = cas.getName();
        CoolClass varType = current.getSymbolType(varName);

        CoolExpr assign = cas.getExpression();

        this.indent += offset;
        assign.accept(this);
        this.indent -= offset;

        CoolClass assignType = assign.getComputedType();

        if (!(CoolClass.equalOrSubrelation(assignType, varType))) {
            String msg = String.format(
                    "Type of assignment expression %s does not match declared type of variable %s (%s)",
                    assignType,
                    varName.getValueString(),
                    varType
            );

            throw TypeCheckerException.factory(msg, cas);
        }
    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        System.out.println(this.space.repeat(this.indent) + cbo);
        SymbolTable current = cbo.getSymbols();

        CoolExpr lhs = cbo.getLhs();
        CoolExpr rhs = cbo.getRhs();

        CoolClass lhsType = lhs.getComputedType();
        CoolClass rhsType = rhs.getComputedType();

        this.indent += offset;
        lhs.accept(this);
        rhs.accept(this);
        this.indent -= offset;

        CoolBinaryOp.OPERATOR op = cbo.getOp();
        switch(op) {
            case ADD, MUL, DIV, SUB, GT, GTE, LT, LTE: {
                CoolClass expected = current.getSymbolType(new CoolIdentifier("Int"));

                if (!(lhsType.equals(expected)) || !(rhsType.equals(expected))) {
                    String msg = String.format(
                            "In binary operation %s, both left (%s) and right hand side (%s) must be of type Int",
                            cbo,
                            lhsType,
                            rhsType
                    );
                    throw TypeCheckerException.factory(msg, cbo);
                }
                break;
            }
            case EQ: {
                if (!(CoolClass.equalOrSubrelation(lhsType, rhsType))) {
                    String msg = String.format(
                            "In binary operation %s, left (%s) and right hand sides (%s) must be equal or subrelated",
                            cbo,
                            lhsType,
                            rhsType
                    );
                    throw TypeCheckerException.factory(msg, cbo);
                }
                break;
            }
            default: {
                String msg = String.format("Binary operation %s uses unknown operator %s. This shouldn't happen.");
                throw TypeCheckerException.factory(msg, cbo);
            }
        }
    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        System.out.println(this.space.repeat(this.indent) + cb);

        this.indent += offset;
        for (CoolExpr ce : cb.getExpressions()) {
            ce.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolCase(CoolCase cca) {
        System.out.println(this.space.repeat(this.indent) + cca);

        CoolExpr expr0 = cca.getPredicate();

        for (CoolCaseBranch ccb : cca.getBranches())  {
            ccb.accept(this);
        }

        // the static type of the case expression is the LCA of all the branches
        LinkedList<CoolClass> stack = new LinkedList<>(
                cca.getBranches()
                        .stream()
                        .map(CoolExpr::getComputedType)
                        .toList()
        );

        while (stack.size() != 1) {
            CoolClass cc1 = stack.pop();
            CoolClass cc2 = stack.pop();
            CoolClass lca = CoolClass.leastCommonAncestor(cc1, cc2);
            stack.push(lca);
        }

        cca.setComputedType(stack.getFirst());
    }

    @Override
    public void visitCoolCaseBranch(CoolCaseBranch ccb) {
        System.out.println(this.space.repeat(this.indent) + ccb);

        this.indent += offset;
        CoolExpr ce = ccb.getExpression();
        ce.accept(this);
        this.indent -= offset;
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

        CoolClass classObj = cdmd.getClassName().getComputedType();
        CoolIdentifier methodName = cdmd.getMethodName();
        CoolMethod methodObj = classObj.classMethodSearch(methodName);

        ArrayList<CoolExpr> args = cdmd.getArguments();
        ArrayList<CoolFormal> params = methodObj.getParameters().getParameters();

        if (args.size() != params.size()) {
            String msg = String.format(
                    "In method dispatch %s, the number of arguments passed in (%s) " +
                    "does not match the number of parameters in the method signature (%s)",
                    cdmd,
                    args.size(),
                    params.size()
            );
            throw TypeCheckerException.factory(msg, cdmd);
        }

        for (int i = 0; i < args.size(); i++) {
            CoolExpr arg = args.get(i);
            CoolFormal param = params.get(i);

            this.indent += offset;
            arg.accept(this);
            this.indent -= offset;

            CoolClass argType = arg.getComputedType();
            CoolClass paramType = param.getComputedType();

            if (!(CoolClass.equalOrSubrelation(argType, paramType))) {
                String msg = String.format(
                        "In method dispatch %s, the type of argument #%s (%s) does " +
                                "not match the type of its corresponding parameter (%s)",
                        cdmd,
                        i,
                        argType,
                        paramType
                );
                throw TypeCheckerException.factory(msg, cdmd);
            }
        }
    }

    @Override
    public void visitCoolExpr(CoolExpr ce) {
        // nothing to do here
    }

    @Override
    public void visitCoolFormal(CoolFormal cf) {
        // nothing to do here
    }

    @Override
    public void visitCoolIf(CoolIf cif) {
        System.out.println(this.space.repeat(this.indent) + cif);
        SymbolTable current = cif.getSymbols();

        CoolExpr guard = cif.getGuard();
        guard.accept(this);
        CoolClass expectedGuardType = current.getSymbolType(new CoolIdentifier("Bool"));
        CoolClass actualGuardType=  guard.getComputedType();

        if (!(actualGuardType.equals(expectedGuardType))) {
            String msg = String.format("In if-expression %s, guard clause must be of type Bool", cif);
            throw TypeCheckerException.factory(msg, cif);
        }
    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
        System.out.println(this.space.repeat(this.indent) + ci);
        SymbolTable current = ci.getSymbols();

        CoolIdentifier newName = ci.getIdentifier();
        CoolClass newType = ci.getComputedType();
        CoolClass newObj = current.getSymbolType(newName);

        // TODO: circle back around to this - I need some way of determining, outside of these two constructs
        // if the SELF_TYPE returned by SymbolType is the _correct_ SELF_TYPE, i.e. the classs that contains the
        // instantiation. I'm certain that the symbol table lookup will give me the right one, but how do I verify that
        // without something on the outside looking in?
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        System.out.println(this.space.repeat(this.indent) + civ);
        CoolExpr ce = civ.getExpression();
        ce.accept(this);
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        System.out.println(this.space.repeat(this.indent) + cl);
        SymbolTable current = cl.getSymbols();

        this.indent += offset;
        ArrayList<CoolAttribute> attributes = cl.getAttributes();
        for (CoolAttribute ca : attributes) {
            if (ca.getInitExpression() != null) {
                CoolExpr initExpr = ca.getInitExpression();
                initExpr.accept(this);

                CoolIdentifier declaredType = ca.getTypeName();
                CoolClass concreteType = current.getSymbolType(declaredType);
                CoolClass initType = initExpr.getComputedType();

                if (!(CoolClass.equalOrSubrelation(concreteType, initType))) {
                    String msg = String.format(
                            "Initialization of variable %s evaluates to incorrect type. Expected: %s, actual: %s",
                            ca.getIdentifier().getValueString(),
                            ca.getTypeName().getValueString(),
                            initType.getNameString()
                    );
                    throw TypeCheckerException.factory(msg, cl);
                }
            }
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        // NOTE: check to see that the last expression in the method body is the same as the
        // return type declared in its signature.
        // NOTE: also sanity check every expression in the program
        System.out.println(this.space.repeat(this.indent) + cm);
        CoolClass declaredType = cm.getComputedType();

        CoolExpr lastExpr = cm.getBody().getLast();
        CoolClass lastExprType = lastExpr.getComputedType();

        this.indent += offset;
        for (CoolExpr ce : cm.getBody()) {
            ce.accept(this);
        }
        this.indent -= offset;

        if (!(CoolClass.equalOrSubrelation(lastExprType, declaredType))) {
            String msg = String.format(
                    "Method %s final expression type does not match its declared return type. (declared: %s, received: %s)",
                    cm,
                    declaredType,
                    lastExprType
            );

            throw TypeCheckerException.factory(msg, cm);
        }
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {
        System.out.println(this.space.repeat(this.indent) + cmd);
        SymbolTable current = cmd.getSymbols();

        CoolIdentifier methodName = cmd.getIdentifier();
        // method dispatch is basically a shorthand for SELF_TYPE.<method_name>(<args>) i.e. the class name
        // is assumed to be SELF_TYPE
        CoolClass classObj = current.getSymbolType(new CoolIdentifier("SELF_TYPE"));
        CoolMethod methodObj = classObj.classMethodSearch(methodName);

        ArrayList<CoolExpr> args = cmd.getArguments();
        ArrayList<CoolFormal> params = methodObj.getParameters().getParameters();

        if (args.size() != params.size()) {
            String msg = String.format(
                    "In method dispatch %s, number of arguments provided (%s) does not match the number of parameters in its declaration (%s).",
                    cmd,
                    args.size(),
                    params.size()
            );
            throw TypeCheckerException.factory(msg, cmd);
        }
    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {
        // nothing to do here
    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {
        System.out.println(this.space.repeat(this.indent) + cpe);

        CoolExpr expr = cpe.getExpression();

        this.indent += offset;
        expr.accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
        cp.getRoot().accept(this);
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        System.out.println(this.space.repeat(this.indent) + cuo);
        SymbolTable current = cuo.getSymbols();
        CoolExpr expr = cuo.getExpression();
        CoolUnaryOp.OPERATOR op = cuo.getOp();

        this.indent += offset;
        expr.accept(this);
        this.indent -= offset;

        CoolClass exprType = expr.getComputedType();

        switch(op) {
            case NOT: {
                CoolClass expectedType = current.getSymbolType(new CoolIdentifier("Bool"));
                if (!(exprType.equals(expectedType))) {
                    String msg = String.format(
                            "In unary expression %s, expression must be of type Bool. (received: %s)",
                            cuo,
                            exprType
                    );
                    throw TypeCheckerException.factory(msg, cuo);
                }
                break;
            }
            case COMPLEMENT: {
                CoolClass expectedType = current.getSymbolType(new CoolIdentifier("Int"));
                if (!(exprType.equals(expectedType))) {
                    String msg = String.format(
                            "In unary expression %s, expression must be of type Int. (received %s)",
                            cuo,
                            exprType
                    );
                    throw TypeCheckerException.factory(msg, cuo);
                }
                break;
            }
            default: {
                String msg = String.format("Unary expression %s uses unknown operator %s", cuo, op);
                throw TypeCheckerException.factory(msg, cuo);
            }
        }
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        System.out.println(this.space.repeat(this.indent) + cw);
        SymbolTable current = cw.getSymbols();

        CoolExpr guard = cw.getPredicate();
        guard.accept(this);

        CoolExpr body = cw.getBody();
        body.accept(this);

        CoolClass guardType = guard.getComputedType();
        CoolClass expectedType = current.getSymbolType(new CoolIdentifier("Bool"));

        if (!(guardType.equals(expectedType))) {
            String msg = String.format(
                    "In while statement %s, predicate must be of type Bool (received: %s).",
                    cw,
                    guardType
            );
            throw TypeCheckerException.factory(msg, cw);
        }
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
