package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolAttribute;
import com.enricojr.coollang.ast.program.CoolClass;

import java.util.ArrayList;

public class CoolIntegerType extends CoolBuiltInType {
    public CoolIntegerType() {
        this.setName(new CoolIdentifier("Int"));
        this.setParentName(new CoolIdentifier("Object"));

    }

    public String defaultInit() {
        return "int_zero";
    }
}
