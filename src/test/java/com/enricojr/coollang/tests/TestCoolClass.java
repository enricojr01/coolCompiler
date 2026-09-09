package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.builtins.CoolObjectType;
import com.enricojr.coollang.ast.program.CoolClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testLeastCommonAncestorPass() {
        CoolClass cc1 = CoolClass.factory("test1");
        CoolClass cc2 = CoolClass.factory("test2");
        CoolClass cc3 = CoolClass.factory("test3");

        cc1.setParent(cc3);
        cc1.setParentName(cc3.getName());
        cc2.setParent(cc3);
        cc2.setParentName(cc3.getName());

        CoolClass result = CoolClass.leastCommonAncestor(cc1, cc2);

        assertTrue(result.equals(cc3));
    }

    @Test
    public void testLeastCommonAncestorFail() {
        CoolObjectType root = new CoolObjectType();

        CoolClass cc1 = CoolClass.factory("test1");
        CoolClass cc2 = CoolClass.factory("test2");
        CoolClass cc3 = CoolClass.factory("test3");

        CoolClass result = CoolClass.leastCommonAncestor(cc1, cc2);
        assertNull(result);
    }

    @Test
    public void testLeastCommonAncestorRoot() {
        CoolObjectType root = new CoolObjectType();

        CoolClass cc1 = CoolClass.factory("test1");
        CoolClass cc2 = CoolClass.factory("test2");
        CoolClass cc3 = CoolClass.factory("test3");

        cc3.setParent(root);
        cc3.setParentName(root.getName());

        cc1.setParent(root);
        cc1.setParentName(root.getName());

        cc2.setParent(root);
        cc2.setParentName(root.getName());

        CoolClass result = CoolClass.leastCommonAncestor(cc1, cc2);
        assertTrue(result.equals(root));
    }

}
