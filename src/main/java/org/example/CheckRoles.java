// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.entities.Member;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CheckRoles extends ListenerAdapter
{
    Dotenv dotenv;
    
    public CheckRoles() {
        this.dotenv = Dotenv.load();
    }
    
    public boolean isOwner(final Member m, final GenericEvent event) {
        final Role Owner = event.getJDA().getRoleById(this.dotenv.get("OWNER_ROLE"));
        return m.getRoles().contains(Owner);
    }
    
    public boolean isEventMaker(final Member m, final GenericEvent event) {
        final Role EventMaker = event.getJDA().getRoleById(this.dotenv.get("EVENT_MAKER_ROLE"));
        return m.getRoles().contains(EventMaker);
    }
    
    public boolean isModerator(final Member m, final GenericEvent event) {
        final Role Moderator = event.getJDA().getRoleById(this.dotenv.get("MODERATOR_ROLE"));
        return m.getRoles().contains(Moderator);
    }
    
    public boolean isEventBan(final Member m, final GenericEvent event) {
        final Role EventBan = event.getJDA().getRoleById(this.dotenv.get("EVENT_BAN_ROLE"));
        return m.getRoles().contains(EventBan);
    }
    
    public boolean isMakerAssistant(final Member m, final GenericEvent event) {
        final Role MakerAssistant = event.getJDA().getRoleById(this.dotenv.get("EVENT_ASSISTANT"));
        return m.getRoles().contains(MakerAssistant);
    }
}
