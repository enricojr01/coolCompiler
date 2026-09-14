package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.*;
import com.enricojr.coollang.ast.expressions.*;
import com.enricojr.coollang.ast.program.*;
import com.enricojr.coollang.semantic.exceptions.TypeCheckerException;
import com.enricojr.coollang.semantic.symboltable.SymbolTable;

import java.util.LinkedList;

public class TypeSetter implements AstVisitor {
    private int indent = 0;
    private final int offset = 2;
    private final String space = " ";

    @Override
    public void visitCoolAtMethodDispatch(CoolAtMethodDispatch camd) {
        System.out.println(this.space.repeat(this.indent) + camd);
        SymbolTable current = camd.getSymbols();

        this.indent += offset;
        CoolExpr lhs = camd.getLhs();
        lhs.accept(this);

        // NOTE: can't do "accept" here on methodName because its on another class
        // and we need to resolve that class first
        CoolIdentifier methodName = camd.getMethodName();
        CoolIdentifier className = camd.getClassName();

        // So we resolve the classname here and it gets added as the computedType
        className.accept(this);

        // then we can use it to get the method object
        CoolClass classObj = className.getComputedType();
        CoolMethod methodObj = classObj.classMethodSearch(methodName);

        for (CoolExpr ce : camd.getArguments()) {
            ce.accept(this);
        }
        this.indent -= offset;

        // then we can get the return type and assign it here.
        CoolIdentifier methodReturnType = methodObj.getReturnType();
        CoolClass concreteMethodReturnType = current.getSymbolType(methodReturnType);
        camd.setComputedType(concreteMethodReturnType);
    }

    @Override
    public void visitCoolAttribute(CoolAttribute ca) {
        // NOTE: the type of the attribute is its declared type
        System.out.println(this.space.repeat(this.indent) + ca);
        SymbolTable current = ca.getSymbols();

        CoolIdentifier declaredType = ca.getTypeName();
        CoolClass concreteDeclaredType = current.getSymbolType(declaredType);
        ca.setComputedType(concreteDeclaredType);

        this.indent += offset;
        if (ca.getInitExpression() != null) {
            CoolExpr init = ca.getInitExpression();
            init.accept(this);
        }
        this.indent -= offset;
    }

    @Override
    public void visitCoolAssign(CoolAssign cas) {
        System.out.println(this.space.repeat(this.indent) + cas);

        this.indent += offset;
        CoolExpr expr = cas.getExpression();
        expr.accept(this);
        this.indent -= offset;

        CoolClass resultType = expr.getComputedType();
        cas.setComputedType(resultType);
    }

    @Override
    public void visitCoolBinaryOp(CoolBinaryOp cbo) {
        System.out.println(this.space.repeat(this.indent) + cbo);
        CoolExpr lhs = cbo.getLhs();
        CoolExpr rhs = cbo.getRhs();

        this.indent += offset;
        lhs.accept(this);
        rhs.accept(this);
        this.indent -= offset;

    }

    @Override
    public void visitCoolBlock(CoolBlock cb) {
        // NOTE: The type of a block is the value of its final expression;
        System.out.println(this.space.repeat(this.indent) + cb);

        this.indent += offset;
        for (CoolExpr ce : cb.getExpressions()) {
            ce.accept(this);
        }
        this.indent -= offset;

        // block might be EMPTY in which case what type is it? void?
        // I don't have a representation for a void type.
        CoolExpr last = cb.getExpressions().getLast();
        CoolClass lastExprType = last.getComputedType();

        cb.setComputedType(lastExprType);
    }

