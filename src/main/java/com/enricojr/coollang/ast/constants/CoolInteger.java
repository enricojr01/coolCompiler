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

    public String codeGenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append(".word -1\n");
        String label = String.format("int_const%s:\n", counter);
        sb.append(label);
        sb.append(".word ").append(CoolIntegerType.getMipsTag()).append("\n");
        sb.append(".word 4\n");
        sb.append(".word Int_dispTab\n");
        sb.append(".word ").append(value).append("\n");

        return sb.toString();
    }
}
