package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;
import com.enricojr.coollang.ast.program.CoolFormal;

public class CoolCase extends CoolExpr {
    // TODO: Consider moving this to its own file?
    public class Branch {
        private CoolFormal formal;
        private CoolExpr expression;

        private Branch() {}

        public CoolFormal getFormal() {
            return formal;
        }

        public void setFormal(CoolFormal formal) {
            this.formal = formal;
        }

        public CoolExpr getExpression() {
            return expression;
        }

        public void setExpression(CoolExpr expression) {
            this.expression = expression;
        }
    }

    private CoolExpr predicate; 
    private ArrayList<Branch> branches;

    public CoolCase() {}

    public CoolExpr getPredicate() {
        return predicate;
    }

    public void setPredicate(CoolExpr predicate) {
        this.predicate = predicate;
    }

    public ArrayList<Branch> getBranches() {
        return branches;
    }

    public void setBranches(ArrayList<Branch> branches) {
        this.branches = branches;
    }

    public Branch createBranch(CoolFormal formal, CoolExpr expression) {
        Branch b = new Branch();
        b.setFormal(formal);
        b.setExpression(expression);

        return b;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<CaseStatement - %s>\n", this.predicate));
        return sb.toString();
    }
}
