// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

public class ParseDate {
    public static String returnTrueIfDaysElseFalse(final String str) {
        if (str.contains("\u0447")) {
            return "hours";
        }
        return "days";
    }
}
