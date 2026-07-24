package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolAtMethodDispatch extends CoolExpr {
    private String identifier; 
    private CoolExpr lhs; 
    private String type;
    private ArrayList<CoolExpr> arguemnts;

    public CoolAtMethodDispatch() {}

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public CoolExpr getLhs() {
        return lhs;
    }

    public void setLhs(CoolExpr lhs) {
        this.lhs = lhs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<CoolExpr> getArguemnts() {
        return arguemnts;
    }

    public void setArguemnts(ArrayList<CoolExpr> arguemnts) {
        this.arguemnts = arguemnts;
    }
}
