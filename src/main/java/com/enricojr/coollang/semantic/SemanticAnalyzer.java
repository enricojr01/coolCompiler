package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstPrinter;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.semantic.exceptions.*;

import java.util.HashMap;
import java.util.LinkedList;

/* 
semantic analysis needs to do several things, maybe not in this order: 
- inheritance
- naming and scoping
- type checking
- provide a code generator interface
*/

public class SemanticAnalyzer {
    private CoolProgram prog;
    private ClassTree tree;

    public SemanticAnalyzer(CoolProgram prog) throws
            CoolParentUndefinedException, CoolInvalidInheritanceException, CoolClassUndefinedException {
        this.prog = prog;

        SymbolTableBuilder stb = new SymbolTableBuilder(this.prog);
        /* not sure if this step is strictly necessary, but I do it here just to make sure that the SemanticAnalyizer
           has the copy of prog containing the symbol table. */
        /* from what I understand java passes by value, and the values are references, so it shouldn't be needed,
           but I can't remember right now. */
        this.prog = stb.getProg();

        /* I'm not comfortable with the class-global mutation of this.prog but I don't see a better option right now.
           I do it here after the symbol tables to make sure that the objects in the inheritance graph have the symbol
           tables. */
        /* Later, I expect to just be able to iterate down any given chain and evaluate stuff. */
        ClassTreeBuilder ctb = new ClassTreeBuilder(this.prog);
        this.tree = ctb.getClassTree();
    }

    public String reportInheritanceTree() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inheritance Tree:\n");
        sb.append("-------------------\n");
        sb.append(this.tree);
        sb.append("-------------------\n");
        sb.append("\n");
        return sb.toString();
    }

    public String reportInheritanceChains() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inheritance Chains:\n");
        sb.append("--------------------\n");
        sb.append("--------------------\n");
        sb.append("\n");
        return sb.toString();

    }

    public String reportAbstractSyntaxTree() {
        AstPrinter ap = new AstPrinter(this.prog);
        return ap + "\n";
    }

    public String reportSymbolTables() {
        StringBuilder sb = new StringBuilder();
        sb.append("Symbol Tables:\n");
        sb.append("--------------\n");
        sb.append("--------------\n");

        return sb.toString();
    }
}
