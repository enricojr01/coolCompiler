package com.enricojr.coollang.ast.constants;

import com.enricojr.coollang.ast.AstVisitor;

public class CoolBool extends CoolLiteral {
    private boolean value;

    public CoolBool() {
        this.value = false;
    }

    public CoolBool(boolean v) {
        this.value = v;
    }

    public boolean getValue() {
        return this.value;
    }

    public void setValue(boolean b) {
        this.value = b;
    }

    public String toString() {
        return String.format("<Boolean - %s>", this.value);
    }

    public void accept(AstVisitor t) {
        t.visitCoolBool(this);
    }
}
