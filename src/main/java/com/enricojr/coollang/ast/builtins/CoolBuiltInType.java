package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.program.CoolClass;

public class CoolBuiltInType extends CoolClass {
    private boolean isBuiltIn = true;

    public CoolBuiltInType() {}

    public CoolBuiltInType(CoolClass parent) {
        this.setParent(parent);
    }
}
