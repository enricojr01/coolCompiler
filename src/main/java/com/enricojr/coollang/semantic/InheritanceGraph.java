package com.enricojr.coollang.semantic;

import java.util.HashMap;
import java.util.LinkedList;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.exceptions.ClassDefinedTwiceException;
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

    public InheritanceGraph() {
        CoolClass objClass = new CoolClass();
        objClass.setName(this.objectIdentifier);
        graph.put(this.objectIdentifier, new LinkedList<>()); 
    }

    public void addClass(CoolClass cc) throws 
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
            CoolIdentifier className = cc.getName();
            LinkedList<CoolClass> emptyList = new LinkedList<>();
            this.graph.put(className, emptyList);
        }

        if (cc.getParent() != null) {
            CoolClass parent = cc.getParent();
            CoolIdentifier parentName = parent.getName();

            if (this.graph.containsKey(parentName)) {
                this.graph.get(parent.getName()).add(cc);
            } else {
                throw new ParentClassNotDefinedException();
            }
        } else {
            this.graph.get(this.objectIdentifier).add(cc);
        }
    }
}
