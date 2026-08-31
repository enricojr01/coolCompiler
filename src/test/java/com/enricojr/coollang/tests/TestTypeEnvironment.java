package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.TypeEnvironment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestTypeEnvironment {
    @Test
    public void TestTypeEnvironmentLookup() {
        TypeEnvironment te = new TypeEnvironment();
        CoolIdentifier ci = new CoolIdentifier("test1");
        CoolClass cc = new CoolClass();
        cc.setName(ci);
        te.addType(ci, cc);

        CoolClass target = te.getType(ci);
        assertNotNull(target);
    }

    @Test
    public void TestTypeEnvironmentLookupFail() {
        TypeEnvironment te = new TypeEnvironment();
        CoolIdentifier ci = new CoolIdentifier("test1");

        CoolClass target = te.getType(ci);
        assertNull(target);
    }

    @Test
    public void TestTypeEnvironmentLookupChain() {
        TypeEnvironment te1 = new TypeEnvironment();
        TypeEnvironment te2 = new TypeEnvironment();

        te2.setParent(te1);

        CoolClass cc = CoolClass.factory("test1");
        te1.addType(cc.getName(), cc);

        CoolClass target = te2.getType(cc.getName());

        assertNotNull(target);
    }
}
