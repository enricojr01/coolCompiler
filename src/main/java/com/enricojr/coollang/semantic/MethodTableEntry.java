package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolMethod;

import java.util.Map;

public class MethodTableEntry {
    private CoolIdentifier name;
    private SymbolTable parameters = new SymbolTable();
    private CoolClass returnType;
    private CoolMethod methodObj;

    public MethodTableEntry() {}

//    public void addInput(CoolIdentifier ci, CoolClass cc) {
//        this.parameters.addType(ci, cc);
//    }
//
//    public SymbolTableEntry getInput(CoolIdentifier ci) {
//        return this.parameters.getType(ci);
//    }

    public CoolIdentifier getName() {
        return name;
    }

    public void setName(CoolIdentifier name) {
        this.name = name;
    }

    public SymbolTable getParameters() {
        return parameters;
    }

    public void setParameters(SymbolTable parameters) {
        this.parameters = parameters;
    }

    public CoolClass getReturnType() {
        return returnType;
    }

    public void setReturnType(CoolClass returnType) {
        this.returnType = returnType;
    }

    public CoolMethod getMethodObj() {
        return methodObj;
    }

    public void setMethodObj(CoolMethod methodObj) {
        this.methodObj = methodObj;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name.getValue()).append("(");

        for (Map.Entry<CoolIdentifier, SymbolTableEntry> entry : this.parameters.getTypes().entrySet())  {
            sb.append(String.format("%s : %s, ", entry.getKey().getValue(), entry.getValue().getTypeString()));
        }
        sb.append(") -> " + this.returnType.getName().getValue());
        return sb.toString();
    }
}
