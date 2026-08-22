package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.constants.CoolConstant;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolExpr extends CoolBaseNode {
    // NOTE: The compiler should create CoolClass() instances for Int, Bool, and String
    private CoolClass computedType;
    private CoolConstant computedValue;

    public CoolClass getComputedType() {
        return this.computedType;
    }

    public void setType(CoolClass type) {
        this.computedType = type;
    }

    public CoolConstant getComputedValue() {
        return this.computedValue;
    }

    public void setValue(CoolConstant value) {
        this.computedValue = value;
    }
}
