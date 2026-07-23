package com.enricojr.coollang.ast.program;

public class CoolFormal extends CoolBaseNode {
    private String name;
    private CoolClass type;

    public CoolFormal() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoolClass getType() {
        return type;
    }

    public void setType(CoolClass type) {
        this.type = type;
    }

    
}
