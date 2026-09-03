package com.enricojr.coollang.ast.constants;

public class CoolSelf extends CoolIdentifier {
    public CoolSelf() {
        super("self");
    }

    public String toString() {
        return "<SELF_TYPE>";
    }
}
