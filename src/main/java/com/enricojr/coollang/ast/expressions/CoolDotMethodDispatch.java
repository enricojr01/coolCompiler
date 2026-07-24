package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;

public class CoolDotMethodDispatch extends CoolExpr {
    private CoolExpr lhs; 
    private String name;
    private ArrayList<CoolExpr> arguments; 
}
