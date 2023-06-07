// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.List;

public class MassRole extends ListenerAdapter {
    DbHandler dbHandler;

    public MassRole() {
        this.dbHandler = new DbHandler();
    }

    public void onMassRoleMessage(final MessageReceivedEvent event, final int days) throws SQLException, ClassNotFoundException {
        final List<Role> role = event.getMessage().getMentions().getRoles();
        final List<Member> members = event.getMessage().getMentions().getMembers();
        for (final Member member : members) {
            event.getGuild().addRoleToMember(member, role.get(0)).queue();
            event.getChannel().sendMessage(String.format("Выдана роль %s пользователю %s на %s дня", role.get(0).getAsMention(), member.getAsMention(), days)).queue();
            this.dbHandler.updateMassRoleScheduler(member.getId(), role.get(0).getId(), days);
        }
    }
}
