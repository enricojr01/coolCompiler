package com.enricojr.coollang.semantic;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;

import java.util.HashMap;

public class TypeEnvironment {
    private HashMap<CoolIdentifier, CoolClass> environment;
    private TypeEnvironment parent;

    public TypeEnvironment() {}

    public void setEnvironment(HashMap<CoolIdentifier, CoolClass> environment) {
        this.environment = environment;
    }

    public TypeEnvironment getParent() {
        return this.parent;
    }

    public void setParent(TypeEnvironment previous) {
        this.parent = previous;
    }

    public HashMap<CoolIdentifier, CoolClass> getEnvironment() {
        return this.environment;
    }

    public void addType(CoolIdentifier ci, CoolClass cc) {
        this.environment.put(ci, cc);
    }

    public boolean hasType(CoolIdentifier ci) {
        if (this.environment.containsKey(ci)) {
            return true;
        }

        if (this.parent != null) {
            TypeEnvironment next = this.parent;
            while (true) {
                if (next.hasType(ci)) {
                    return true;
                } else if (next.getParent() == null) {
                    break;
                } else {
                    next = next.getParent();
                }
            }
        }

        return false;
    }

    public CoolClass getType(CoolIdentifier ci) {
        if (this.environment.containsKey(ci)) {
            return this.environment.get(ci);
        }

        if (this.parent != null) {
            TypeEnvironment next = this.parent;
            while (true) {
                if (next.hasType(ci)) {
                    return next.getType(ci);
                } else if (next.getParent() == null) {
                    break;
                } else {
                    next = next.getParent();
                }
            }
        }
        return null;
    }
}
