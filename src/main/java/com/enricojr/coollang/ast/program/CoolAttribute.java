package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.ast.expressions.CoolExpr;

public class CoolAttribute extends CoolBaseNode {
    private String identifier;
    private CoolClass type;
    private CoolExpr value;
}
