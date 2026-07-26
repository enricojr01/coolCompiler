package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.constants.CoolType;

public class CoolAtMethodDispatch extends CoolExpr {
    private CoolExpr lhs; 
    private CoolType type;
    private CoolIdentifier identifier; 
    private ArrayList<CoolExpr> arguments;

    public CoolAtMethodDispatch() {}

    public CoolIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CoolIdentifier identifier) {
        this.identifier = identifier;
    }

    public CoolExpr getLhs() {
        return lhs;
    }

    public void setLhs(CoolExpr lhs) {
        this.lhs = lhs;
    }

    public CoolType getType() {
        return type;
    }

    public void setType(CoolType type) {
        this.type = type;
    }

    public ArrayList<CoolExpr> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<CoolExpr> arguments) {
        this.arguments = arguments;
    }

    public String toString() {
        return String.format("<AtMethodCall - %s>", this.identifier);
    }
}
