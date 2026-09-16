package com.enricojr.coollang.ast.builtins;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolMethod;

public class CoolSelfType extends CoolBuiltInType {
    private CoolClass selfTypeOf;

    public CoolSelfType(CoolClass selfTypeOf) {
        this.selfTypeOf = selfTypeOf;
        this.setName(new CoolIdentifier("SELF_TYPE"));
    }

    public CoolClass getTypeOf() {
        return this.selfTypeOf;
    }

    public CoolClass getComputedType() {
        return this.selfTypeOf;
    }

    public CoolMethod classMethodSearch(CoolIdentifier method) {
        return this.selfTypeOf.classMethodSearch(method);
    }
}
