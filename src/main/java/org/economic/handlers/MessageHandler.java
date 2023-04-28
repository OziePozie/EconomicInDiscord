package org.economic.handlers;

import net.dv8tion.jda.api.events.session.ShutdownEvent;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.economic.triggers.LikeBotTrigger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MessageHandler extends ListenerAdapter {

    Map<Long, Integer> messageCounter = new HashMap<>();
    UserDAOImplement userDAOImplement = new UserDAOImplement();

    LikeBotTrigger likeBotTrigger = new LikeBotTrigger();
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getAuthor().isBot()){

            Long userID = event.getMember().getIdLong();

            Integer count = messageCounter.get(userID);

            if (userDAOImplement.findByID(userID) == null){
                userDAOImplement.addUser(new User(userID, 0));
            }

            if (count == null) messageCounter.put(userID, 1);

            else if (count >= (int)(Math.random()*((10-1)+1))+1) {
                userDAOImplement.setMessages(userDAOImplement.findByID(userID), count);

                messageCounter.remove(userID);
            } else messageCounter.replace(userID, count + 1);
        }

        if(likeBotTrigger.isFromBot(event)){

            likeBotTrigger.trigger(event);

        }
        }
    }


