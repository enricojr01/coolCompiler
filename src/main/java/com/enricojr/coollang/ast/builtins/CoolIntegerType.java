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

    @Override
    protected ArrayList<String> getDefaultInits() {
        // HACK: overrides default inits for classes because the prototype of Int
        // should have a single attribute field set to the 32-bit int 0;
        ArrayList<String> defaults = new ArrayList<>();
        defaults.add("0");

        return defaults;
    }

    public String defaultInit() {
        return "int_zero";
    }
}
