package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolAtMethodDispatch extends CoolExpr {
    private CoolExpr lhs; 
    private CoolIdentifier identifier;
    private CoolIdentifier classType;
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

    public ArrayList<CoolExpr> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<CoolExpr> arguments) {
        this.arguments = arguments;
    }

    public String toString() {
        return String.format("<CoolAtMethodCall - %s>", this.identifier);
    }

    public CoolIdentifier getClassType() {
        return classType;
    }

    public void setClassType(CoolIdentifier classType) {
        this.classType = classType;
    }
}
