package com.enricojr.coollang.ast.expressions;

public class CoolAssign extends CoolExpr {
    private String name;
    private CoolExpr expression;
    
    public CoolAssign() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoolExpr getExpression() {
        return expression;
    }

    public void setExpression(CoolExpr expression) {
        this.expression = expression;
    }
}
