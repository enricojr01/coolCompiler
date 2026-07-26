package com.enricojr.coollang.ast.constants;

// TODO: change all usages of straight String in the AST and Nodes to CoolIdentifier
public class CoolIdentifier extends CoolConstant {
    private String value;

    public CoolIdentifier(String v) {
        this.value = v;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String v) {
        this.value = v;
    }

    public String toString() {
        return String.format("<Identifier - %s>", this.value);
    }
}
