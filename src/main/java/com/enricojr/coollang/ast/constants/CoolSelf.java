package com.enricojr.coollang.ast.constants;

public class CoolSelf extends CoolIdentifier {
    public CoolSelf() {
        super("self");
    }

    public String getValue() {
        return super.getValue();
    }

    public String toString() {
        return "<SELF_TYPE>";
    }
}
