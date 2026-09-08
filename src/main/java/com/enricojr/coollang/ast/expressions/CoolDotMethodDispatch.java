package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolDotMethodDispatch extends CoolExpr {
    private CoolExpr className;
    private CoolIdentifier methodName;
    private ArrayList<CoolExpr> arguments; 

    public CoolDotMethodDispatch() {}

    public CoolExpr getClassName() {
        return className;
    }

    public void setClassName(CoolExpr className) {
        this.className = className;
    }

    public CoolIdentifier getMethodName() {
        return methodName;
    }

    public void setMethodName(CoolIdentifier methodName) {
        this.methodName = methodName;
    }

    public ArrayList<CoolExpr> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<CoolExpr> arguments) {
        this.arguments = arguments;
    }

    public String toString() {
        return String.format("<CoolDotMethodCall - %s()>", this.methodName.getValue());
    }

    public void accept(AstVisitor t) {
        t.visitCoolDotMethodDispatch(this);
    }
}
