// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

public class dayFormat
{
    String[] rusDayFormat;
    
    public dayFormat() {
        this.rusDayFormat = new String[] { "\u0434\u0435\u043d\u044c", "\u0434\u043d\u044f", "\u0434\u043d\u0435\u0439" };
    }
    
    public String numberDayFormat(final int d) {
        if (d == 1) {
            return this.rusDayFormat[0];
        }
        if (d > 1 && d < 5) {
            return this.rusDayFormat[1];
        }
        if (d > 5) {
            return this.rusDayFormat[2];
        }
        return null;
    }
}
