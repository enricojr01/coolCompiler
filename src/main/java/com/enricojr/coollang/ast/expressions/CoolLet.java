package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;
import com.enricojr.coollang.ast.program.CoolAttribute;

public class CoolLet extends CoolExpr {
    private ArrayList<CoolAttribute> attributes;
    private CoolExpr expression;

    public CoolLet() {}

    public ArrayList<CoolAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<CoolAttribute> attributes) {
        this.attributes = attributes;
    }

    public CoolExpr getExpression() {
        return expression;
    }

    public void setExpression(CoolExpr expression) {
        this.expression = expression;
    }
}
