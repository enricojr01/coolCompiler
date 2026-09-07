package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.program.CoolClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCoolClass {
    @Test
    public void testEqualitySubrelationCheck() {
        // NOTE: any CoolClass `a` is a subrelation (<=) to a CoolClass `b` if it is a direct
        // or indirect descendant of `b` such that a -> b -> ... -> x
        CoolClass cc1 = CoolClass.factory("test1");
        CoolClass cc2 = CoolClass.factory("test2");
        CoolClass cc3 = CoolClass.factory("test3");

        cc3.setParentName(cc2.getName());
        cc3.setParent(cc2);

        cc2.setParentName(cc1.getName());
        cc2.setParent(cc1);

        assertTrue(cc3.equalOrSubrelation(cc2));
    }

    @Test
    public void testEqualitySubrelationCheckDeep() {
        // NOTE: any CoolClass `a` is a subrelation (<=) to a CoolClass `b` if it is a direct
        // or indirect descendant of `b` such that a -> b -> ... -> x
        CoolClass cc1 = CoolClass.factory("test1");
        CoolClass cc2 = CoolClass.factory("test2");
        CoolClass cc3 = CoolClass.factory("test3");

        cc3.setParentName(cc2.getName());
        cc3.setParent(cc2);

        cc2.setParentName(cc1.getName());
        cc2.setParent(cc1);

        assertTrue(cc3.equalOrSubrelation(cc1));
    }

    @Test
    public void testEqualitySubrelationCheckFail() {
        CoolClass cc1 = CoolClass.factory("test1");
        CoolClass cc2 = CoolClass.factory("test2");
        CoolClass cc3 = CoolClass.factory("test3");

        cc3.setParentName(cc2.getName());
        cc3.setParent(cc2);

        assertFalse(cc3.equalOrSubrelation(cc1));
    }
}
