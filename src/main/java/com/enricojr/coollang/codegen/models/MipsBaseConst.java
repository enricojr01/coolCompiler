package com.enricojr.coollang.codegen.models;

public class MipsBaseConst {
    private String label;
    private int tag;

    public MipsBaseConst(String label, int tag) {
        this.label = label;
        this.tag = tag;
    };

    public String getLabel() {
        return this.label;
    }

    public int getTag() {
        return this.tag;
    }

    public String toCode() {
        return "";
    }
}
