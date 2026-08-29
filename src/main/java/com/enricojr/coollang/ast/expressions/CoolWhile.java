package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;

public class CoolWhile extends CoolExpr {
    private CoolExpr predicate; 
    private CoolExpr body;

    public CoolWhile() {}

    public CoolExpr getPredicate() {
        return predicate;
    }

    public void setPredicate(CoolExpr predicate) {
        this.predicate = predicate;
    }

    public CoolExpr getBody() {
        return body;
    }

    public void setBody(CoolExpr body) {
        this.body = body;
    }
    
    public String toString() {
        return String.format("<CoolWhileStatement - %s>", this.predicate, this.body);
    }

    public void accept(AstVisitor t) {
        t.visitCoolWhile(this);
    }
}
