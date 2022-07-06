package com.susu.demoapplication.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Author : sudan
 * Time : 2021/2/24
 * Description:
 */
public class GraphBFS {
    //BFS的时间复杂度是Θ（V+E）

    private static HashMap<Character, LinkedList<Character>> graph;
    private static HashMap<Character, Integer> dist = new HashMap<>();//记录每个顶点离起始点的距离，也即最短距离

    public static void main(String[] args) {
        initMap();
        bfs(graph, dist, 's');
    }

//    The 1th element:s Distance from s is:0
//    The 2th element:w Distance from s is:1
//    The 3th element:r Distance from s is:1
//    The 4th element:i Distance from s is:2
//    The 5th element:x Distance from s is:2
//    The 6th element:v Distance from s is:2
//    The 7th element:u Distance from s is:3
//    The 8th element:y Distance from s is:3
    private static void bfs(HashMap<Character, LinkedList<Character>> graph,
                            HashMap<Character, Integer> dist,
                            char start) {
        Queue<Character> q = new LinkedList<>();
        q.add(start); //将s作为起始顶点加入队列
        dist.put(start, 0);
        int i = 0;
        while (!q.isEmpty()) {
            char top = q.poll();// 取出队首元素
            i++;
            System.out.println("The " + i + "th element:" + top + " Distance from s is:" + dist.get(top));
            int d = dist.get(top) + 1;// 得出其周边还未被访问的节点的距离
            for (Character c : graph.get(top)) {
                if (!dist.containsKey(c)) { // 如果dist中还没有该元素说明还没有被访问
                    dist.put(c, d);
                    q.add(c);
                }
            }
        }
    }

    private static void initMap() {
        //构造各顶点
        LinkedList<Character> list_s = new LinkedList<>();
        list_s.add('w');
        list_s.add('r');
        LinkedList<Character> list_w = new LinkedList<>();
        list_w.add('s');
        list_w.add('i');
        list_w.add('x');
        LinkedList<Character> list_r = new LinkedList<>();
        list_r.add('s');
        list_r.add('v');
        LinkedList<Character> list_x = new LinkedList<>();
        list_x.add('w');
        list_x.add('i');
        list_x.add('u');
        list_x.add('y');
        LinkedList<Character> list_v = new LinkedList<>();
        list_v.add('r');
        LinkedList<Character> list_i = new LinkedList<>();
        list_i.add('u');
        list_i.add('x');
        list_i.add('w');
        LinkedList<Character> list_u = new LinkedList<>();
        list_u.add('i');
        list_u.add('x');
        list_u.add('y');
        LinkedList<Character> list_y = new LinkedList<>();
        list_y.add('u');
        list_y.add('x');

        //构造图
        graph = new HashMap<>();
        graph.put('s', list_s);
        graph.put('w', list_w);
        graph.put('r', list_r);
        graph.put('x', list_x);
        graph.put('v', list_v);
        graph.put('i', list_i);
        graph.put('y', list_y);
        graph.put('u', list_u);
    }

}
