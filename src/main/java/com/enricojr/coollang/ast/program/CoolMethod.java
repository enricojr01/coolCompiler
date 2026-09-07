package com.enricojr.coollang.ast.program;

import java.util.ArrayList;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.CoolExpr;

public class CoolMethod extends CoolBaseNode {
    private CoolIdentifier name;
    private CoolIdentifier returnType;
    private CoolParamList parameters;
    private ArrayList<CoolExpr> expressions;

    public CoolMethod() {}

    public CoolIdentifier getName() {
        return name;
    }

    public String getNameString() {
        return this.name.getValue().toString();
    }

    public void setName(CoolIdentifier name) {
        this.name = name;
    }

    public ArrayList<CoolExpr> getExpressions() {
        return expressions;
    }

    public void setExpressions(ArrayList<CoolExpr> expressions) {
        this.expressions = expressions;
    }

    public CoolIdentifier getReturnType() {
        return returnType;
    }

    public String getReturnTypeString() {
        return this.returnType.getValue().toString();
    }

    public void setReturnType(CoolIdentifier returnType) {
        this.returnType = returnType;
    }

    public CoolParamList getParameters() {
        return parameters;
    }

    public void setParameters(CoolParamList parameters) {
        this.parameters = parameters;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("(CoolMethod: %s -> %s)", this.name, this.returnType));
        return sb.toString();
    }

    public void accept(AstVisitor t) {
        t.visitCoolMethod(this);
    }
}
