package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolLiteral;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.SymbolTable;

public class CoolExpr extends CoolBaseNode {
    private CoolClass computedType;
    private CoolLiteral computedValue;

    public CoolClass getComputedType() {
        return this.computedType;
    }

    public void setComputedType(CoolClass type) {
        this.computedType = type;
    }

    public CoolLiteral getComputedValue() {
        return this.computedValue;
    }

    public void setComputedValue(CoolLiteral value) {
        this.computedValue = value;
    }

    public void accept(AstVisitor t) {
    }
}
