package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolIsVoid extends CoolExpr {
    private CoolIdentifier identifier;    

    public CoolIsVoid() {}

    public CoolIdentifier getType() {
        return identifier;
    }

    public void setType(CoolIdentifier type) {
        this.identifier = type;
    }

    public String toString() {
        return String.format("<CoolIsVoid %s>", this.identifier);
    }

}
