package economic.controllers;

import economic.database.userxp.UserXp;
import economic.database.userxp.UserXpDAO;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;


public class XpController {
    UserXpDAO userXpDAO = new UserXpDAO();

    public XpController() {}

    public void execute(GenericGuildVoiceEvent event, int addedXp){
        Member member = event.getMember();
        UserXp userXp = userXpDAO.findById(member.getIdLong());
        if (userXp == null){
            userXpDAO.addUserXp(new UserXp(member.getIdLong(), addedXp));
        } else userXpDAO.setXp(userXp, addedXp);

    }


}
