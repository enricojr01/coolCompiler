package com.enricojr.coollang.ast.constants;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.CoolStringType;

public class CoolString extends CoolLiteral {
    private String value;

    public CoolString() {
        this.setComputedType(new CoolStringType());
    }

    public CoolString(String v) {
        this.value = v;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value; 
    }

    public String toString() {
        return String.format("'%s'", value);
    }

    public void accept(AstVisitor t) {
        t.visitCoolString(this);
    }
}
