package org.economic.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.internal.requests.Route;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MassAwardCommand {
    UserDAOImplement userDAOImplement = new UserDAOImplement();
    Dotenv dotenv = Dotenv.load();
    public void execute(MessageReceivedEvent event){
        String[] s = event.getMessage().getContentRaw().split(" ");
        int quantity = Integer.parseInt(s[1]);
        List<Role> roles = event.getMessage().getMentions().getRoles();
        List<Member> members = event.getMessage().getMentions().getMembers();
        List<Member> memberList = new ArrayList<>();
        if (roles.size()>0){
            for (Role role:roles) {
                memberList.addAll(event.getGuild().getMembersWithRoles(role));

            }
        }
        List<Member> members1 = new ArrayList<>(members.stream().toList());
        members1.addAll(memberList.stream().toList());
        for (Member m :members1) {
            long id = m.getIdLong();
            User user = userDAOImplement.findByID(id);
            if (user == null){
                userDAOImplement.addUser(new User(id, quantity));
            } else {
                userDAOImplement.setBalance(user, +quantity);
            }
            event
                    .getChannel()
                    .sendMessage("Выдано " + quantity + dotenv.get("CURRENCY_EMOJI") + " " + m.getEffectiveName())
                    .complete()
                    .delete()
                    .queueAfter(1, TimeUnit.MINUTES);
        }

    }
}
