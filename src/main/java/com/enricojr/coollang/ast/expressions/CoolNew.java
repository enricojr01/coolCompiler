package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.constants.CoolString;

public class CoolNew extends CoolExpr {
    private CoolString name;    

    public CoolNew() {}

    public CoolString getName() {
        return name;
    }

    public void setName(CoolString name) {
        this.name = name;
    }

    public String toString() {
        return String.format("Instantiate - %s\n", this.name);
    }
}
