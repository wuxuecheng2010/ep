package com.enze.ep.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
public class OrderCodeUtil 
{ 
    private OrderCodeUtil() 
    { 
    } 
    public static synchronized String getUniqueString(int orderType) 
    { 
        if(generateCount > 99999) 
            generateCount = 0; 
        String suffix=String.format("%04d", generateCount);
        String uniqueNumber =orderType+DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS") + suffix; 
        generateCount++; 
        return uniqueNumber; 
    } 
    private static final int MAX_GENERATE_COUNT = 9999; 
    private static int generateCount = 1; 
} 



