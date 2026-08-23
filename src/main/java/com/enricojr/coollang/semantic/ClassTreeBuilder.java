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
public class ClassTreeBuilder {
    private CoolProgram prog;
    private ClassTree tree = new ClassTree();

    public ClassTreeBuilder(CoolProgram prog) throws
            CoolParentUndefinedException, CoolInvalidInheritanceException {
        this.prog = prog;

        // IO, Int, Bool, and String are the built-in classes.
        // The following restrictions will need to be enforced somewhere:
        // - You can inherit from IO but not redefine any of its methods, nor can you
        //   redefine IO itself.
        // - You cannot inherit from or redefine Int.
        // - You cannot inherit from or redefine Bool.
        // - You cannot inherit from or redefine String.
        this.buildTree();
    }

    public void addClass(CoolClass cc) throws CoolParentUndefinedException, CoolInvalidInheritanceException {
        CoolIdentifier parent = cc.getParentName();

        if (parent == null) {
            parent = new CoolIdentifier("Object");
        }

        this.tree.addChild(parent, cc);
    }

    public LinkedList<CoolClass> buildChain(CoolIdentifier id) throws CoolClassUndefinedException {
        System.out.println("Building chain for " + id);
        return this.tree.classChain(id);
    }

    private void buildTree() throws CoolParentUndefinedException, CoolInvalidInheritanceException {
        ArrayList<CoolClass> classes = this.prog.getClasses();

        for (CoolClass cc : classes) {
            this.addClass(cc);
        }
    }

    public ClassTree getClassTree() {
        return this.tree;
    }

    public String toString() {
        return tree.toString();
    }
}
