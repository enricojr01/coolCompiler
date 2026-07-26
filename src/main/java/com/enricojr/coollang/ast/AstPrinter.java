package com.enricojr.coollang.ast;

import java.util.ArrayList;
import com.enricojr.coollang.ast.expressions.CoolExpr;
import com.enricojr.coollang.ast.program.CoolAttribute;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolMethod;
import com.enricojr.coollang.ast.program.CoolProgram;

public class AstPrinter {
    private CoolProgram startNode;

    public AstPrinter() {} 
    public AstPrinter(CoolProgram cprog) {
        this.startNode = cprog;
    }

    public CoolProgram getStartNode() {
        return startNode;
    }
    public void setStartNode(CoolProgram startNode) {
        this.startNode = startNode;
    }

    // TODO: make a proper tree ffs this is insane you'd think I'd be better than this after 10 yrs.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ArrayList<CoolClass> classes = this.startNode.getClasses();

        for (CoolClass cc : classes) {
            sb.append(cc);
            ArrayList<CoolAttribute> attributes = cc.getAttributes();
            ArrayList<CoolMethod> methods = cc.getMethods();

            for (CoolAttribute ca : attributes) {
                sb.append("  " + ca + "\n");
            }

            for (CoolMethod cm : methods) {
                sb.append("  " + cm + "\n");
                ArrayList<CoolExpr> expressions = cm.getExpressions();
                for (CoolExpr ce : expressions) {
                    sb.append("    " + ce + "\n");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
