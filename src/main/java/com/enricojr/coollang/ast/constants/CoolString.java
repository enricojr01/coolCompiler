package com.enricojr.coollang.ast.constants;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.CoolIntegerType;
import com.enricojr.coollang.ast.builtins.CoolStringType;

public class CoolString extends CoolLiteral {
    private String value;
    private static int counter = 0;

    public CoolString() {
        this.setComputedType(new CoolStringType());
        counter += 1;
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
        return String.format("<CoolString: '%s'>", value);
    }

    public int length() {
        return this.value.length();
    }

    public void accept(AstVisitor t) {
        t.visitCoolString(this);
    }

    public String toMipsLayout() {
        return ".word str_empty";
    }
}
