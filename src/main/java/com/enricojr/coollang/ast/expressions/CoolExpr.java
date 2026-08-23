package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.constants.CoolConstant;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.SymbolTable;

public class CoolExpr extends CoolBaseNode {
    private CoolClass computedType;
    private CoolConstant computedValue;
    // NOTE: This is the only way I could think of to get symbols out of
    //       a let or case statement that may be deeply nested.
    private SymbolTable symbols;

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

    public SymbolTable getSymbols() {
        return this.symbols;
    }

    public void setSymbols(SymbolTable st) {
        this.symbols = st;
    }
}
