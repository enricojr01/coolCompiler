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
        return String.format("'%s'", value);
    }

    public int length() {
        return this.value.length();
    }

    public void accept(AstVisitor t) {
        t.visitCoolString(this);
    }

    public String codeGenerate() {
        StringBuilder sb = new StringBuilder();
        String label = String.format("str_const%s:\n", counter);
        String labelSize = String.format("str_const%s_size:\n", counter);
        String contents = String.format("\"%s\"", this.value);
        int valueWordSize = Math.max((this.value.getBytes().length / 4), 1);
        int objSize = 4 + valueWordSize;

        // NOTE: this section represents the integer value of the string length
        sb.append(".word -1\n");
        sb.append(labelSize);
        sb.append(".word ").append(CoolIntegerType.getMipsTag()).append("\n");
        sb.append(".word 4\n"); // maybe I can get away with just hard-coding 4 because they're Ints?
        sb.append(".word Int_dispTab\n");
        sb.append(".word ").append(this.value.length()).append("\n");
        sb.append(".word -1\n");

        // NOTE: the 2nd field here is the size of the object in words, not of string length
        sb.append(label);
        sb.append(".word ").append(CoolStringType.getMipsTag()).append("\n");
        sb.append(".word ").append(objSize).append("\n");
        sb.append(".word String_dispTab\n");
        sb.append(".word ").append(labelSize).append("\n");
        sb.append(".ascii ").append(contents).append("\n");
        sb.append(".byte 0\n");

        return sb.toString();
    }
}
