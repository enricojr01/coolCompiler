package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolFormal;
import com.enricojr.coollang.ast.program.CoolParamList;
import com.enricojr.coollang.semantic.SymbolTable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestSymbolTable {
    @Test
    public void TestSymbolLookup() {
        SymbolTable st1 = new SymbolTable();

        CoolIdentifier ci = new CoolIdentifier("test1");
        st1.addSymbolType(ci, new CoolClass());

        CoolBaseNode target = st1.getSymbolType(ci);
        assertNotNull(target);
    }

    @Test
    public void TestSymbolLookupFail() {
        SymbolTable st1 = new SymbolTable();

        CoolIdentifier ci = new CoolIdentifier("test1");

        CoolBaseNode target = st1.getSymbolType(ci);
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

        st1.addSymbolType(ci1, new CoolClass());
        st2.addSymbolType(ci2, new CoolClass());
        st3.addSymbolType(ci3, new CoolClass());;

        CoolBaseNode target = st3.getSymbolType(ci1);
        assertNotNull(target);

        target = st3.getSymbolType(ci2);
        assertNotNull(target);
    }

    @Test
    public void TestTypeLookupSimple() {
        SymbolTable st1 = new SymbolTable();

        CoolClass cc1 = CoolClass.factory("test1");

        st1.addSymbolType(cc1.getName(), cc1);

        CoolClass target = st1.getSymbolType(cc1.getName());

        assertNotNull(target);
    }

    @Test
    public void TestTypeLookupDeep() {
        SymbolTable st1 = new SymbolTable();
        SymbolTable st2 = new SymbolTable();

        st2.setParent(st1);

        CoolClass cc1 = CoolClass.factory("test1");

        st1.addSymbolType(cc1.getName(), cc1);

        CoolClass target = st2.getSymbolType(cc1.getName());
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
        st1.addSymbolType(cc1.getName(), cc1);

        CoolClass target = st3.getSymbolType(cc1.getName());
        assertNotNull(target);
    }

    @Test
    public void TestMethodLookup() {
        SymbolTable st1 = new SymbolTable();

        ArrayList<CoolFormal> parameters = new ArrayList<>();
        CoolFormal cf1 = new CoolFormal();
        cf1.setName(new CoolIdentifier("test1"));
        cf1.setType(new CoolIdentifier("Bool"));

        CoolParamList cpl = new CoolParamList();
    }
}
