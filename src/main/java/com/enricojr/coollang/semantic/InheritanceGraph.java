package com.enricojr.coollang.semantic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.exceptions.ClassDefinedTwiceException;
import com.enricojr.coollang.semantic.exceptions.CoolClassUndefinedException;
import com.enricojr.coollang.semantic.exceptions.ParentClassNotDefinedException;

/* 
the following restrictions on inheritance apply:
- must inherit from a class ABOVE in the hierarchy
- classes you're inheriting from must exist
- inheritance must not be cyclic
- only single inheritance allowed
- Object is the root of the inheritance graph
- if no parent is specified, then the class inherits from Object.
- you can inherit from the IO class, but not redefine it. 
- cannot inherit from, or redefine, Int.
- cannot inherit from, or redefine, Bool.
- cannot inherit from, or redefine, String.

let A, C, P be types:
- A <= (conforms to) A for all types A
- if C inherits from P then C <= P
- if A <= C and C <= P then A <= P
*/
public class InheritanceGraph {
    private HashMap<CoolIdentifier, LinkedList<CoolClass>> graph = new HashMap<>();
    private CoolIdentifier objectIdentifier = new CoolIdentifier("Object");
    private CoolIdentifier ioIdentifier = new CoolIdentifier("IO");
    private CoolClass objectClass;
    private CoolClass ioClass;

    public InheritanceGraph() {
        CoolClass objClass = new CoolClass();
        objClass.setName(this.objectIdentifier);

        CoolClass ioClass = new CoolClass();
        ioClass.setName(this.ioIdentifier);

        this.objectClass = objClass;
        this.ioClass = ioClass;

        graph.put(this.objectIdentifier, new LinkedList<>()); 
        graph.put(this.ioIdentifier, new LinkedList<>());
        graph.get(this.objectIdentifier).add(ioClass);
    }

    public void oldAddClass(CoolClass cc) throws 
        ClassDefinedTwiceException, 
        ParentClassNotDefinedException {
        // two things need to happen:
        // 1. an key for the new class needs to be added to the graph
        // 2. the class itself needs to be added to the linkedlist of its parent OR
        //    to the linkedlist of Object, if there is no parent defined
        if (this.graph.containsKey(cc.getName())) {
            // TODO: better error messages
            throw new ClassDefinedTwiceException(cc.getName().getValue());
        } else {
            System.out.println(String.format("Adding %s to graph...", cc.getName().getValue()));
            CoolIdentifier className = cc.getName();
            LinkedList<CoolClass> emptyList = new LinkedList<>();
            this.graph.put(className, emptyList);
        }

        if (cc.getParentName() != null) {
            CoolIdentifier parentName = cc.getParentName();
            System.out.println(String.format("%s extends %s", cc.getName().getValue(), parentName));

            if (this.graph.containsKey(parentName)) {
                System.out.println("Found parent class!");
                this.graph.get(parentName).add(cc);
            } else {
                throw new ParentClassNotDefinedException();
            }
        } else {
            System.out.println("No parent class defined, therefore it extends Object");
            this.graph.get(this.objectIdentifier).add(cc);
        }
    }

    public void addClassKey(CoolClass cc) throws ClassDefinedTwiceException {
        System.out.println(String.format("Adding class: %s", cc));
        if (this.graph.containsKey(cc.getName())) {
            throw new ClassDefinedTwiceException(cc.getName().getValue());
        }
        this.graph.put(cc.getName(), new LinkedList<>());
        if (cc.getParentName() == null) {
            this.graph.get(this.objectIdentifier).add(cc);
        }
    }

    public void addChildren(CoolClass cc) throws CoolClassUndefinedException {
        if (cc.getParentName() != null) {
            System.out.println(
                String.format("Class %s extends ", cc.getName(), cc.getParentName())
            );
            LinkedList<CoolClass> target = this.graph.get(cc.getParentName());
            if (target == null) {
                throw new CoolClassUndefinedException(cc.getParentName().getValue());
            } else {
                target.add(cc);
            }
        } else {
            System.out.println(String.format("Class %s extends Object", cc.getName()));
        }
    }

    public String rawAdjacencyList() {
        StringBuilder sb = new StringBuilder();
        LinkedList<CoolClass> top = this.graph.get(this.objectIdentifier);
        sb.append(String.format("%s: %s\n", this.objectIdentifier, top));
        for (CoolIdentifier ci : this.graph.keySet()) {
            if (ci.getValue().equals("Object") == false) {
                sb.append(String.format("%s: %s\n", ci, this.graph.get(ci)));
            }
        }

        return sb.toString();

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        LinkedList<CoolClass> top = this.graph.get(this.objectIdentifier);
        for (CoolClass cc : top) {
            sb.append(String.format("Object -> %s ", cc));
            Stack<CoolClass> travel = new Stack<>();
            travel.addAll(this.graph.get(cc.getName()));
            while(travel.isEmpty() == false) {
                CoolClass next = travel.pop();
                sb.append(String.format("-> %s ", next));
                travel.addAll(this.graph.get(next.getName()));
            }
            sb.append("\n");
        }
        return sb.toString();
        // return this.rawAdjacencyList();
    }
}
