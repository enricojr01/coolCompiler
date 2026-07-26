package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolDotMethodDispatch extends CoolExpr {
    private CoolExpr lhs; 
    private CoolIdentifier name;
    private ArrayList<CoolExpr> arguments; 

    public CoolDotMethodDispatch() {}

    public CoolExpr getLhs() {
        return lhs;
    }

    public void setLhs(CoolExpr lhs) {
        this.lhs = lhs;
    }

    public CoolIdentifier getName() {
        return name;
    }

    public void setName(CoolIdentifier name) {
        this.name = name;
    }

    public ArrayList<CoolExpr> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<CoolExpr> arguments) {
        this.arguments = arguments;
    }

    public String toString() {
        return String.format("<MethodCall - %s.%s(%s arguments)>", this.lhs, this.name, this.arguments.size());
    }
}
