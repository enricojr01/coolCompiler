package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;

public class CoolUnaryOp extends CoolExpr {
    public enum OPERATOR {
        NOT,
        COMPLEMENT
    }

    private CoolUnaryOp.OPERATOR op;
    private CoolExpr expression;

    public CoolUnaryOp() {}

    public CoolUnaryOp.OPERATOR getOp() {
        return op;
    }

    public void setOp(CoolUnaryOp.OPERATOR op) {
        this.op = op;
    }

    public CoolExpr getExpression() {
        return expression;
    }

    public void setExpression(CoolExpr expression) {
        this.expression = expression;
    }

    public String toString() {
        return String.format("<CoolUnaryOp - %s %s>", this.op, this.expression);
    }

    public void accept(AstVisitor t) {
        t.visitCoolUnaryOp(this);
    }
}
