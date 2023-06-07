package org.economic.handlers;

import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.economic.EconomicBot;
import org.economic.triggers.BannerUpdater;

import java.util.HashMap;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class CommandHandler extends ListenerAdapter {

    EconomicBot economicBot;
    BannerUpdater bannerUpdater = new BannerUpdater();

    public CommandHandler(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }


    @Override
    public void onReady(ReadyEvent event) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Long id;
                    try {
                        System.out.println(MessageHandler.messageCounterMapTwoHoursRefresh.get(145439829549645824L));
                        MessageHandler
                                .messageCounterMapTwoHoursRefresh
                                .forEach((aLong, integer) -> System.out.println(aLong + " " + integer));
                        id = MessageHandler.getMessageCounterMapTwoHoursRefresh()
                                .entrySet()
                                .stream()
                                .sorted()
                                .findFirst().get().getKey();
                        System.out.println(id);
                    } catch (Exception e){
                        id = 808613338644742159L;
                    }

                    bannerUpdater.updateActivityMemberOnBanner(event, id);
                    MessageHandler.refreshMap();
                }
                catch (Exception ignored) {
                }
            }

        }, 10000L, 30000L); //7200000 - 2 часа
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    bannerUpdater.updateOnline(event);
                }
                catch (Exception ignored) {
                }
            }
        }, 30000L, 300000L); // 5 минут

    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        String commandName = event.getName();
        System.out.println(event.getCommandId());

        System.out.println(commandName);

        if (commandName.equals("balance")) {
            economicBot.getBalanceCommand().execute(event);
        }
        if (commandName.equals("give")) {
            economicBot.getGiveCommand().execute(event);
        }
        if (commandName.equals("profile")) {
            economicBot.getProfileCommand().execute(event);
        }
        if (commandName.equals("rep")) {
            economicBot.getReputationCommand().execute(event);
        }
        if (commandName.equals("flip")) {
            economicBot.getFlipCommand().execute(event);
        }
        if (commandName.equals("award")) {
            economicBot.getAwardCommand().execute(event);
        }
        if (commandName.equals("top")) {
            economicBot.getTopCommand().execute(event);
        }
        if (commandName.equals("shop") && event.getSubcommandGroup().equals("roles") && event.getSubcommandName().equals("show")) {
            economicBot.getShopCommand().execute(event);
        }
        if (commandName.equals("shop") && event.getSubcommandGroup().equals("roles") && event.getSubcommandName().equals("buy")) {
            economicBot.getShopBuyCommand().execute(event);
        }
        if (commandName.equals("admin") && event.getSubcommandGroup().equals("shop") && event.getSubcommandName().equals("add")) {
            economicBot.getAddRoleToShopCommand().execute(event);
        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        String memberId = event.getMember().getId();
        if (buttonId.contains("shoprole") && buttonId.contains(memberId)) {
            event.getButton().asDisabled();
            economicBot.getShopCommand().buttonExecute(event);
        }
        if (buttonId.contains("buyroleyes")) {
            economicBot.getShopBuyCommand().buttonExecuteSuccess(event);
        }

    }
}
