package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;

public class CoolParenthesisExpr extends CoolExpr {
    // TODO: Is it worth it to have this class? It's just a single expression in the end.
    private CoolExpr expression;

    public CoolParenthesisExpr() {}

    public CoolExpr getExpression() {
        return expression;
    }

    public void setExpression(CoolExpr expression) {
        this.expression = expression;
    }
}
