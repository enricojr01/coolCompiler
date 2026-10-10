package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolIntegerType extends CoolBuiltInType {
    public static int getMipsTag() {
        return 3;
    }

    public CoolIntegerType() {
        this.setName(new CoolIdentifier("Int"));
        this.setParentName(new CoolIdentifier("Object"));
    }

    public CoolIntegerType(CoolClass parent) {
        super(parent);
        this.setName(new CoolIdentifier("Int"));
        this.setParentName(new CoolIdentifier("Object"));
    }

    public String defaultInit() {
        return "int_zero";
    }
}
