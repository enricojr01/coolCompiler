package com.enricojr.coollang.ast.constants;

public class CoolInteger extends CoolConstant {
    private int value; 

    public CoolInteger() {}

    public CoolInteger(int v) {
        this.value = v;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int v) {
        this.value = v;
    }

    public String toString() {
        return String.format("Integer - %s", this.value);
    }
}
