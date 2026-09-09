package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.CoolExpr;

public class CoolAttribute extends CoolBaseNode {
    private CoolIdentifier identifier;
    private CoolIdentifier typeName;
    private CoolExpr initExpression;
    private CoolClass computedType;

    public CoolAttribute() {}

    public CoolIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CoolIdentifier identifier) {
        this.identifier = identifier;
    }

    public CoolExpr getInitExpression() {
        return initExpression;
    }

    public void setInitExpression(CoolExpr value) {
        this.initExpression = value;
    }

    public CoolIdentifier getTypeName() {
        return typeName;
    }

    public void setTypeName(CoolIdentifier typeName) {
        this.typeName = typeName;
    }

    public CoolClass getComputedType() {
        return computedType;
    }

    public void setComputedType(CoolClass computedType) {
        this.computedType = computedType;
    }

    public String toString() {
        return String.format(
            "(CoolAttribute: %s: %s <- %s)", 
            this.identifier.getValue(), 
            this.typeName.getValue(),
            this.initExpression
        );
    }

    public void accept(AstVisitor t) {
        t.visitCoolAttribute(this);
    }
}
