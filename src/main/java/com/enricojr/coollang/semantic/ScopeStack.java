package com.enricojr.coollang.semantic;

public class ScopeStack {
    private ScopeStack head;
    private ScopeStack tail;

    public ScopeStack() {}

    public ScopeStack getTail() {
        return tail;
    }

    public void setTail(ScopeStack tail) {
        this.tail = tail;
    }

    public ScopeStack getHead() {
        return head;
    }

    public void setHead(ScopeStack head) {
        this.head = head;
    }
}
