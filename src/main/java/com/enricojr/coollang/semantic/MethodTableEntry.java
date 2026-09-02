package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolMethod;

import java.util.Map;

public class MethodTableEntry {
    private CoolIdentifier name;
    private SymbolTable inputs = new SymbolTable();
    private CoolClass output;
    private CoolMethod methodObj;

    public MethodTableEntry() {}

    public void addInput(CoolIdentifier ci, CoolClass cc) {
        this.inputs.addType(ci, cc) ;
    }

    public CoolClass getInput(CoolIdentifier ci) {
        return this.inputs.getType(ci);
    }

    public CoolIdentifier getName() {
        return name;
    }

    public void setName(CoolIdentifier name) {
        this.name = name;
    }

    public SymbolTable getInputs() {
        return inputs;
    }

    public void setInputs(SymbolTable inputs) {
        this.inputs = inputs;
    }

    public CoolClass getOutput() {
        return output;
    }

    public void setOutput(CoolClass output) {
        this.output = output;
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

        for (Map.Entry<CoolIdentifier, CoolClass> entry : this.inputs.getTypes().entrySet())  {
            sb.append(String.format("%s : %s, ", entry.getKey().getValue(), entry.getValue().getName().getValue()));
        }
        sb.append(") -> " + this.output.getName().getValue());
        return sb.toString();
    }
}
