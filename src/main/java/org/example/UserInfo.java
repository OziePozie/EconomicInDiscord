// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

import net.dv8tion.jda.api.entities.Member;

public class UserInfo
{
    private final Member member;
    private int money;
    
    UserInfo(final Member member) {
        this.member = member;
    }
    
    public int getMoney() {
        return this.money;
    }
    
    public Member getMember() {
        return this.member;
    }
    
    public void setMoney(final int money) {
        this.money = money;
    }
}
