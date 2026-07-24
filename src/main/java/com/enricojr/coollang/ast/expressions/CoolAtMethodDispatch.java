package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;
import com.enricojr.coollang.ast.program.CoolClass;

public class CoolAtMethodDispatch extends CoolExpr {
    private String identifier; 
    private CoolExpr lhs; 
    private CoolClass type;
    private ArrayList<CoolExpr> arguemnts;

    private CoolAtMethodDispatch() {}
}
