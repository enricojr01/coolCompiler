package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;

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

    public String toString() {
        return String.format("<CoolBinaryOp - %s>", this.op);
    }

    public void accept(AstVisitor t) {
        t.visitCoolBinaryOp(this);
    }
}
