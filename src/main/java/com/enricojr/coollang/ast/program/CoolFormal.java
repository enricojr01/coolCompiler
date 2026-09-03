package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolFormal extends CoolBaseNode {
    private CoolIdentifier name;
    private CoolIdentifier type;

    public CoolFormal() {}

    public CoolIdentifier getName() {
        return name;
    }

    public String getNameString() {
        return this.name.getValue().toString();
    }

    public void setName(CoolIdentifier name) {
        this.name = name;
    }

    public CoolIdentifier getType() {
        return type;
    }

    public String getTypeString() {
        return this.type.getValue().toString();
    }

    public void setType(CoolIdentifier type) {
        this.type = type;
    }

    public String toString() {
        return String.format("(CoolFormal - %s : %s)", this.name, this.type);
    }

    public void accept(AstVisitor t) {
        t.visitCoolFormal(this);
    }
}
