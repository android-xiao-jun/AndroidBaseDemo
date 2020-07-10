package com.hsjskj.hy.library_net.net;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanglei on 2016/11/28.
 */

public class Kits {


    public static class Empty {

        public static boolean check(Object obj) {
            return obj == null;
        }

        public static boolean check(List list) {
            return list == null || list.isEmpty();
        }

        public static boolean check(Object[] array) {
            return array == null || array.length == 0;
        }

        public static boolean check(String str) {
            return str == null || "".equals(str);
        }

        public static boolean check(Map map) {
            return map == null || map.isEmpty();
        }

        public static boolean check(Set set) {
            return set == null || set.isEmpty();
        }

    }

}
