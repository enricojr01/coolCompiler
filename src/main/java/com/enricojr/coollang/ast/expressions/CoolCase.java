package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.program.CoolFormal;

/*
* NOTE: Case expressions provide runtime type checking on objects.
*
* It wasn't obvious just by looking. Given the code snippet:
*
* case <expr0> of
*   <id1> : <type1> => <expr1>;
*   <idn> : <typen> => <exprn>;
*
* the following will occur - first, expr0 is evaulated and its type (designated C) noted.
* next, a branch is selected with the least <typeK> such that C <= <typeK>, its corresponding
* identifier is bound to the value of expr0, and its corresponding expression is run. The result
* of case is the value of that expression.
* */
public class CoolCase extends CoolExpr {
    private CoolExpr predicate;
    private ArrayList<CoolCaseBranch> branches;

    public CoolCase() {}

    public CoolExpr getPredicate() {
        return predicate;
    }

    public void setPredicate(CoolExpr predicate) {
        this.predicate = predicate;
    }

    public ArrayList<CoolCaseBranch> getBranches() {
        return branches;
    }

    public void setBranches(ArrayList<CoolCaseBranch> branches) {
        this.branches = branches;
    }

    public CoolCaseBranch createBranch(CoolFormal formal, CoolExpr expression) {
        CoolCaseBranch b = new CoolCaseBranch();
        b.setFormal(formal);
        b.setExpression(expression);

        return b;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<CoolCaseStatement - %s>", this.predicate));
        return sb.toString();
    }

    public void accept(AstVisitor t) {
        t.visitCoolCase(this);
    }
}
