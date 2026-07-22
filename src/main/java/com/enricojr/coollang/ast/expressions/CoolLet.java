package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.program.CoolClass;

public class CoolLet extends CoolExpr {
    private String identifier; 
    private CoolClass type;
    private CoolExpr init;
    private CoolExpr body;
}
