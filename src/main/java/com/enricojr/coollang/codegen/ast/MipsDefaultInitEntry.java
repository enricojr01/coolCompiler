package com.enricojr.coollang.codegen.ast;

public class MipsDefaultInitEntry {
    private int position;
    private String defaultValue;

    public MipsDefaultInitEntry(int pos, String defaultValue) {
        this.position = pos;
        this.defaultValue = defaultValue;
    }

    public int getPosition() {
        return this.position;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }
}
