package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

import java.util.HashMap;

public class TypeEnvironment {
    private HashMap<CoolIdentifier, CoolClass> environment;

    public TypeEnvironment() {}

    public HashMap<CoolIdentifier, CoolClass> getEnvironment() {
        return this.environment;
    }

    public void addType(CoolIdentifier ci, CoolClass cc) {
        this.environment.put(ci, cc);
    }

    public boolean hasType(CoolIdentifier ci) {
        return this.environment.containsKey(ci);
    }

    public CoolClass getType(CoolIdentifier ci) {
        return this.environment.get(ci);
    }
}
