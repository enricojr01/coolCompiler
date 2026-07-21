package ast.expressions;

import ast.program.CoolClass;

public class CoolLet extends CoolExpr {
    private String identifier; 
    private CoolClass type;
    private CoolExpr init;
    private CoolExpr body;
}
