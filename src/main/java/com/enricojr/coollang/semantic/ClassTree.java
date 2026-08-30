package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.builtins.*;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.exceptions.CoolClassUndefinedException;
import com.enricojr.coollang.semantic.exceptions.CoolInvalidInheritanceException;
import com.enricojr.coollang.semantic.exceptions.CoolParentUndefinedException;

import java.util.*;

public class ClassTree implements Iterable<ClassTreeNode> {
    class ClassTreeIterator implements Iterator<ClassTreeNode> {
        // NOTE: use it like a stack
        LinkedList<ClassTreeNode> iterate = new LinkedList<>();

        public ClassTreeIterator(ClassTree tree) {
            LinkedList<ClassTreeNode> travel = new LinkedList<>();
            ClassTreeNode start = tree.getRoot();
            travel.push(start);

            // NOTE: the point of doing it this way is so that the elements of iterate are in
            // depth-first order.
            while (!travel.isEmpty()) {
                ClassTreeNode current = travel.pop();
                iterate.push(current);
                ArrayList<ClassTreeNode> children = current.getChildren();
                if (children != null && !children.isEmpty()) {
                    for (ClassTreeNode child : current.getChildren()) {
                        CoolIdentifier childName = child.getCoolClass().getName();
                        // TODO: remove IO later because right now it doesn't have any methods, but later on it will.
                        if (childName.equals("Int") ||
                                childName.equals("String") ||
                                childName.equals("Bool") ||
                                childName.equals("IO")
                        ) {
                            continue;
                        } else {
                            travel.push(child);
                        }
                    }
                }
            }
        }


        @Override
        public boolean hasNext() {
            return this.iterate.peek() != null;
        }

        @Override
        public ClassTreeNode next() {
            return this.iterate.pop();
        }
    }

    private final ClassTreeNode root;

    public ClassTree() {
        // IO, Int, Bool, and String are the built-in classes.
        // The following restrictions will be enforced:
        // - You can inherit from IO but not redefine any of its methods, nor can you
        //   redefine IO itself.
        // - You cannot inherit from or redefine Int.
        // - You cannot inherit from or redefine Bool.
        // - You cannot inherit from or redefine String.
        // TODO: Think about a factory or something for all of these.
        CoolClass objectClass = new CoolObjectType();
        CoolClass ioClass = new CoolIOType();
        CoolClass stringClass = new CoolStringType();
        CoolClass intClass = new CoolIntegerType();
        CoolClass boolClass = new CoolBooleanType();

        ClassTreeNode rootNode = new ClassTreeNode(objectClass);
        ClassTreeNode ioNode = new ClassTreeNode(ioClass);
        ClassTreeNode stringNode = new ClassTreeNode(stringClass);
        ClassTreeNode intNode = new ClassTreeNode(intClass);
        ClassTreeNode boolNode = new ClassTreeNode(boolClass);

        this.root = rootNode;
        this.root.setChildren(new ArrayList<>(List.of(ioNode, stringNode, intNode, boolNode)));
    }

    public void addChild(CoolIdentifier parentIdentifier, CoolClass childClass) throws
            CoolParentUndefinedException, CoolInvalidInheritanceException {
        ClassTreeNode ctn = null;
        try {
            ctn = this.find(parentIdentifier);
        } catch (CoolClassUndefinedException e) {
            // Now that I think about it a cycle is impossible in a tree because one has to exist before the other.
            CoolClass cc = ctn.getCoolClass();
            String msg = String.format("Class %s references undefined parent %s", cc.getName(), cc.getParentName());
            throw new CoolParentUndefinedException(msg);
        }

        CoolIdentifier parentClassName = ctn.getCoolClass().getName();
        if (parentClassName.equals(new CoolIdentifier("Int")) ||
                parentClassName.equals(new CoolIdentifier("String")) ||
                parentClassName.equals(new CoolIdentifier("Bool"))
        ) {
            String msg = String.format("Class %s not allowed toinherit from String, Int, or Bool", ctn.getCoolClass());
            throw new CoolInvalidInheritanceException(msg);
        }

        ClassTreeNode newChild = new ClassTreeNode();
        newChild.setIdentifier(childClass.getName());
        newChild.setCoolClass(childClass);
        newChild.setParent(ctn);

        ctn.addChild(newChild);
    }

    public ClassTreeNode getRoot() {
        return root;
    }

    public ClassTreeNode find(CoolIdentifier id) throws CoolClassUndefinedException {
        // non-recursive solution here because I'm feeling lazy
        if (root.getCoolClass().getName().equals(id)) {
            return this.root;
        } else {
            Stack<ClassTreeNode> travel = new Stack<>();
            travel.addAll(this.root.getChildren());

            while (!travel.isEmpty()) {
                ClassTreeNode current = travel.pop();
                if (current.getCoolClass().getName().equals(id)) {
                    return current;
                }
                if (current.getChildren() != null) {
                    travel.addAll(current.getChildren());
                }
            }

            throw new CoolClassUndefinedException("Can't find class with name: " + id.getValue());
        }
    }

    public LinkedList<CoolClass> classChain(CoolIdentifier target) throws CoolClassUndefinedException {
        LinkedList<CoolClass> chain = new LinkedList<>();
        ClassTreeNode ctn = this.find(target);

        while (true) {
            ctn = ctn.getParent();
            if (ctn.getParent() == null) {
                chain.add(ctn.getCoolClass());
                break;
            }
            chain.add(ctn.getCoolClass());
        }

        return chain.reversed();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        return this.root.print(sb, 0);
    }

    @Override
    public Iterator<ClassTreeNode> iterator() {
        return new ClassTreeIterator(this);
    }

}
