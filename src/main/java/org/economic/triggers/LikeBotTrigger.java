package org.economic.triggers;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;

public class LikeBotTrigger {
    final public String botID = "575776004233232386";
    UserDAOImplement userDAOImplement = new UserDAOImplement();
    Dotenv dotenv = Dotenv.load();
    public void trigger(MessageReceivedEvent event) {
        if (event.getMessage().getEmbeds().get(0).getDescription().contains("Вы успешно лайкнули")) {
            String st = event.getMessage().getEmbeds().get(0).getAuthor().getName();
            String[] name = st.split("#");

            Member member = event.getGuild().getMemberByTag(name[0], name[1]);
            User user = userDAOImplement.findByID(member.getIdLong());
            if (user == null) {
                userDAOImplement.addUser(new User(member.getIdLong(), 300));
            } else userDAOImplement.setBalance(user, +300);
            //
            //
            event.getGuild()
                    .getTextChannelById(dotenv.get("SPAM_CHANNEL"))
                    .sendMessage(member.getEffectiveName() + " лайкнул сервер и получил за это 300" + dotenv.get("CURRENCY_EMOJI"))
                    .queue();
        }

    }

    public boolean isFromBot(MessageReceivedEvent event) {
        return event.getMessage().getAuthor().isBot() && event.getAuthor().getId().equals(botID);

    }


}