    @Override
    public void visitCoolCase(CoolCase cca) {
        System.out.println(this.space.repeat(this.indent) + cca);

        this.indent += 1;
        for (CoolCaseBranch ccb : cca.getBranches()) {
            ccb.accept(this);
        }
        this.indent -= 1;

        LinkedList<CoolClass> stack = new LinkedList<>(
                cca.getBranches()
                        .stream()
                        .map(x -> x.getComputedType())
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
        CoolExpr expr = ccb.getExpression();
        expr.accept(this);
        this.indent -= offset;

        CoolClass exprType = expr.getComputedType();
        ccb.setComputedType(exprType);
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

        CoolClass concreteClass = className.getComputedType();

        CoolIdentifier methodName = cdmd.getMethodName();
        CoolMethod method = concreteClass.classMethodSearch(methodName);
        if (method == null) {
            String msg = String.format("Class %s does not have method named %s.");
            throw TypeCheckerException.factory(msg, cdmd);
        }

        for (CoolExpr ce : cdmd.getArguments()) {
            ce.accept(this);
        }
        this.indent -= offset;

        CoolIdentifier methodReturnType = method.getReturnType();
        CoolClass computedType = current.getSymbolType(methodReturnType);
        cdmd.setComputedType(computedType);
    }

    @Override
    public void visitCoolExpr(CoolExpr ce) {
    }

    @Override
    public void visitCoolFormal(CoolFormal cf) {
        System.out.println(this.space.repeat(this.indent) + cf);
        SymbolTable current = cf.getSymbols();

        CoolIdentifier type = cf.getType();
        CoolClass concreteType = current.getSymbolType(type);

        cf.setComputedType(concreteType);;
    }

    @Override
    public void visitCoolIf(CoolIf cif) {
        System.out.println(this.space.repeat(this.indent) + cif);

        this.indent += offset;
        CoolExpr guard = cif.getGuard();
        guard.accept(this);

        CoolExpr consequent = cif.getConsequent();
        consequent.accept(this);

        CoolExpr alternative = cif.getAlternative();
        alternative.accept(this);
        this.indent -= offset;

        CoolClass conType = consequent.getComputedType();
        CoolClass altType = alternative.getComputedType();
        CoolClass resultType = CoolClass.leastCommonAncestor(conType, altType);

        cif.setComputedType(resultType);
    }

    @Override
    public void visitCoolInstantiate(CoolInstantiate ci) {
        System.out.println(this.space.repeat(this.indent) + ci);
        SymbolTable current = ci.getSymbols();

        CoolIdentifier newType = ci.getIdentifier();
        CoolClass concreteType = current.getSymbolType(newType);

        ci.setComputedType(concreteType);
    }

    @Override
    public void visitCoolIsVoid(CoolIsVoid civ) {
        System.out.println(this.space.repeat(this.indent) + civ);
        SymbolTable current = civ.getSymbols();

        // NOTE: in case I forget - we type the expression now, set the computed type of the isvoid expression to
        // bool, and then verify it later
        this.indent += offset;
        CoolExpr ce = civ.getExpression();
        ce.accept(this);
        this.indent -= offset;

        CoolClass boolType = current.getSymbolType(new CoolIdentifier("Bool"));
        civ.setComputedType(boolType);
    }

    @Override
    public void visitCoolLet(CoolLet cl) {
        System.out.println(this.space.repeat(this.indent) + cl);

        this.indent += offset;
        for (CoolAttribute ca : cl.getAttributes()) {
            ca.accept(this);
        }

        CoolExpr ce = cl.getExpression();
        ce.accept(this);
        this.indent -= offset;

        CoolClass resultType = ce.getComputedType();
        cl.setComputedType(resultType);
    }

    @Override
    public void visitCoolMethod(CoolMethod cm) {
        // NOTE: The return type of a method is its declared type
        // NOTE: if its type is SELF_TYPE, rewrite it to the class its in NOW because doing it
        //       later will cause SELF_TYPE to be incorrectly resolved.
        // NOTE: for all of these methods we will simply "stamp" the expressions with their types
        //       and check that they conform later.
        System.out.println(this.space.repeat(this.indent) + cm);
        SymbolTable current = cm.getSymbols();

        CoolIdentifier returnType = cm.getReturnType();
        CoolClass concreteReturnType = current.getSymbolType(returnType);

        this.indent += offset;
        for (CoolExpr ce : cm.getBody()) {
            ce.accept(this);
        }
        this.indent -= offset;

        cm.setComputedType(concreteReturnType);
    }

    @Override
    public void visitCoolMethodDispatch(CoolMethodDispatch cmd) {
        System.out.println(this.space.repeat(this.indent) + cmd);
        SymbolTable current = cmd.getSymbols();

        this.indent += offset;
        for (CoolExpr ce : cmd.getArguments()) {
            ce.accept(this);
        }
        this.indent -= offset;

        CoolIdentifier methodName = cmd.getIdentifier();
        CoolClass targetClass = current.getSymbolType(new CoolIdentifier("SELF_TYPE"));
        CoolMethod method = targetClass.classMethodSearch(methodName);
        CoolIdentifier methodReturnType = method.getReturnType();
        CoolClass computedReturnType = current.getSymbolType(methodReturnType);

        cmd.setComputedType(computedReturnType);
    }

    @Override
    public void visitCoolParamList(CoolParamList cpl) {
        // NOTE: doesn't really have a type
    }

    @Override
    public void visitCoolParenthesisExpr(CoolParenthesisExpr cpe) {
        System.out.println(this.space.repeat(this.indent) + cpe);

        this.indent += offset;
        CoolExpr expr = cpe.getExpression();
        expr.accept(this);
        this.indent -= offset;

        CoolClass exprType = expr.getComputedType();
        cpe.setComputedType(exprType);
    }

    @Override
    public void visitCoolProgram(CoolProgram cp) {
//        System.out.println(this.space.repeat(this.indent) + cp);
        CoolClass root = cp.getRoot();

        this.indent += offset;
        root.accept(this);
        this.indent -= offset;
    }

    @Override
    public void visitCoolUnaryOp(CoolUnaryOp cuo) {
        System.out.println(this.space.repeat(this.indent) + cuo);
        SymbolTable current = cuo.getSymbols();

        // It's not that the type of the expression matters, its that I need to traverse the tree
        // so that everything gets a type;
        this.indent += offset;
        CoolExpr expr = cuo.getExpression();
        expr.accept(this);
        this.indent -= offset;

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
                String msg = String.format("Unary expression %s has invalid operator %s", cuo, op);
                throw TypeCheckerException.factory(msg, cuo);
            }
        }
    }

    @Override
    public void visitCoolWhile(CoolWhile cw) {
        System.out.println(this.space.repeat(this.indent) + cw);

        this.indent += offset;
        CoolExpr pred = cw.getPredicate();
        pred.accept(this);

        CoolExpr body = cw.getBody();
        body.accept(this);
        this.indent -= offset;

        CoolClass bodyType = cw.getComputedType();
        cw.setComputedType(bodyType);
    }

    @Override
    public void visitCoolString(CoolString cs) {
        System.out.println(this.space.repeat(this.indent) + cs);
        SymbolTable current = cs.getSymbols();
        CoolClass targetType = current.getSymbolType(new CoolIdentifier("String"));
        cs.setComputedType(targetType);
    }

    @Override
    public void visitCoolBool(CoolBool cb) {
        System.out.println(this.space.repeat(this.indent) + cb);
        SymbolTable current = cb.getSymbols();
        CoolClass targetType = current.getSymbolType(new CoolIdentifier("Bool"));
        cb.setComputedType(targetType);
    }

    @Override
    public void visitCoolInteger(CoolInteger ci) {
        System.out.println(this.space.repeat(this.indent) + ci);
        SymbolTable current = ci.getSymbols();
        CoolClass targetType = current.getSymbolType(new CoolIdentifier("Int"));
        ci.setComputedType(targetType);
    }

    @Override
    public void visitCoolSelf(CoolSelf cs) {
        System.out.println(this.space.repeat(this.indent) + cs);
        SymbolTable current = cs.getSymbols();
        CoolClass targetType = current.getSymbolType(new CoolIdentifier("SELF_TYPE"));
        cs.setComputedType(targetType);
    }

    @Override
    public void visitCoolIdentifier(CoolIdentifier ci) {
        System.out.println(this.space.repeat(this.indent) + ci);
        SymbolTable current = ci.getSymbols();
        CoolClass symbolType = current.getSymbolType(ci);
        CoolClass methodType = current.getMethodType(ci);
        if (symbolType == null && methodType == null) {
            String msg = String.format("Identifier %s not found in symbol table.", ci.getValueString());
            throw TypeCheckerException.factory(msg, ci);
        } else if (symbolType != null && methodType == null){
            ci.setComputedType(symbolType);
        } else {
            ci.setComputedType(methodType);
        }
    }
}
