package com.enricojr.coollang.semantic.exceptions;

public class CoolClassInheritanceCycleException extends Exception {
    public CoolClassInheritanceCycleException(String message) {
        super(message);
    }
}
