package com.enricojr.coollang.ast.constants;

import com.enricojr.coollang.ast.AstVisitor;

public class CoolSelf extends CoolIdentifier {
    public CoolSelf() {
        super("self");
    }

    public String toString() {
        return "SELF_TYPE";
    }

    public void accept(AstVisitor t) {
        t.visitCoolSelf(this);
    }
}
