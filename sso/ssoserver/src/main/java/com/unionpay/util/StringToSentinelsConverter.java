package com.unionpay.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class StringToSentinelsConverter {

    public Set<String> convert(String sentinels) {
        Set<String> result = new HashSet<String>();
        String[] ss = sentinels.split(",");
        result.addAll(Arrays.asList(ss));
        return result;
    }
}