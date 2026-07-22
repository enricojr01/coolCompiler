package com.enricojr.coollang.ast.program;

import java.util.ArrayList;
import com.enricojr.coollang.ast.expressions.CoolExpr;

public class CoolMethod extends CoolBaseNode {
    private String name;
    private ArrayList<CoolAttribute> params;
    private ArrayList<CoolExpr> expressions;
}
