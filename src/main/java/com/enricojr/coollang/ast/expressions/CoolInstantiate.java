package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolInstantiate extends CoolExpr {
    private CoolIdentifier identifier; 

    public CoolInstantiate() {}

    public CoolInstantiate(CoolIdentifier ci) {
        this.identifier = ci;
    }

    public CoolIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CoolIdentifier identifier) {
        this.identifier = identifier;
    }

    public String toString() {
        return String.format("<CoolInstantiate - %s>", this.identifier);
    }
}
