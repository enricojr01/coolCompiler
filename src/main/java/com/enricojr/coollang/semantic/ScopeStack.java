package com.enricojr.coollang.semantic;

public class ScopeStack {
    private ScopeTable head;
    private ScopeTable tail;

    public ScopeStack() {}

    public ScopeTable getTail() {
        return tail;
    }

    public void setTail(ScopeTable tail) {
        this.tail = tail;
    }

    public ScopeTable getHead() {
        return head;
    }

    public void setHead(ScopeTable head) {
        this.head = head;
    }

    public void push(ScopeTable obj) {
        if (this.head == null && this.tail == null) {
            this.head = obj;
            this.tail = obj;
        } else {
            obj.setPrevious(this.tail);
            this.tail.setNext(obj);
            this.tail = obj;
        }
    }

    public ScopeTable pop() {
        ScopeTable obj = this.tail;
        this.tail = this.tail.getPrevious();
        return obj;
    }
}
