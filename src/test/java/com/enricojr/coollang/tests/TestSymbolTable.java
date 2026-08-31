package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolBaseNode;
import com.enricojr.coollang.semantic.SymbolTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestSymbolTable {
    @Test
    public void TestSymbolLookup() {
        SymbolTable st1 = new SymbolTable();

        CoolIdentifier ci = new CoolIdentifier("test1");
        st1.addSymbol(ci, new CoolBaseNode());

        CoolBaseNode target = st1.getSymbol(ci);
        assertNotNull(target);
    }

    @Test
    public void TestSymbolLookupFail() {
        SymbolTable st1 = new SymbolTable();

        CoolIdentifier ci = new CoolIdentifier("test1");

        CoolBaseNode target = st1.getSymbol(ci);
        assertNotNull(target);
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

        st1.addSymbol(ci1, new CoolBaseNode());
        st2.addSymbol(ci2, new CoolBaseNode());
        st3.addSymbol(ci3, new CoolBaseNode());;

        CoolBaseNode target = st3.getSymbol(ci1);
        assertNotNull(target);

        target = st3.getSymbol(ci2);
        assertNotNull(target);
    }
}
