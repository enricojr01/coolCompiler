package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.ast.expressions.CoolExpr;

public class CoolAttribute extends CoolBaseNode {
    private String identifier;
    private String typeName;
    private CoolExpr value;

    public CoolAttribute() {}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public CoolExpr getValue() {
        return value;
    }

    public void setValue(CoolExpr value) {
        this.value = value;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    
}
