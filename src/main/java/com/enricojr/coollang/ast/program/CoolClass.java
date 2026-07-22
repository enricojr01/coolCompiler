package com.enricojr.coollang.ast.program;

import java.util.ArrayList;

public class CoolClass extends CoolBaseNode {
    private String name;
    private ArrayList<CoolAttribute> attributes;
    private ArrayList<CoolFormal> formals;
    private ArrayList<CoolMethod> methods;
}
