package com.enricojr.coollang.semantic;

import java.util.*;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.semantic.exceptions.*;

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
    private final HashMap<CoolIdentifier, LinkedList<CoolClass>> chains = new HashMap<>();
    private final CoolIdentifier objectIdentifier = new CoolIdentifier("Object");
    private final CoolProgram prog;

    private final CoolClass objClass;
    private final CoolClass ioClass;
    private final CoolClass strClass;
    private final CoolClass intClass;
    private final CoolClass boolClass;

    public InheritanceGraph(CoolProgram prog) throws
            InvalidClassNameException, ClassDefinedTwiceException, CoolClassInheritanceCycleException {
        this.prog = prog;
        CoolClass objClass = new CoolClass();
        objClass.setName(this.objectIdentifier);
        this.objClass = objClass;

        // IO, Int, Bool, and String are the built-in classes.
        // The following restrictions will need to be enforced somewhere:
        // - You can inherit from IO but not redefine any of its methods, nor can you
        //   redefine IO itself.
        // - You cannot inherit from or redefine Int.
        // - You cannot inherit from or redefine Bool.
        // - You cannot inherit from or redefine String.
        CoolClass ioClass = new CoolClass();
        CoolIdentifier ioIdentifier = new CoolIdentifier("IO");
        ioClass.setName(ioIdentifier);
        ioClass.setParentName(this.objectIdentifier);
        this.ioClass = ioClass;

        CoolClass intClass = new CoolClass();
        CoolIdentifier intIdentifier = new CoolIdentifier("Int");
        intClass.setName(intIdentifier);
        intClass.setParentName(this.objectIdentifier);
        this.intClass = intClass;

        CoolClass strClass = new CoolClass();
        CoolIdentifier strIdentifier = new CoolIdentifier("String");
        strClass.setName(strIdentifier);
        strClass.setParentName(this.objectIdentifier);
        this.strClass = strClass;

        CoolClass boolClass = new CoolClass();
        CoolIdentifier boolIdentifier = new CoolIdentifier("Bool");
        boolClass.setName(boolIdentifier);
        boolClass.setParentName(this.objectIdentifier);
        this.boolClass = boolClass;

        graph.put(this.objectIdentifier, new LinkedList<>());
        // you can inherit from the IO class
        graph.put(ioIdentifier, new LinkedList<>());
        graph.get(this.objectIdentifier).add(ioClass);
        // you cannot inherit from int, bool or string
        graph.get(this.objectIdentifier).add(intClass);
        graph.get(this.objectIdentifier).add(boolClass);
        graph.get(this.objectIdentifier).add(strClass);

        this.buildGraph();
        this.buildChains();
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

    private void buildGraph() throws
            InvalidClassNameException, ClassDefinedTwiceException, CoolClassInheritanceCycleException {
        ArrayList<CoolClass> classes = this.prog.getClasses();
        for (CoolClass cc : classes) {
            String className = cc.getName().getValue();
            String parentName = null;

            if (cc.getParentName() != null) {
                parentName = cc.getParentName().getValue();
                if (parentName.equals("Int") || parentName.equals("Bool") || parentName.equals("String")) {
                    throw new InvalidClassNameException("Cannot inherit from class Int, Bool, or String");
                }
                if (parentName.equals(className)) {
                    throw new CoolClassInheritanceCycleException("Classes cannot inherit from themselves.");
                }
            }

            if (className.equals("Int") || className.equals("Bool") || className.equals("String")) {
                throw new InvalidClassNameException("Cannot name class Int, Bool, or String.");
            }

            this.addClassKey(cc);
        }

        for (CoolClass cc : classes) {
            try {
                this.addChildren(cc);
            } catch (CoolClassUndefinedException e) {
                System.out.println("Class undefined: " + e);
                System.exit(1);
            }
        }
    }

    private void buildChains() {
        LinkedList<CoolClass> top = this.graph.get(this.objectIdentifier);
        for (CoolClass cc : top) {
            // put empty chain under cc.getName()
            this.chains.put(cc.getName(), new LinkedList<>());
            // add the object at the start
            this.chains.get(cc.getName()).add(this.objClass);
            // nav through the neighbors of cc
            Stack<CoolClass> travel = new Stack<>();
            LinkedList<CoolClass> neighbors = this.graph.get(cc.getName());
            if (neighbors != null) {
                travel.addAll(neighbors);
            }
            while (!travel.isEmpty()) {
                CoolClass next = travel.pop();
                travel.addAll(this.graph.get(next.getName()));
                this.chains.get(cc.getName()).add(next);
            }
        }
    }

    public HashMap<CoolIdentifier, LinkedList<CoolClass>> getGraph() {
        return this.graph;
    }

    public HashMap<CoolIdentifier, LinkedList<CoolClass>> getChains() {
        return this.chains;
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
            while(!travel.isEmpty()) {
                CoolClass next = travel.pop();
                sb.append(String.format("-> %s ", next));
                travel.addAll(this.graph.get(next.getName()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
