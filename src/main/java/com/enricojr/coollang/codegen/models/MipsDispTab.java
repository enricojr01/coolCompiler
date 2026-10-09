package com.enricojr.coollang.codegen.models;

import java.util.ArrayList;

public class MipsDispTab extends MipsBaseConst {
    private String label;
    private String[] methodNames;

    public MipsDispTab(String label, int size) {
        super(label, -1);
        this.label = label;
        this.methodNames = new String[size];
    }

    public String getLabel() {
        return this.label;
    }

    public void addMethodName(String methodName, int pos) {
        this.methodNames[pos] = methodName;
    }
}
