package org.economic.handlers;

import net.dv8tion.jda.api.entities.Member;
import org.economic.controllers.XpController;
import org.economic.utils.RecordXp;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class VoiceXpHandler extends ListenerAdapter {
    XpController xpController = new XpController();
    private static Map<Long, RecordXp> expControllerMap = new HashMap<>();

    //TODO проверить как оно работает со статик полем

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        long memberId = event.getMember().getIdLong();
        if (event.getChannelJoined() != null && event.getChannelLeft() == null){
            addExpController(memberId);
        }
        else if (event.getChannelJoined() == null && expControllerMap.get(memberId) != null){
            expCount(event);
        }
    }

    @Override
    public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event) {
        long memberId = event.getMember().getIdLong();
        if (event.getMember().getVoiceState().isGuildMuted() && expControllerMap.get(memberId) != null)
            expCount(event);
        else if (!event.getMember().getVoiceState().isGuildMuted() && expControllerMap.get(memberId) == null)
            addExpController(memberId);

    }

    @Override
    public void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event) {
        long memberId = event.getMember().getIdLong();
        if (event.getMember().getVoiceState().isGuildDeafened() && expControllerMap.get(memberId) != null)
            expCount(event);
        else if (!event.getMember().getVoiceState().isGuildDeafened() && expControllerMap.get(memberId) == null)
            addExpController(memberId);
    }

    @Override
    public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent event) {
        long memberId = event.getMember().getIdLong();
        if (event.getMember().getVoiceState().isSelfMuted() && expControllerMap.get(memberId) != null)
            expCount(event);
        else if (!event.getMember().getVoiceState().isSelfMuted() && expControllerMap.get(memberId) == null)
            addExpController(memberId);
    }

    @Override
    public void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event) {
        long memberId = event.getMember().getIdLong();
        if (event.getMember().getVoiceState().isSelfDeafened() && expControllerMap.get(memberId) != null)
            expCount(event);
        else if (!event.getMember().getVoiceState().isSelfDeafened() && expControllerMap.get(memberId) == null)
            addExpController(memberId);
    }
    public RecordXp addExpController(long memberId){
        RecordXp recordXp = new RecordXp(Instant.now(), memberId);
        expControllerMap.put(memberId, recordXp);
        return recordXp;
    }
    public void expCount(GenericGuildVoiceEvent event){
        int exp = expControllerMap.get(event.getMember().getIdLong()).countFinalExp();
        expControllerMap.remove(event.getMember().getIdLong());
        System.out.println(exp);
        xpController.execute(event,exp);
    }

    public static int getCountExpBeforeExecuteByDB(Member member) {
        return expControllerMap.get(member.getIdLong()).countFinalExp();
    }
}
