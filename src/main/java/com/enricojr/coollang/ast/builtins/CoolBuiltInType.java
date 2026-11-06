package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.program.CoolClass;

import java.util.ArrayList;

public class CoolBuiltInType extends CoolClass {
    private final boolean isBuiltIn = true;

    public CoolBuiltInType() {
        this.setAttributes(new ArrayList<>());
        this.setMethods(new ArrayList<>());
    }

    public CoolBuiltInType(CoolClass parent) {
        this.setParent(parent);
    }

    // honestly this is probably better as an Interface or something
    public String defaultInit() {
        return null;
    }
}
