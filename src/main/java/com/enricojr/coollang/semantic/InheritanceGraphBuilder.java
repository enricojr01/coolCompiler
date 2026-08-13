package com.enricojr.coollang.semantic;

import java.util.ArrayList;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.semantic.exceptions.ClassDefinedTwiceException;
import com.enricojr.coollang.semantic.exceptions.InvalidClassNameException;
import com.enricojr.coollang.semantic.exceptions.ParentClassNotDefinedException;

public class InheritanceGraphBuilder {
    private CoolProgram prog;
    private InheritanceGraph graph;

    public InheritanceGraphBuilder(CoolProgram prog) throws 
        InvalidClassNameException, 
        ClassDefinedTwiceException, 
        ParentClassNotDefinedException {
        this.prog = prog;
        this.graph = new InheritanceGraph();
        this.buildGraph();
    }

    private void buildGraph() throws 
        InvalidClassNameException, 
        ClassDefinedTwiceException, 
        ParentClassNotDefinedException {
        ArrayList<CoolClass> classes = this.prog.getClasses();
        for (CoolClass cc : classes) {
            String className = cc.getName().getValue();
            if (
                className.equals("Int") 
                || className.equals("Bool") 
                || className.equals("String")
            ) {
                throw new InvalidClassNameException("Cannot name class Int, Bool, or String.");
            }
            this.graph.addClassKey(cc);
        }

        for (CoolClass cc : classes) {
            this.graph.addChildren(cc);
        }
    }

    public String toString() {
        return this.graph.toString();
    }
}
