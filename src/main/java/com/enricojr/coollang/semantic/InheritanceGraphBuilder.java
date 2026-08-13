package com.enricojr.coollang.semantic;

import java.util.ArrayList;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.semantic.exceptions.InvalidClassNameException;

public class InheritanceGraphBuilder {
    private CoolProgram prog;

    public InheritanceGraphBuilder(CoolProgram prog) throws InvalidClassNameException {
        this.prog = prog;
        this.buildGraph();
    }

    private void buildGraph() throws InvalidClassNameException {
        ArrayList<CoolClass> classes = new ArrayList<>();
        for (CoolClass cc : classes) {
            String className = cc.getName().getValue();
            String parentName = cc.getParentName().getValue();
            if (
                className.equals("Int") 
                || className.equals("Bool") 
                || className.equals("String")
            ) {
                throw new InvalidClassNameException("Cannot name class Int, Bool, or String.");
            }
            
        }
    }
}
