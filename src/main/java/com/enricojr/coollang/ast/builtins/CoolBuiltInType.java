package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.program.CoolAttribute;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolMethod;

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
}
