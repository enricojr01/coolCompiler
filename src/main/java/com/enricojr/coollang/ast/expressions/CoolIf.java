package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;

public class CoolIf extends CoolExpr {
    private CoolExpr guard;
    private CoolExpr consequent;
    private CoolExpr alternative;

    public CoolIf() {}

    public CoolExpr getGuard() {
        return guard;
    }

    public void setGuard(CoolExpr guard) {
        this.guard = guard;
    }

    public CoolExpr getConsequent() {
        return consequent;
    }

    public void setConsequent(CoolExpr consequent) {
        this.consequent = consequent;
    }

    public CoolExpr getAlternative() {
        return alternative;
    }

    public void setAlternative(CoolExpr alternative) {
        this.alternative = alternative;
    }

    public String toString() {
        return String.format(
            "<CoolIfStatement %s THEN %s ELSE %s>", this.guard, this.consequent, this.alternative
        );
    }

    public void accept(AstVisitor t) {
        t.visitCoolIf(this);
    }
}
