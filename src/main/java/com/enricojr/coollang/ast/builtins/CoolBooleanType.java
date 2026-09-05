package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolBooleanType extends CoolBuiltInType {

    public CoolBooleanType() {
        this.setName(new CoolIdentifier("Bool"));
        this.setParentName(new CoolIdentifier("Object"));
    }

    public CoolBooleanType(CoolClass parent) {
        super(parent);
        this.setName(new CoolIdentifier("Bool"));
        this.setParentName(new CoolIdentifier("Object"));
    }
}
