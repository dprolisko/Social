package com.dprol.post_service.utils;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class HashtagUtils {

    public Set<String> getHashtag(String text) {
        String[] words = text.split(" ");
        Set<String> set = new HashSet<String>();
        for (String word : words) {
            if(word != null && word.startsWith("#") && word.length() > 1) {
                set.add(word.substring(1));
            }
        }
        return set;
    }
}
