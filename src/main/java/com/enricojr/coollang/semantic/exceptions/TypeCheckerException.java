package com.enricojr.coollang.semantic.exceptions;

import com.enricojr.coollang.ast.program.CoolBaseNode;
import org.antlr.v4.runtime.Token;

public class TypeCheckerException extends RuntimeException {
    private TypeCheckerException() {}

    private TypeCheckerException(String message) {
        super(message);
    }

    public static TypeCheckerException factory(String message, CoolBaseNode cbn) {
        String realMessage = String.format("%s @ L%s:%s", message, cbn.getLine(), cbn.getCharPos());
        return new TypeCheckerException(message);
    }
}
