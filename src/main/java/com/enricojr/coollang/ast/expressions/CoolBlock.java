package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;

import java.util.ArrayList;

public class CoolBlock extends CoolExpr {
    private ArrayList<CoolExpr> expressions;

    public CoolBlock() {}

    public ArrayList<CoolExpr> getExpressions() {
        return this.expressions;
    }

    public void setExpressions(ArrayList<CoolExpr> expressions) {
        this.expressions = expressions;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<CoolBlock - %s expressions>", this.expressions.size()));
        return sb.toString();
    }

    public void accept(AstVisitor t) {
        t.visitCoolBlock(this);
    }

}
