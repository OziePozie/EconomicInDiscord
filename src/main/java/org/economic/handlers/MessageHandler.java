package org.economic.handlers;

import jakarta.persistence.criteria.CriteriaBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.economic.commands.MassAwardCommand;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;
import org.economic.triggers.LikeBotTrigger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MessageHandler extends ListenerAdapter {

    Map<Long, Integer> messageCounter = new HashMap<>();
    UserDAOImplement userDAOImplement = new UserDAOImplement();
    LikeBotTrigger likeBotTrigger = new LikeBotTrigger();
    MassAwardCommand massAwardCommand = new MassAwardCommand();

    static Map<Long, Integer> messageCounterMapTwoHoursRefresh = new HashMap<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        if (!event.getAuthor().isBot()) {

            Long userID = event.getMember().getIdLong();

            Integer count = messageCounter.get(userID);



            if (userDAOImplement.findByID(userID) == null) {
                userDAOImplement.addUser(new User(userID, 0));
            }

            if (count == null) {
                messageCounter.put(userID, 1);
                messageCounterMapTwoHoursRefresh.put(userID, 1);
            }

            else if (count >= (int) (Math.random() * ((10 - 1) + 1)) + 1) {

                userDAOImplement.setMessages(userDAOImplement.findByID(userID), count);

                messageCounter.remove(userID);

            } else {
                messageCounter.replace(userID, count + 1);

                messageCounterMapTwoHoursRefresh.replace(userID, count + 1);
            }
        }

        if (likeBotTrigger.isFromBot(event)) {
            likeBotTrigger.trigger(event);
        }
        if (content.equals("!rebootBot") && event
                .getMessage()
                .getMember()
                .getId()
                .equals("145439829549645824")){
            event.getJDA().shutdown();
        }

        if (content.startsWith("!massaward") && event
                .getMember()
                .getPermissions()
                .contains(Permission.ADMINISTRATOR)){
            massAwardCommand.execute(event);
        }
    }

    public static Map<Long, Integer> getMessageCounterMapTwoHoursRefresh() {
        return messageCounterMapTwoHoursRefresh;
    }

    public static void setMessageCounterMapTwoHoursRefresh(Map<Long, Integer> messageCounterMapTwoHoursRefresh) {
        MessageHandler.messageCounterMapTwoHoursRefresh = messageCounterMapTwoHoursRefresh;
    }
    public void addToMapCount(Long memberId, Integer count){
        messageCounterMapTwoHoursRefresh.put(memberId, count);
    }
    public static void refreshMap(){
        messageCounterMapTwoHoursRefresh.clear();
    }
}


