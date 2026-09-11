package com.enricojr.coollang.ast.program;

import java.util.ArrayList;
import java.util.Objects;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.CoolExpr;

public class CoolMethod extends CoolBaseNode {
    private CoolIdentifier name;
    private CoolIdentifier returnType;
    private CoolParamList parameters;
    private ArrayList<CoolExpr> body = new ArrayList<>();
    private CoolClass computedType;

    public CoolMethod() {}

    public CoolIdentifier getName() {
        return name;
    }

    public String getNameString() {
        return this.name.getValue().toString();
    }

    public CoolClass getComputedType() {
        return computedType;
    }

    public void setComputedType(CoolClass computedType) {
        this.computedType = computedType;
    }

    public void setName(CoolIdentifier name) {
        this.name = name;
    }

    public ArrayList<CoolExpr> getBody() {
        return body;
    }

    public void setBody(ArrayList<CoolExpr> body) {
        this.body = body;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CoolMethod that = (CoolMethod) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getReturnType(), that.getReturnType()) &&
                Objects.equals(getParameters(), that.getParameters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getReturnType(), getParameters());
    }
}
