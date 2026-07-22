package com.enricojr.coollang.ast.expressions;

public class CoolBinaryOp extends CoolExpr {
    enum OPERATOR {
        ADD,
        SUB,
        MUL,
        DIV,
        LT,
        LTE,
        GT,
        GTE,
        EQ,
    }

    private CoolExpr lhs;
    private CoolBinaryOp.OPERATOR op;
    private CoolExpr rhs;
}
