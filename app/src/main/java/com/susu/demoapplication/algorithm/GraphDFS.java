package com.susu.demoapplication.algorithm;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Author : sudan
 * Time : 2021/2/24
 * Description:
 */
public class GraphDFS {

    private static HashMap<Character, LinkedList<Character>> graph;
    private static HashMap<Character, Boolean> visited = new HashMap<>();
    private static int count;//通过一个全局变量count记录了进入每个节点和离开每个节点的时间

    public static void main(String[] args) {
        initMap();
        dfs(graph, visited);
    }

    private static void dfs(HashMap<Character, LinkedList<Character>> graph, HashMap<Character, Boolean> visited) {
        visit(graph, visited, 'u');// 为了和图中的顺序一样，我认为控制了DFS先访问u节点
//        visit(graph, visited, 'w');
    }


//    The time into element u:1
//    The time into element v:2
//    The time into element y:3
//    The time into element x:4
//    The time out element x:5
//    The time out element y:6
//    The time out element v:7
//    The time out element u:8
    private static void visit(HashMap<Character, LinkedList<Character>> graph,
                              HashMap<Character, Boolean> visited,
                              char start) {
        if (!visited.containsKey(start)) {
            count++;
            System.out.println("The time into element " + start + ":" + count);
            visited.put(start, true);
            for (char c : graph.get(start)) {
                if (!visited.containsKey(c)) {
                    visit(graph, visited, c);
                }
            }
            count++;
            System.out.println("The time out element " + start + ":" + count);// 记录离开该节点的时间
        }
    }

    private static void initMap() {
        LinkedList<Character> list_u = new LinkedList<>();
        list_u.add('v');
        list_u.add('x');
        LinkedList<Character> list_v = new LinkedList<>();
        list_v.add('y');
        LinkedList<Character> list_y = new LinkedList<>();
        list_y.add('x');
        LinkedList<Character> list_x = new LinkedList<>();
        list_x.add('v');
        LinkedList<Character> list_w = new LinkedList<>();
        list_w.add('y');
        list_w.add('z');
        LinkedList<Character> list_z = new LinkedList<>();
        //构造图
        graph = new HashMap<>();
        graph.put('u', list_u);
        graph.put('v', list_v);
        graph.put('y', list_y);
        graph.put('x', list_x);
        graph.put('w', list_w);
        graph.put('z', list_z);
    }


}
