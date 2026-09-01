package com.enricojr.coollang.tests;

import com.enricojr.coollang.ast.constants.CoolIdentifier;
import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.semantic.ClassTreeAnalyzer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestClassTreeAnalyzer {
    @Test
    public void testClassTreeSimple() {
        ClassTreeAnalyzer cta = new ClassTreeAnalyzer();

        // build a simple "program"
        CoolProgram cp = new CoolProgram();
        CoolClass cc1 = CoolClass.factory("test1");
        CoolClass cc2 = CoolClass.factory("test2");
        cc2.setParentName(cc1.getName());
        cc2.setParent(cc1);
        ArrayList<CoolClass> classes = new ArrayList<>();
        classes.addAll(List.of(cc1, cc2));
        cp.setClasses(classes);

        cta.visitCoolProgram(cp);
    }

    @Test
    public void testClassTreeClassInheritsItself() {
        ClassTreeAnalyzer cta = new ClassTreeAnalyzer();

        CoolProgram cp = new CoolProgram();
        CoolClass cc1 = CoolClass.factory("test1");
        cc1.setParentName(cc1.getName());
        ArrayList<CoolClass> classes = new ArrayList<>();
        classes.addAll(List.of(cc1));
        cp.setClasses(classes);

        assertThrows(RuntimeException.class, () -> {cta.visitCoolProgram(cp);});
    }

    @Test
    public void testClassTreeInheritanceCycle() {
        ClassTreeAnalyzer cta = new ClassTreeAnalyzer();

        CoolProgram cp = new CoolProgram();
        CoolClass cc1 = CoolClass.factory("test1");
        CoolClass cc2 = CoolClass.factory("test2");

        cc1.setParentName(cc2.getName());
        cc2.setParentName(cc1.getName());

        ArrayList<CoolClass> classes = new ArrayList<>();
        classes.addAll(List.of(cc1, cc2));
        cp.setClasses(classes);

        assertThrows(RuntimeException.class, () -> {cta.visitCoolProgram(cp);});
    }

    @Test
    public void testClassInheritFromInt() {
        ClassTreeAnalyzer cta = new ClassTreeAnalyzer();

        CoolProgram cp = new CoolProgram();
        CoolClass cc1 = CoolClass.factory("Int");
        ArrayList<CoolClass> classes = new ArrayList<>();
        classes.addAll(List.of(cc1));
        cp.setClasses(classes);

        assertThrows(RuntimeException.class, () -> {cta.visitCoolProgram(cp);});
    }

    @Test
    public void testClassInheritFromString() {
        ClassTreeAnalyzer cta = new ClassTreeAnalyzer();

        CoolProgram cp = new CoolProgram();
        CoolClass cc1 = CoolClass.factory("String");
        ArrayList<CoolClass> classes = new ArrayList<>();
        classes.addAll(List.of(cc1));
        cp.setClasses(classes);

//        assertDoesNotThrow(() -> {cta.visitCoolProgram(cp);});
        assertThrows(RuntimeException.class, () -> {cta.visitCoolProgram(cp);});
    }

    @Test
    public void testClassInheritFromBool() {
        ClassTreeAnalyzer cta = new ClassTreeAnalyzer();

        CoolProgram cp = new CoolProgram();
        CoolClass cc1 = CoolClass.factory("Bool");
        ArrayList<CoolClass> classes = new ArrayList<>();
        classes.addAll(List.of(cc1));
        cp.setClasses(classes);

//        assertDoesNotThrow(() -> {cta.visitCoolProgram(cp);});
        assertThrows(RuntimeException.class, () -> {cta.visitCoolProgram(cp);});
    }

    @Test
    public void testClassOverrideIO() {
        // NOTE: As a reminder, you can INHERIT from IO, but not override it.
        ClassTreeAnalyzer cta = new ClassTreeAnalyzer();

        CoolProgram cp = new CoolProgram();
        CoolClass cc1 = CoolClass.factory("IO");
        ArrayList<CoolClass> classes = new ArrayList<>();
        classes.addAll(List.of(cc1));
        cp.setClasses(classes);

//        assertDoesNotThrow(() -> {cta.visitCoolProgram(cp);});
        assertThrows(RuntimeException.class, () -> {cta.visitCoolProgram(cp);});
    }

    @Test
    public void testClassInheritsBadClass() {
        ClassTreeAnalyzer cta = new ClassTreeAnalyzer();

        CoolProgram cp = new CoolProgram();
        CoolClass cc1 = CoolClass.factory("test1");
        cc1.setParentName(new CoolIdentifier("doesnotexist"));
        ArrayList<CoolClass> classes = new ArrayList<>();
        classes.addAll(List.of(cc1));
        cp.setClasses(classes);

//        assertDoesNotThrow(() -> {cta.visitCoolProgram(cp);});
        assertThrows(RuntimeException.class, () -> {cta.visitCoolProgram(cp);});
    }
}
