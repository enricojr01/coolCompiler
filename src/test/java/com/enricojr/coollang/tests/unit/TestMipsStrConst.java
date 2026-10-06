package com.enricojr.coollang.tests.unit;

import com.enricojr.coollang.codegen.models.MipsIntConst;
import com.enricojr.coollang.codegen.models.MipsStrConst;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMipsStrConst {
    @Test
    public void testStringByteCounter() {
        String test1 = "\n";
        System.out.println("String length: " + test1.getBytes().length);

        assertEquals(1, test1.getBytes().length);
    }

    @Test
    public void testLongerStringByteCounter() {
        String test1 = "hello";
        System.out.println("String length: " + test1.getBytes().length);
        assertEquals(5, test1.getBytes().length);
    }

    @Test
    public void testEvenWordSizeStr() {
        String test1 = "cab";
        MipsIntConst size = new MipsIntConst("testStrSize", 2, test1.length());
        MipsStrConst sample = new MipsStrConst("testStr1", 1, size, test1);
        System.out.println(sample);
    }

    @Test
    public void testEmptyString() {
        String test1 = "";
        MipsIntConst size = new MipsIntConst("testStrSize", 2, test1.length());
        MipsStrConst sample = new MipsStrConst("testStr1", 1, size, test1);
        System.out.println(sample);
    }
}
