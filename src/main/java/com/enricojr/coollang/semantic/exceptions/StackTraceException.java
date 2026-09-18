package com.enricojr.coollang.semantic.exceptions;

import com.enricojr.coollang.ast.program.CoolBaseNode;

import java.util.LinkedList;

public class StackTraceException extends RuntimeException {
    private LinkedList<CoolBaseNode> stackTrace;
    private String fileName;
    private String errorMessage;
    private String location;

    public StackTraceException(String message, String fileName, String location, LinkedList<CoolBaseNode> stack) {
        this.stackTrace.addAll(stack);
        this.fileName = fileName;
        this.errorMessage = message;
        this.location = location;
    }

    public LinkedList<CoolBaseNode> getCustomStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(LinkedList<CoolBaseNode> stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
