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

    public String codeGenerate() {
        StringBuilder sb = new StringBuilder();
        sb.append(".data\n");
        sb.append(".align 2\n");
        sb.append(".globl class_mainTab\n");
        sb.append(".globl Main_protObj\n");
        sb.append(".globl Int_protObj\n");
        sb.append(".globl String_protObj\n");
        sb.append(".globl bool_const0\n");
        sb.append(".globl bool_const1\n");
        sb.append(".globl _int_tag\n");
        sb.append(".globl _bool_tag\n");
        sb.append(".globl _string_tag\n");
        sb.append("_int_tag:\n");
        sb.append(".word ").append(CoolIntegerType.getMipsTag()).append("\n");
        sb.append("_bool_tag:\n");
        sb.append(".word ").append(CoolBooleanType.getMipsTag()).append("\n");
        sb.append("_string_tag:\n");
        sb.append(".word ").append(CoolStringType.getMipsTag()).append("\n");
        sb.append(".globl _MemMgr_INITIALIZER\n");
        sb.append("_MemMgr_INITIALIZER:\n");
        sb.append(".word _NoGC_Init\n");
        sb.append(".globl _MemMgr_COLLECTOR\n");
        sb.append("._MemMgr_COLLECTOR:\n");
        sb.append(".word _NoGC_Collect\n");
        sb.append(".globl _MemMgr_TEST\n");
        sb.append("_MemMgr_TEST:\n");
        sb.append(".word 0\n");
        sb.append(".word -1\n");

        // every string constant + one for what I'm presuming is an empty string
        // every integer constant

        return sb.toString();
    }
}
