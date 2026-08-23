package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolIntegerType extends CoolClass {
    public CoolIntegerType() {
        this.setName(new CoolIdentifier("Int"));
        this.setParentName(new CoolIdentifier("Object"));
    }
}
