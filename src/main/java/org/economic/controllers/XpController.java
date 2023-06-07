package org.economic.controllers;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import org.economic.database.userxp.UserXp;
import org.economic.database.userxp.UserXpDAOImplement;


public class XpController {
    UserXpDAOImplement userXpDAOImplement = new UserXpDAOImplement();

    public XpController() {
    }

    public void execute(GenericGuildVoiceEvent event, int addedXp) {
        Member member = event.getMember();
        UserXp userXp = userXpDAOImplement.findById(member.getIdLong());
        if (userXp == null) {
            userXpDAOImplement.addUserXp(new UserXp(member.getIdLong(), addedXp));
        } else userXpDAOImplement.setXp(userXp, addedXp);

    }

    public void execute(long id, int addedXp) {
        UserXp userXp = userXpDAOImplement.findById(id);
        if (userXp == null) {
            userXpDAOImplement.addUserXp(new UserXp(id, addedXp));
        } else userXpDAOImplement.setXp(userXp, addedXp);

    }

}
