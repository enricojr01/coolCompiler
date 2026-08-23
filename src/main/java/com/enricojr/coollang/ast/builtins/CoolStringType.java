package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolStringType extends CoolClass {
    public CoolStringType() {
        this.setName(new CoolIdentifier("String"));
        this.setParentName(new CoolIdentifier("Object"));
    }
}
