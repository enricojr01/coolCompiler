package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.ast.AstVisitor;

import java.util.ArrayList;
import java.util.Objects;

public class CoolParamList extends CoolBaseNode {
    private ArrayList<CoolFormal> parameters;

    public CoolParamList() {}

    public ArrayList<CoolFormal> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<CoolFormal> parameters) {
        this.parameters = parameters;
    }

    public String toString() {
        return String.format("(ParameterList - %s params)", this.parameters.size());
    }

    public void accept(AstVisitor t) {
        t.visitCoolParamList(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CoolParamList that = (CoolParamList) o;
        return Objects.equals(getParameters(), that.getParameters());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getParameters());
    }
}
