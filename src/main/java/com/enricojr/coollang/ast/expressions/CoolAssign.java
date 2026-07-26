package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolAssign extends CoolExpr {
    private CoolIdentifier name;
    private CoolExpr expression;
    
    public CoolAssign() {}

    public CoolIdentifier getName() {
        return name;
    }

    public void setName(CoolIdentifier name) {
        this.name = name;
    }

    public CoolExpr getExpression() {
        return expression;
    }

    public void setExpression(CoolExpr expression) {
        this.expression = expression;
    }

    public String toString() {
        return String.format("<Assign - %s <- %s>\n", this.name, this.expression);
    }
}
