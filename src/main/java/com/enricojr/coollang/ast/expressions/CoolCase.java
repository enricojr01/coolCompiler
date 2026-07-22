package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolCase extends CoolExpr {
    // TODO: Consider moving this to its own file?
    class Branch {
        private String name;
        private CoolClass type;
        private CoolExpr expression;
    }

    private CoolExpr predicate; 
    private ArrayList<Branch> branches;
}
