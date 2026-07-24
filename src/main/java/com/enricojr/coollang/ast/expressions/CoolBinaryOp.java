package com.enricojr.coollang.ast.expressions;

public class CoolBinaryOp extends CoolExpr {
    public enum OPERATOR {
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

    public CoolBinaryOp() {}

    public CoolExpr getLhs() {
        return lhs;
    }

    public void setLhs(CoolExpr lhs) {
        this.lhs = lhs;
    }

    public CoolBinaryOp.OPERATOR getOp() {
        return op;
    }

    public void setOp(CoolBinaryOp.OPERATOR op) {
        this.op = op;
    }

    public CoolExpr getRhs() {
        return rhs;
    }

    public void setRhs(CoolExpr rhs) {
        this.rhs = rhs;
    }
}
