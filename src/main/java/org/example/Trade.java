// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

public class Trade {
    private final int value;
    UserInfo userInfoGiver;
    UserInfo userInfoGetter;
    private String memberGiver;
    private String memberGetter;

    Trade(final int value, final UserInfo user1, final UserInfo user2) {
        this.userInfoGiver = user1;
        this.userInfoGetter = user2;
        this.value = value;
    }

    public void commandGive() {
        if (this.userInfoGiver.getMoney() < this.value) {
            System.out.println("\u041d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u0441\u0440\u0435\u0434\u0441\u0442\u0432 \u043d\u0430 \u0431\u0430\u043b\u0430\u043d\u0441\u0435");
        } else {
            this.userInfoGiver.setMoney(this.userInfoGiver.getMoney() - this.value);
            this.userInfoGetter.setMoney(this.userInfoGetter.getMoney() + this.value);
        }
    }
}
