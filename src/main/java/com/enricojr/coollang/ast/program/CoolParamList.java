package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.ast.AstVisitor;

import java.util.ArrayList;

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
}
