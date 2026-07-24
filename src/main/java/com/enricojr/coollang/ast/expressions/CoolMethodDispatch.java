package com.enricojr.coollang.ast.expressions;

import java.util.ArrayList;

public class CoolMethodDispatch extends CoolExpr {
    private String identifier;
    private ArrayList<CoolExpr> arguments;

    public CoolMethodDispatch() {}
}
