package org.economic.handlers;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.economic.controllers.XpController;
import org.economic.utils.RecordXp;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class VoiceXpHandler extends ListenerAdapter {
    private static final Map<Long, RecordXp> expControllerMap = new HashMap<>();
    XpController xpController = new XpController();

    public static int getCountExpBeforeExecuteByDB(Member member) {
        try {
            expControllerMap.get(member.getIdLong());
            return expControllerMap.get(member.getIdLong()).countFinalExp();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        long memberId = event.getMember().getIdLong();
        Member member = event.getMember();
        if (event.getChannelJoined() != null && event.getChannelLeft() == null && !isAfkChannel(event)
                && !member.getVoiceState().isSuppressed()
                && !member.getVoiceState().isDeafened()
                && !member.getVoiceState().isMuted()) {
            addExpController(memberId);
        } else if ((event.getChannelJoined() == null || isAfkChannel(event)) && expControllerMap.get(memberId) != null) {
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

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getGuilds().get(0).loadMembers().onSuccess(members -> members.forEach(member -> {
            if (!member.getVoiceState().isSuppressed()
                    && member.getVoiceState().inAudioChannel()
                    && !member.getVoiceState().isDeafened()
                    && !member.getVoiceState().isMuted()){
                addExpController(member.getIdLong());
            }
        } ));

    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        expControllerMap.forEach((key, value) -> {
            int exp = value.countFinalExp();
            System.out.println(exp);
            xpController.execute(key, exp);
        });
    }

    public boolean isAfkChannel(GuildVoiceUpdateEvent event) {
        VoiceChannel afkChannel = event.getGuild().getAfkChannel();
        return event.getChannelJoined().getId().equals(afkChannel.getId());
    }

    public void addExpController(long memberId) {
        RecordXp recordXp = new RecordXp(Instant.now(), memberId);
        expControllerMap.put(memberId, recordXp);
    }

    public void expCount(GenericGuildVoiceEvent event) {
        int exp = expControllerMap.get(event.getMember().getIdLong()).countFinalExp();
        expControllerMap.remove(event.getMember().getIdLong());
        System.out.println(exp);
        xpController.execute(event, exp);
    }

    public void expCount(Member member) {
        int exp = expControllerMap.get(member.getIdLong()).countFinalExp();
        expControllerMap.remove(member.getIdLong());
        System.out.println(exp);
        xpController.execute(member.getIdLong(), exp);
        if (!member.getVoiceState().isSuppressed()
                && !member.getVoiceState().isDeafened()
                && !member.getVoiceState().isMuted()) {
            addExpController(member.getIdLong());
        }
    }

    public void clearListAddAllExp(Member member){
        int exp = expControllerMap.get(member.getIdLong()).countFinalExp();
        System.out.println(exp);
        xpController.execute(member.getIdLong(), exp);
    }

    public static Map<Long, RecordXp> getExpControllerMap() {
        return expControllerMap;
    }
}
