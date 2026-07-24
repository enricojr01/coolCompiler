package com.enricojr.coollang.ast.program;

public class CoolFormal extends CoolBaseNode {
    private String name;
    private String type;

    public CoolFormal() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
