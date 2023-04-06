package economic.handlers;

import economic.controllers.XpController;
import economic.utils.ExpController;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class VoiceXpHandler extends ListenerAdapter {
    XpController xpController = new XpController();
    Map<Long, ExpController> expControllerMap = new HashMap<>();


    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        System.out.println(event);
        ExpController expController;
        System.out.println(expControllerMap);
        if (event.getChannelJoined() != null){
            expController = new ExpController(Instant.now(), event.getMember().getIdLong());
            expControllerMap.put(event.getMember().getIdLong(), expController);
        }
        else if (event.getChannelJoined() == null){
            int exp = expControllerMap.get(event.getMember().getIdLong()).countFinalExp();
            expControllerMap.remove(event.getMember().getIdLong());
            System.out.println(exp);
            xpController.execute(event,exp);
        }
    }
}
