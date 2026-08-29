package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolIsVoid extends CoolExpr {
    private CoolIdentifier identifier;
    private CoolExpr expression;

    public CoolIsVoid() {}

    public CoolExpr getExpression() {
        return expression;
    }

    public void setExpression(CoolExpr expression) {
        this.expression = expression;
    }

    public CoolIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CoolIdentifier type) {
        this.identifier = type;
    }

    public String toString() {
        return String.format("<CoolIsVoid %s>", this.identifier);
    }

    public void accept(AstVisitor t) {
        t.visitCoolIsVoid(this);
    }

}
