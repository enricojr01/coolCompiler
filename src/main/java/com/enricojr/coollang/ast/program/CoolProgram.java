package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.ast.AstVisitor;
import com.enricojr.coollang.ast.builtins.CoolBooleanType;
import com.enricojr.coollang.ast.builtins.CoolIntegerType;
import com.enricojr.coollang.ast.builtins.CoolStringType;
import com.enricojr.coollang.ast.constants.CoolInteger;

import java.util.ArrayList;

public class CoolProgram extends CoolBaseNode {
    private ArrayList<CoolClass> classes;
    private CoolClass root;

    public CoolProgram() {}

    public ArrayList<CoolClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<CoolClass> classes) {
        this.classes = classes;
    }

    public CoolClass getRoot() {
        return root;
    }

    public void setRoot(CoolClass root) {
        this.root = root;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (CoolClass cc : this.classes) {
            sb.append(String.format("[%s]\n", cc));
        }

        return sb.toString();
    }

    public void accept(AstVisitor t) {
        t.visitCoolProgram(this);
    }
}
