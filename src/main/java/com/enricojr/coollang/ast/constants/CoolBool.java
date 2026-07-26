package com.enricojr.coollang.ast.constants;

public class CoolBool extends CoolConstant {
    private boolean value;

    public CoolBool() {
        this.value = false;
    }

    public CoolBool(boolean v) {
        this.value = v;
    }

    public boolean getValue() {
        return this.value;
    }

    public void setValue(boolean b) {
        this.value = b;
    }

    public String toString() {
        return String.format("<Boolean - %s>", this.value);
    }
}
