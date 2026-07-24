package com.enricojr.coollang.ast.program;

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
}
