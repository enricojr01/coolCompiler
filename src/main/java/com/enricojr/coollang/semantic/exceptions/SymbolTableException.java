package com.enricojr.coollang.semantic.exceptions;

import com.enricojr.coollang.ast.program.CoolBaseNode;

public class SymbolTableException extends RuntimeException {
    private SymbolTableException(String message) {
        super(message);
    }

    public static SymbolTableException factory(String message, CoolBaseNode cbn) {
        String realMessage = String.format("%s @ L%s:%s", message, cbn.getLine(), cbn.getCharPos());
        return new SymbolTableException(realMessage);
    }
}
