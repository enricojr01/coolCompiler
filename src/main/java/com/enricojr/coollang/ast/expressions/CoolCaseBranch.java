package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.program.CoolFormal;

public class CoolCaseBranch extends CoolExpr {
    private CoolFormal formal;
    private CoolExpr expression;

    public CoolCaseBranch() {}

    public CoolFormal getFormal() {
        return formal;
    }

    public void setFormal(CoolFormal formal) {
        this.formal = formal;
    }

    public CoolExpr getExpression() {
        return expression;
    }

    public void setExpression(CoolExpr expression) {
        this.expression = expression;
    }

    public void accept(AstVisitor t) {
        t.visitCoolCaseBranch(this);
    }
}
