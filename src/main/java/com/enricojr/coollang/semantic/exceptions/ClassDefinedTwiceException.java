package com.enricojr.coollang.semantic.exceptions;

public class ClassDefinedTwiceException extends Exception {
    // TODO: isn't there a better way to handle this? 
    public ClassDefinedTwiceException(String message) {
        super(message);
    }

}
