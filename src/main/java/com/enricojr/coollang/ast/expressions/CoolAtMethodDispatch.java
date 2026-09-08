package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolAtMethodDispatch extends CoolExpr {
    private CoolExpr lhs; 
    private CoolIdentifier className;
    private CoolIdentifier methodName;
    private ArrayList<CoolExpr> arguments;

    public CoolAtMethodDispatch() {}

    public CoolIdentifier getClassName() {
        return className;
    }

    public void setClassName(CoolIdentifier className) {
        this.className = className;
    }

    public CoolExpr getLhs() {
        return lhs;
    }

    public void setLhs(CoolExpr lhs) {
        this.lhs = lhs;
    }

    public ArrayList<CoolExpr> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<CoolExpr> arguments) {
        this.arguments = arguments;
    }

    public String toString() {
        return String.format("<CoolAtMethodCall - %s>", this.className);
    }

    public CoolIdentifier getMethodName() {
        return methodName;
    }

    public void setMethodName(CoolIdentifier methodName) {
        this.methodName = methodName;
    }

    public void accept(AstVisitor t) {
        t.visitCoolAtMethodDispatch(this);
    }
}
