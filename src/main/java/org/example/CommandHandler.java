// 
// Decompiled by Procyon v0.5.36
// 

package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class CommandHandler extends ListenerAdapter {
    private final Main main;
    private final DbHandler dbHandler;
    private final MassRole massRole;
    CheckRoles checkRoles;

    CommandHandler(final Main main) {
        this.dbHandler = new DbHandler();
        this.massRole = new MassRole();
        this.checkRoles = new CheckRoles();
        this.main = main;
    }

    @Override
    public void onReady(final ReadyEvent event) {
        final JDA jda = event.getJDA();
        System.out.println(jda.getGuilds());
        System.out.println(event.getJDA().getGuildById("1043545096748027944").getCategoryById("1043545096748027945").getTextChannels());
        try {
            this.dbHandler.createUsers();
            this.dbHandler.createCloseBansTable();
            this.dbHandler.createEventBansTable();
            this.dbHandler.createTournamentBansTable();
            this.dbHandler.createSchedulerBansTable();
            this.dbHandler.createSchedulerMassRole();
            System.out.println(this.dbHandler.selectFromMassRoleScheduler());
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        CommandHandler.this.dbHandler.selectFromScheduler("close").forEach((s, s2) -> event.getJDA().getGuilds().get(1).removeRoleFromMember(UserSnowflake.fromId(s), jda.getRoleById(s2)).queue());
                        CommandHandler.this.dbHandler.selectFromScheduler("event").forEach((s, s2) -> event.getJDA().getGuilds().get(1).removeRoleFromMember(UserSnowflake.fromId(s), jda.getRoleById(s2)).queue());
                        CommandHandler.this.dbHandler.selectFromScheduler("tournament").forEach((s, s2) -> event.getJDA().getGuilds().get(1).removeRoleFromMember(UserSnowflake.fromId(s), jda.getRoleById(s2)).queue());
                        CommandHandler.this.dbHandler.selectFromMassRoleScheduler().forEach(s -> {
                            event.getJDA().getGuilds().get(1).removeRoleFromMember(UserSnowflake.fromId(s.getMemberid()), jda.getRoleById(s.getRoleID())).queue();
                            System.out.println(UserSnowflake.fromId(s.getMemberid()).getAsMention());
                        });
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 10000L, 600000L);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onButtonInteraction(final ButtonInteractionEvent event) {
        if (event.getButton().getId().contains("event") && event.getButton().getId().contains(event.getMember().getId())) {
            final String[] array = event.getButton().getId().split(":");
            final Member m = event.getGuild().getMemberById(array[2]);
            event.deferEdit().queue();
            event.getMessage().editMessageEmbeds(this.dbHandler.eventBanSelect("event", array[2], m, event, array[0])).queue();
            event.getButton().isDisabled();
        }
        if (event.getButton().getId().contains("close") && event.getButton().getId().contains(event.getMember().getId())) {
            final String[] array = event.getButton().getId().split(":");
            final Member m = event.getGuild().getMemberById(array[2]);
            event.deferEdit().queue();
            event.getMessage().editMessageEmbeds(this.dbHandler.eventBanSelect("close", array[2], m, event, array[0])).queue();
            event.getButton().isDisabled();
        }
        if (event.getButton().getId().contains("tournament") && event.getButton().getId().contains(event.getMember().getId())) {
            final String[] array = event.getButton().getId().split(":");
            final Member m = event.getGuild().getMemberById(array[2]);
            event.deferEdit().queue();
            event.getMessage().editMessageEmbeds(this.dbHandler.eventBanSelect("tournament", array[2], m, event, array[0])).queue();
        }
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        final String message = event.getMessage().getContentRaw();
        final String memberid = event.getAuthor().getId();
        if (message.startsWith("!bansCommands")) {
            final User me = event.getJDA().getUserById("145439829549645824");
            final EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("\u041a\u043e\u043c\u0430\u043d\u0434\u044b \u0434\u043b\u044f \u0431\u0430\u043d\u043e\u0432 \u043d\u0430 \u0438\u0432\u0435\u043d\u0442\u0430\u0445").addField("", String.format("!CloseBan <@\u0423\u0447\u0430\u0441\u0442\u043d\u0438\u043a | ID> [\u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0434\u043d\u0435\u0439] [\u043f\u0440\u0438\u0447\u0438\u043d\u0430]\n!CloseBan %s 1 \u041f\u0440\u0435\u043f\u044f\u0442\u0441\u0442\u0432\u0438\u0435 \u043f\u043e\u0431\u0435\u0434\u0435\n\n!EventBan <@\u0423\u0447\u0430\u0441\u0442\u043d\u0438\u043a | ID> [\u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0434\u043d\u0435\u0439] [\u043f\u0440\u0438\u0447\u0438\u043d\u0430]\n!EventBan %s 1 \u041f\u0440\u0435\u043f\u044f\u0442\u0441\u0442\u0432\u0438\u0435 \u043f\u043e\u0431\u0435\u0434\u0435\n\n!TournamentBan <@\u0423\u0447\u0430\u0441\u0442\u043d\u0438\u043a | ID> [\u043a\u043e\u043b\u0438\u0447\u0435\u0441\u0442\u0432\u043e \u0434\u043d\u0435\u0439] [\u043f\u0440\u0438\u0447\u0438\u043d\u0430]\n!TournamentBan %s 90 \u041f\u0440\u0435\u043f\u044f\u0442\u0441\u0442\u0432\u0438\u0435 \u043f\u043e\u0431\u0435\u0434\u0435\n\n!StoryBans <@\u0423\u0447\u0430\u0441\u0442\u043d\u0438\u043a | ID>\n!StoryBans %s\n\n!EventDelBan [\u043d\u043e\u043c\u0435\u0440 \u0441\u043b\u0443\u0447\u0430\u044f]\n!CloseDelBan [\u043d\u043e\u043c\u0435\u0440 \u0441\u043b\u0443\u0447\u0430\u044f]\n!TournamentDelBan [\u043d\u043e\u043c\u0435\u0440 \u0441\u043b\u0443\u0447\u0430\u044f]\n\n", me.getAsMention(), me.getAsMention(), me.getAsMention(), me.getAsMention(), me.getAsMention(), me.getAsMention()), false);
            event.getChannel().sendMessageEmbeds(embedBuilder.build(), new MessageEmbed[0]).queue();
        }
        if (message.contains("Ban") && message.startsWith("!") && !message.contains("Story")) {
            final Member author = event.getMember();
            if (author.getPermissions().contains(Permission.ADMINISTRATOR) || this.checkRoles.isModerator(author, event) || this.checkRoles.isEventMaker(author, event) || this.checkRoles.isMakerAssistant(author, event)) {
                final String[] msg = message.split(" ");
                final String s = msg[0];
                switch (s) {
                    case "!EventBan": {
                        new BanHandler().banApply(event, "event");
                        break;
                    }
                    case "!CloseBan": {
                        new BanHandler().banApply(event, "close");
                        break;
                    }
                    case "!TournamentBan": {
                        new BanHandler().banApply(event, "tournament");
                        break;
                    }
                    case "!EventDelBan": {
                        new BanHandler().banRemove(event, "event");
                        break;
                    }
                    case "!CloseDelBan": {
                        new BanHandler().banRemove(event, "close");
                        break;
                    }
                    case "!TournamentDelBan": {
                        new BanHandler().banRemove(event, "tournament");
                        break;
                    }
                }
            } else {
                event.getChannel().sendMessage("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432 \u0434\u043b\u044f \u0434\u0430\u043d\u043d\u043e\u0439 \u043a\u043e\u043c\u0430\u043d\u0434\u044b!").queue();
                event.getMessage().delete().queue();
            }
        }
        if (message.startsWith("!StoryBans")) {
            event.getMessage().delete().queue();
            final String id = event.getAuthor().getId();
            new BanHandler().banHistory(event, id);
        }
        if (event.getMessage().getContentRaw().startsWith("!\u041c\u0430\u0441\u0441\u0420\u043e\u043b\u044c")) {
            final Member m = event.getMember();
            final String[] msg = event.getMessage().getContentRaw().split(" ");
            Label_0784:
            {
                if (!this.checkRoles.isOwner(m, event) && !this.checkRoles.isModerator(m, event)) {
                    if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                        break Label_0784;
                    }
                }
                try {
                    final int days = Integer.parseInt(msg[1]);
                    this.massRole.onMassRoleMessage(event, days);
                    return;
                } catch (NumberFormatException e2) {
                    event.getChannel().sendMessage("`\u041f\u0440\u0430\u0432\u0438\u043b\u044c\u043d\u044b\u0439 \u0432\u0430\u0440\u0438\u0430\u043d\u0442 \u043a\u043e\u043c\u0430\u043d\u0434\u044b: !\u041c\u0430\u0441\u0441\u0420\u043e\u043b\u044c [\u041a\u043e\u043b-\u0432\u043e \u0434\u043d\u0435\u0439] [\u0422\u0435\u0433\u0438 \u041f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u0442\u0435\u043b\u0435\u0439] [\u0422\u0435\u0433 \u0420\u043e\u043b\u0438]`").queue();
                    return;
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            event.getChannel().sendMessage("\u0423 \u0432\u0430\u0441 \u043d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u043f\u0440\u0430\u0432...").queue();
        }
    }
}
