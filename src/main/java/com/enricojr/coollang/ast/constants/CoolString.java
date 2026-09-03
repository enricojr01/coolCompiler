package com.enricojr.coollang.ast.constants;

public class CoolString extends CoolLiteral {
    private String value;

    public CoolString() {}

    public CoolString(String v) {
        this.value = v;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value; 
    }

    public String toString() {
        return String.format("'%s'", value);
    }
}
