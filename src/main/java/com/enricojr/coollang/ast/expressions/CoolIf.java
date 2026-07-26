package com.enricojr.coollang.ast.expressions;

public class CoolIf extends CoolExpr {
    private CoolExpr predicate; 
    private CoolExpr thenExpr;
    private CoolExpr elseExpr;

    public CoolIf() {}

    public CoolExpr getPredicate() {
        return predicate;
    }

    public void setPredicate(CoolExpr predicate) {
        this.predicate = predicate;
    }

    public CoolExpr getThenExpr() {
        return thenExpr;
    }

    public void setThenExpr(CoolExpr thenExpr) {
        this.thenExpr = thenExpr;
    }

    public CoolExpr getElseExpr() {
        return elseExpr;
    }

    public void setElseExpr(CoolExpr elseExpr) {
        this.elseExpr = elseExpr;
    }

    public String toString() {
        return String.format(
            "<IfStatement %s THEN %s ELSE %s>", this.predicate, this.thenExpr, this.elseExpr
        );
    }
}
