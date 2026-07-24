package com.enricojr.coollang.ast.expressions;

public class CoolNew extends CoolExpr {
    private String name;    

    public CoolNew() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
