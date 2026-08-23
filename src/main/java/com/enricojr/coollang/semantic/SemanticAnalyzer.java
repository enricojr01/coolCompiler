package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.AstPrinter;
import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolMethod;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.semantic.exceptions.*;

import java.util.ArrayList;
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

        /* not sure if this step is strictly necessary, but I do it here just to make sure that the SemanticAnalyizer
           has the copy of tree containing the symbol table. */
        /* from what I understand java passes by value, and the values are references, so it shouldn't be needed,
           but I can't remember right now. */
        ClassTreeBuilder ctb = new ClassTreeBuilder(this.prog);
        this.tree = ctb.getClassTree();

        SymbolTableBuilder stb = new SymbolTableBuilder(this.tree);
        this.tree = stb.getTree();
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
        for (ClassTreeNode child : this.tree) {
            sb = this.appendClassSymbolTable(sb, child);
            sb = this.appendMethodSymbolTable(sb, child);
        }
        sb.append("--------------\n");

        return sb.toString();
    }

    private StringBuilder appendClassSymbolTable(StringBuilder sb, ClassTreeNode ctn) {
        SymbolTable st = ctn.getCoolClass().getSymbols();
        if (ctn.getIdentifier().equals(new CoolIdentifier("String")) ||
                ctn.getIdentifier().equals(new CoolIdentifier("Bool")) ||
                ctn.getIdentifier().equals(new CoolIdentifier("Int"))
        ) {
            return sb;
        }
        if (st != null) {
            sb.append(String.format("------<%s (attributes)>-------\n", ctn.getIdentifier().getValue()));
            sb.append(st);
            sb.append(String.format("------</%s (attributes)>------\n", ctn.getIdentifier().getValue()));
        }
        return sb;
    }

    private StringBuilder appendMethodSymbolTable(StringBuilder sb, ClassTreeNode ctn) {
        ArrayList<CoolMethod> methods = ctn.getCoolClass().getMethods();
        if (ctn.getIdentifier().equals(new CoolIdentifier("String")) ||
                ctn.getIdentifier().equals(new CoolIdentifier("Bool")) ||
                ctn.getIdentifier().equals(new CoolIdentifier("Int"))
        ) {
            return sb;
        }
        if (methods != null) {
            sb.append(String.format("------<%s (methods)>-------\n", ctn.getIdentifier().getValue()));
            for (CoolMethod cm : methods) {
                sb.append(String.format("---<method: %s>----\n", cm.getName().getValue()));
                sb.append(cm.getSymbols());
                sb.append(String.format("---</method: %s>---\n", cm.getName().getValue()));
            }
            sb.append(String.format("------</%s (methods)>-------\n", ctn.getIdentifier().getValue()));
        } else {
            return sb;
        }
        return sb;
    }
}
