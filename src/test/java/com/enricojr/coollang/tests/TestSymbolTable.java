package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.semantic.SymbolTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestSymbolTable {
    @Test
    public void TestSymbolLookup() {
        SymbolTable st1 = new SymbolTable();

        CoolIdentifier ci = new CoolIdentifier("test1");
        st1.addType(ci, new CoolClass());

        CoolBaseNode target = st1.getType(ci);
        assertNotNull(target);
    }

    @Test
    public void TestSymbolLookupFail() {
        SymbolTable st1 = new SymbolTable();

        CoolIdentifier ci = new CoolIdentifier("test1");

        CoolBaseNode target = st1.getType(ci);
        assertNull(target);
    }

    @Test
    public void TestSymbolTableLookupchain() {
        SymbolTable st1 = new SymbolTable();
        SymbolTable st2 = new SymbolTable();
        SymbolTable st3 = new SymbolTable();

        st2.setParent(st1);
        st3.setParent(st2);

        CoolIdentifier ci1 = new CoolIdentifier("test1");
        CoolIdentifier ci2 = new CoolIdentifier("test2");
        CoolIdentifier ci3 = new CoolIdentifier("test3");

        st1.addType(ci1, new CoolClass());
        st2.addType(ci2, new CoolClass());
        st3.addType(ci3, new CoolClass());;

        CoolBaseNode target = st3.getType(ci1);
        assertNotNull(target);

        target = st3.getType(ci2);
        assertNotNull(target);
    }

    @Test
    public void TestTypeLookupSimple() {
        SymbolTable st1 = new SymbolTable();

        CoolClass cc1 = CoolClass.factory("test1");

        st1.addType(cc1.getName(), cc1);

        CoolClass target = st1.getType(cc1.getName());

        assertNotNull(target);
    }

    @Test
    public void TestTypeLookupDeep() {
        SymbolTable st1 = new SymbolTable();
        SymbolTable st2 = new SymbolTable();

        st2.setParent(st1);

        CoolClass cc1 = CoolClass.factory("test1");

        st1.addType(cc1.getName(), cc1);

        CoolClass target = st2.getType(cc1.getName());
        assertNotNull(target);

    }

    @Test
    public void TestTypeLookupDeeper() {
        SymbolTable st1 = new SymbolTable();
        SymbolTable st2 = new SymbolTable();
        SymbolTable st3 = new SymbolTable();

        st2.setParent(st1);
        st3.setParent(st2);

        CoolClass cc1 = CoolClass.factory("Test1");
        st1.addType(cc1.getName(), cc1);

        CoolClass target = st3.getType(cc1.getName());
        assertNotNull(target);
    }
}
