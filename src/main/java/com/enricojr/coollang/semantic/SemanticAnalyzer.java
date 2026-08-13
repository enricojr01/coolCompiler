package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.program.CoolProgram;

/* 
semantic analysis needs to do several things, maybe not in this order: 
- inheritance
- naming and scoping
- type checking
- provide a code generator interface
*/
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

public class SemanticAnalyzer {
    private CoolProgram prog;

    public SemanticAnalyzer(CoolProgram cprog) {
        this.prog = cprog;
    }

    public boolean hasValidInheritanceGraph() {
        // InheritanceGraph ig = new InheritanceGraph();
        // try {
        //     for (CoolClass cc : prog.getClasses()) {
        //         ig.addClass(cc);
        //     }
        // } catch (ParentClassNotDefinedException e) {
        //     System.out.println(e.getMessage());
        //     return false;
        // } catch (ClassDefinedTwiceException e) {
        //     System.out.println(e.getMessage());
        //     return false;
        // }



        return true;
    }


}
