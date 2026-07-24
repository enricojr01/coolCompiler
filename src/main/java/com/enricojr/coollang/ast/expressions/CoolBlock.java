package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;

public class CoolBlock extends CoolExpr {
    private ArrayList<CoolExpr> body;

    public CoolBlock() {}

    public ArrayList<CoolExpr> getBody() {
        return this.body;
    }

    public void setBody(ArrayList<CoolExpr> body) {
        this.body = body;
    }
}
