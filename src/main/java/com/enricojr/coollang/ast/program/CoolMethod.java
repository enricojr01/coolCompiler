package com.enricojr.coollang.ast.program;

import java.util.ArrayList;
import com.enricojr.coollang.ast.constants.CoolConstant;
import com.enricojr.coollang.ast.expressions.CoolExpr;

public class CoolMethod extends CoolBaseNode {
    private String name;
    // TODO: I'm going to need some kind of symbol table for this but we're not there in class yet
    private CoolConstant returnType;
    private ArrayList<CoolFormal> params;
    private ArrayList<CoolExpr> expressions;

    public CoolMethod() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CoolFormal> getParams() {
        return params;
    }

    public void setParams(ArrayList<CoolFormal> params) {
        this.params = params;
    }

    public ArrayList<CoolExpr> getExpressions() {
        return expressions;
    }

    public void setExpressions(ArrayList<CoolExpr> expressions) {
        this.expressions = expressions;
    }

    public CoolConstant getReturnType() {
        return returnType;
    }

    public void setReturnType(CoolConstant returnType) {
        this.returnType = returnType;
    }
}
