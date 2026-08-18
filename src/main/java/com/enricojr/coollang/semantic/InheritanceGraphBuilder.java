package com.enricojr.coollang.semantic;

import java.util.ArrayList;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.semantic.exceptions.ClassDefinedTwiceException;
import com.enricojr.coollang.semantic.exceptions.CoolClassUndefinedException;
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

    private void buildGraph() throws InvalidClassNameException, ClassDefinedTwiceException {
        ArrayList<CoolClass> classes = this.prog.getClasses();
        for (CoolClass cc : classes) {
            String className = cc.getName().getValue();
            String parentName = null;

            if (cc.getParentName() != null) {
                parentName = cc.getParentName().getValue();
                if (parentName.equals("Int") || parentName.equals("Bool") || parentName.equals("String")) {
                    throw new InvalidClassNameException("Cannot inherit from class Int, Bool, or String");
                }
            }

            if (className.equals("Int") || className.equals("Bool") || className.equals("String")) {
                throw new InvalidClassNameException("Cannot name class Int, Bool, or String.");
            }


            this.graph.addClassKey(cc);
        }

        for (CoolClass cc : classes) {
            try {
                this.graph.addChildren(cc);
            } catch (CoolClassUndefinedException e) {
                System.out.println("Class undefined: " + e);
                System.exit(1);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----Adjacency List----\n");
        sb.append(this.graph.rawAdjacencyList() + "\n");
        sb.append("----Graph----\n");
        sb.append(this.graph.toString() + "\n");
        return sb.toString();
    }
}
