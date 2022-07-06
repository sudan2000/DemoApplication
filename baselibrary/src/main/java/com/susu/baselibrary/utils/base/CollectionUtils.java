package com.susu.baselibrary.utils.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class CollectionUtils {

    /**
     * 判断列表是否为null或空
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isEmpty(Object[] list) {
        return list == null || list.length == 0;
    }

    /**
     * 判断列表不为空
     */
    public static boolean isNotEmpty(List<?> list) {
        return !isEmpty(list);
    }


    public static boolean isEmpty(Collection<?> collection) {
        return (null == collection || collection.size() == 0);
    }

    public static <T> List<T> asList(T... array) {
        if (array != null) {
            return Arrays.asList(array);
        }
        return new ArrayList<T>();
    }

    public static boolean hasMoreSize(List list, int size){
        return list!=null && list.size() >= size;
    }


    public static int size(Collection<?> collection) {
        if (collection != null ) {
            return collection.size();
        }
        return 0;
    }

    public static <T> T getItem(List<T> collection, int index) {
        if (collection != null && (index >= 0 && index < collection.size())) {
            return collection.get(index);
        }
        return null;
    }

    public static <T> String concat(List<T> strList, String delimiter) {
        if (isEmpty(strList)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (T str : strList) {
            if (str != null) {
                builder.append(str).append(delimiter);
            }
        }
        if (builder.length() == 0) {
            return "";
        }
        return builder.substring(0, builder.length() - delimiter.length());
    }


    public static <T> List<T> merge(List<T> list1, List<T> list2) {
        List<T> mergeList = new ArrayList<>();
        if (list1 != null) {
            mergeList.addAll(list1);
        }
        if (list2 != null) {
            mergeList.addAll(list2);
        }
        return mergeList;
    }

    public static void clearList(List... objectList) {
        for (List list : objectList) {
            if (list != null) {
                list.clear();
            }
        }
    }
}
