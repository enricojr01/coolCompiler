package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;

public class CoolParenthesisExpr extends CoolExpr {
    private CoolExpr expression;

    public CoolParenthesisExpr() {}

    public CoolExpr getExpression() {
        return expression;
    }

    public void setExpression(CoolExpr expression) {
        this.expression = expression;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<CoolParenthesizedExpr %s>", this.expression));
        return sb.toString();
    }

    public void accept(AstVisitor t) {
        t.visitCoolParenthesisExpr(this);
    }
}
