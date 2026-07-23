package com.enricojr.coollang.ast.expressions;

public class CoolIf extends CoolExpr {
    private CoolExpr predicate; 
    private CoolExpr thenExpr;
    private CoolExpr elseExpr;
}
