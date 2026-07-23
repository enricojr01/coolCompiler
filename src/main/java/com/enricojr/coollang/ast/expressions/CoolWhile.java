package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;

public class CoolWhile extends CoolExpr {
    private CoolExpr predicate; 
    private ArrayList<CoolExpr> body;
}
