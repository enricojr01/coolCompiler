package com.enricojr.coollang.ast.program;

import com.enricojr.coollang.semantic.SymbolTable;
import com.enricojr.coollang.semantic.TypeEnvironment;

import java.util.ArrayList;

public class CoolProgram extends CoolBaseNode {
    private ArrayList<CoolClass> classes;
    private TypeEnvironment typeEnvironment;

    public CoolProgram() {}

    public ArrayList<CoolClass> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<CoolClass> classes) {
        this.classes = classes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (CoolClass cc : this.classes) {
            sb.append(String.format("[%s]\n", cc));
        }

        return sb.toString();
    }

    public boolean findClass(String name) {
        for (CoolClass cc : this.classes) {
            if (cc.getName().getValue().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public TypeEnvironment getTypeEnvironment() {
        return typeEnvironment;
    }
}
