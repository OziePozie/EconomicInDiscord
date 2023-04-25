package org.economic.controllers;

import org.economic.database.userxp.UserXp;
import org.economic.database.userxp.UserXpDAOImplement;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;


public class XpController {
    UserXpDAOImplement userXpDAOImplement = new UserXpDAOImplement();

    public XpController() {}

    public void execute(GenericGuildVoiceEvent event, int addedXp){
        Member member = event.getMember();
        UserXp userXp = userXpDAOImplement.findById(member.getIdLong());
        if (userXp == null){
            userXpDAOImplement.addUserXp(new UserXp(member.getIdLong(), addedXp));
        } else userXpDAOImplement.setXp(userXp, addedXp);

    }


}
