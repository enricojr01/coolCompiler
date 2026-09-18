package com.enricojr.coollang.tests.unit;

import com.enricojr.coollang.ast.program.CoolClass;
import com.enricojr.coollang.ast.program.CoolProgram;
import com.enricojr.coollang.semantic.classtree.ClassTreeBuilder;
import com.enricojr.coollang.semantic.classtree.ClassTreeLinker;
import com.enricojr.coollang.semantic.classtree.ClassTreeSetup;
import com.enricojr.coollang.semantic.symboltable.SymbolTableBuilder;
import com.enricojr.coollang.semantic.symboltable.SymbolTableLinker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestClassTreeSetup {
    @Test
    public void testClassTreeLifeCl() {
        // problem - Type mismatch between consequent (Main extends CellularAutomaton) and
        // alternative (Bool extends Object)
        ClassTreeSetup cts = new ClassTreeSetup();
        ClassTreeLinker ctl = new ClassTreeLinker();
        ClassTreeBuilder ctb = new ClassTreeBuilder("none");
        SymbolTableLinker stl = new SymbolTableLinker();
        SymbolTableBuilder stb = new SymbolTableBuilder();

        CoolProgram cp = new CoolProgram();
        cp.setClasses(new ArrayList<>());
        cts.visitCoolProgram(cp);

        CoolClass board = CoolClass.factory("Board");
        CoolClass cellularAutomaton = CoolClass.factory("CellularAutomaton");
        CoolClass main = CoolClass.factory("Main");

        // problem - cannot easily retrieve IO class in a "standalone" setup like this
        CoolClass ioClass = cp
                .getRoot()
                .getChildren()
                .stream()
                .filter(x -> x.getName().getValueString().equals("IO"))
                .toList()
                .getFirst();
        CoolClass boolClass = cp
                .getRoot()
                .getChildren()
                .stream()
                .filter(x -> x.getName().getValueString().equals("Bool"))
                .toList()
                .getFirst();

        main.setParent(cellularAutomaton);
        main.setParentName(cellularAutomaton.getName());
        cellularAutomaton.setParent(board);
        cellularAutomaton.setParentName(board.getParentName());
        board.setParent(ioClass);
        board.setParentName(ioClass.getName());

        cp.getClasses().addAll(new ArrayList<>(List.of(main, board, cellularAutomaton)));
        ctl.visitCoolProgram(cp);
        ctb.visitCoolProgram(cp);
        stl.visitCoolProgram(cp);
        stb.visitCoolProgram(cp);

        CoolClass root = cp.getRoot();
        System.out.println(CoolClass.leastCommonAncestor(main.getComputedType(), boolClass));
    }
}
