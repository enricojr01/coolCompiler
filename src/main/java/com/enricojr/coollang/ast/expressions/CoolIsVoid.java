package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.program.CoolClass;

public class CoolIsVoid extends CoolExpr {
    private String type;    

    public CoolIsVoid() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
