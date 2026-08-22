package com.enricojr.coollang.ast.program;

import java.util.ArrayList;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.expressions.CoolExpr;
import com.enricojr.coollang.semantic.SymbolTable;

public class CoolMethod extends CoolBaseNode {
    private CoolIdentifier name;
    private CoolIdentifier returnType;
    private CoolParamList parameters;
    private ArrayList<CoolExpr> expressions;
    private SymbolTable symbols;

    public CoolMethod() {}

    public CoolIdentifier getName() {
        return name;
    }

    public void setName(CoolIdentifier name) {
        this.name = name;
    }

    public ArrayList<CoolExpr> getExpressions() {
        return expressions;
    }

    public void setExpressions(ArrayList<CoolExpr> expressions) {
        this.expressions = expressions;
    }

    public CoolIdentifier getReturnType() {
        return returnType;
    }

    public void setReturnType(CoolIdentifier returnType) {
        this.returnType = returnType;
    }

    public CoolParamList getParameters() {
        return parameters;
    }

    public void setParameters(CoolParamList parameters) {
        this.parameters = parameters;
    }

    public SymbolTable getSymbols() {
        return symbols;
    }

    public void setSymbols(SymbolTable symbols) {
        this.symbols = symbols;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("(CoolMethod: %s :: %s)", this.name, this.returnType));
        return sb.toString();
    }
}
