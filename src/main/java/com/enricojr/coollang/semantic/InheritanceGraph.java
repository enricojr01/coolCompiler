package com.enricojr.coollang.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.exceptions.ClassDefinedTwiceException;
import com.enricojr.coollang.semantic.exceptions.CoolClassInheritanceCycleException;
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
    private final HashMap<CoolIdentifier, LinkedList<CoolClass>> graph = new HashMap<>();
    private final CoolIdentifier objectIdentifier = new CoolIdentifier("Object");

    public InheritanceGraph() {
        CoolClass objClass = new CoolClass();
        objClass.setName(this.objectIdentifier);

        CoolClass ioClass = new CoolClass();
        CoolIdentifier ioIdentifier = new CoolIdentifier("IO");
        ioClass.setName(ioIdentifier);

        graph.put(this.objectIdentifier, new LinkedList<>());
        graph.put(ioIdentifier, new LinkedList<>());
        graph.get(this.objectIdentifier).add(ioClass);
    }

    public void addClassKey(CoolClass cc) throws ClassDefinedTwiceException {
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
            LinkedList<CoolClass> target = this.graph.get(cc.getParentName());
            if (target == null) {
                throw new CoolClassUndefinedException(cc.getParentName().getValue());
            } else {
                target.add(cc);
            }
        }
    }

    public boolean hasCycles() throws CoolClassInheritanceCycleException {
        Stack<CoolClass> travel = new Stack<>();
        ArrayList<CoolClass> seen = new ArrayList<>();
        for (CoolIdentifier ci : this.graph.keySet()) {
            travel.addAll(this.graph.get(ci));
        }

        while (!travel.isEmpty()) {
            CoolClass next = travel.pop();
            for (CoolClass cc : seen) {
                if (next.getParentName().equals(cc.getName())) {
                    throw new CoolClassInheritanceCycleException("Inheritance cycle detected at class: " + cc.getName());
                }
            }
            seen.add(next);
        }

        return false;
    }

    public String rawAdjacencyList() {
        StringBuilder sb = new StringBuilder();
        LinkedList<CoolClass> top = this.graph.get(this.objectIdentifier);
        sb.append(String.format("%s: %s\n", this.objectIdentifier, top));
        for (CoolIdentifier ci : this.graph.keySet()) {
            if (!ci.getValue().equals("Object")) {
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
