package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;
import com.enricojr.coollang.ast.constants.CoolIdentifier;

public class CoolMethodDispatch extends CoolExpr {
    private CoolIdentifier identifier;
    private ArrayList<CoolExpr> arguments;

    public CoolMethodDispatch() {}

    public CoolIdentifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(CoolIdentifier identifier) {
        this.identifier = identifier;
    }

    public ArrayList<CoolExpr> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<CoolExpr> arguments) {
        this.arguments = arguments;
    }
    
    public String toString() {
        return String.format("<SelfMethodCall %s>", this.identifier);
    }
}
