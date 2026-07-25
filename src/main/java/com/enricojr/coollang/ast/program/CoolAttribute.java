package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.CoolExpr;

public class CoolAttribute extends CoolBaseNode {
    private CoolIdentifier identifier;
    private CoolIdentifier typeName;
    private CoolExpr value;

    public CoolAttribute() {}

    public CoolIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CoolIdentifier identifier) {
        this.identifier = identifier;
    }

    public CoolExpr getValue() {
        return value;
    }

    public void setValue(CoolExpr value) {
        this.value = value;
    }

    public CoolIdentifier getTypeName() {
        return typeName;
    }

    public void setTypeName(CoolIdentifier typeName) {
        this.typeName = typeName;
    }

    public String toString() {
        return String.format(
            "Attribute - %s: %s <- %s", 
            this.identifier.getValue(), 
            this.typeName.getValue()
        );
    } 
}
