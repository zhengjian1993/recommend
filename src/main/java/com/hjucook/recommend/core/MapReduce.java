package com.hjucook.recommend.core;

import java.util.Objects;

/**
 * ${DESCRIPTION}
 *
 * @author zhengjian
 * @date 2018-12-11 15:29
 */
public class MapReduce {

    public static void main(String[] args) {
        String str = "forum-question_132";
        int hash1 = str.hashCode();
        int hash2 = Objects.hashCode(hash1);
        int hash3 = Objects.hashCode(hash2);
        System.out.println(hash1);
        System.out.println(hash2);
        System.out.println(hash3);
    }
}
