package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolIOType extends CoolClass {
    public CoolIOType() {
        this.setName(new CoolIdentifier("IO"));
        this.setParentName(new CoolIdentifier("Object"));
    }
}
