package com.enricojr.coollang.semantic;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassTree {
    private HashMap<String, ArrayList<TreeNode>> adjacencyList;

    public ClassTree() {
        adjacencyList = new HashMap<>();
    }

    public void addClass(TreeNode tn) {
        adjacencyList.put(tn.getName(), new ArrayList<>());
    }

    public void addNeighbor(String source, String destination) {
        adjacencyList.get();
    }
}
