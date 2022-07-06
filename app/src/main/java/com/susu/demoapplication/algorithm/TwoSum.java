package com.susu.demoapplication.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * Author : sudan
 * Time : 2021/12/9
 * Description:
 */
public class TwoSum {

    public static int[] method1(int[] nums, int target) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                count++;
                System.out.println("过程： count: " + count + " --- " + i + " " + j);
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }

    public static int[] method2(int[] nums, int target) {
        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            count++;
            System.out.println("过程： count: " + count);
            Map<Integer, Integer> hashMap = new HashMap<>();
            if (hashMap.containsKey(target - nums[i])) {
                System.out.println(hashMap.get(target - nums[i]) + "  " + i);
                return new int[]{hashMap.get(target - nums[i]), i};
            }
            hashMap.put(nums[i], i);
        }
        return new int[0];
    }

    public static void main(String[] args) {
        int[] nums = {1, 5, 6, 7, 3};
        int[] result = TwoSum.method2(nums, 10);
        System.out.println("结果： " + result[0] + " " + result[1]);
    }
}
