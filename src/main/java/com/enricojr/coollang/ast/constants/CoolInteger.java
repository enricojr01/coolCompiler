package com.enricojr.coollang.ast.constants;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.CoolIntegerType;

public class CoolInteger extends CoolLiteral {
    public static int counter = 0;
    private int value;

    public CoolInteger() {
    }

    public CoolInteger(int v) {
        this.value = v;
        counter += 1;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int v) {
        this.value = v;
    }

    public String toString() {
        return String.format("<Integer - %s>", this.value);
    }

    public void accept(AstVisitor t) {
        t.visitCoolInteger(this);
    }

    public String toMipsLayout() {
        return ".word int_zero";
    }
}
