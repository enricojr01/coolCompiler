package com.enricojr.coollang.ast.expressions;

public class CoolUnaryOp extends CoolExpr {
    enum OPERATOR {
        NOT,
        COMPLEMENT
    }

    private CoolUnaryOp.OPERATOR op;
    private CoolExpr expression;
}
