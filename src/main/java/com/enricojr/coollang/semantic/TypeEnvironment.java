package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

import java.util.HashMap;

public class TypeEnvironment {
    private HashMap<CoolIdentifier, CoolClass> environment;

    public TypeEnvironment() {}

    public HashMap<CoolIdentifier, CoolClass> getEnvironment() {
        return environment;
    }

    public void addType(CoolIdentifier ci, CoolClass cc) {
        environment.put(ci, cc);
    }

    public CoolClass getType(CoolIdentifier ci) {
        return environment.get(ci);
    }
}
