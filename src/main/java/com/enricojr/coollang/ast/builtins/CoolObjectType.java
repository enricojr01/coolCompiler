package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolObjectType extends CoolClass {
    public CoolObjectType() {
        CoolIdentifier name = new CoolIdentifier("Object");
        this.setName(name);
    }
}
