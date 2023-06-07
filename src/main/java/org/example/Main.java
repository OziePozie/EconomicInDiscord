// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main extends ListenerAdapter {
    public Main() {
        JDABuilder.createDefault("MTA0MzU0NTU2MzEzNzg0MzIyMQ.Gf-xAk.jek8SXxA_ppHOLIsY8rjZqVYZtSmev1-JP_Mcg", GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES).addEventListeners(new CommandHandler(this)).setStatus(OnlineStatus.ONLINE).setMemberCachePolicy(MemberCachePolicy.ALL).build();
    }

    public static void main(final String[] args) {
        new Main();
    }
}
