package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;

public class CoolDotMethodDispatch extends CoolExpr {
    private CoolExpr lhs; 
    private String name;
    private ArrayList<CoolExpr> arguments; 

    public CoolDotMethodDispatch() {}

    public CoolExpr getLhs() {
        return lhs;
    }

    public void setLhs(CoolExpr lhs) {
        this.lhs = lhs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CoolExpr> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<CoolExpr> arguments) {
        this.arguments = arguments;
    }
}
