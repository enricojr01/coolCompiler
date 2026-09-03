package com.enricojr.coollang.ast.expressions;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.constants.CoolLiteral;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.SymbolTable;

public class CoolExpr extends CoolBaseNode {
    private CoolClass computedType;
    private CoolLiteral computedValue;
    // NOTE: This is the only way I could think of to get symbols out of
    //       a let or case statement that may be deeply nested.
    private SymbolTable symbols;

    public String symbolTableReport() {
        StringBuilder sb = new StringBuilder();
        if (this.symbols != null) {
            sb.append(String.format("Symbol Table for Expression: %s", this.getClass().getName()));
            sb.append(this.symbols.toString());
        } else {
            sb.append(String.format("Expression %s has no symbol table\n", this.getClass().getName()));
        }
        return sb.toString();
    }

    public CoolClass getComputedType() {
        return this.computedType;
    }

    public void setType(CoolClass type) {
        this.computedType = type;
    }

    public CoolLiteral getComputedValue() {
        return this.computedValue;
    }

    public void setValue(CoolLiteral value) {
        this.computedValue = value;
    }

    public SymbolTable getSymbols() {
        return this.symbols;
    }

    public void setSymbols(SymbolTable st) {
        this.symbols = st;
    }

    public void accept(AstVisitor t) {
    }
}
