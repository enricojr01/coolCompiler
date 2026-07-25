package com.enricojr.coollang.ast.constants;

public class CoolType extends CoolConstant {
    private String value;

    public CoolType() {}

    public CoolType(String v) {
        this.value = v;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String v) {
        this.value = v;
    }
    
}
