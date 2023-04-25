package org.economic.handlers;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.economic.EconomicBot;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandHandler extends ListenerAdapter {

    EconomicBot economicBot;

    public CommandHandler(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }



    @Override
    public void onReady(ReadyEvent event) {

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        
        String commandName = event.getName();
        System.out.println(event.getCommandId());

        System.out.println(commandName);

        if (commandName.equals("balance")){
            economicBot.getBalanceCommand().execute(event);
        }
        if (commandName.equals("give")){
            economicBot.getGiveCommand().execute(event);
        }
        if (commandName.equals("profile")){
            economicBot.getProfileCommand().execute(event);
        }
        if (commandName.equals("rep")){
            economicBot.getReputationCommand().execute(event);
        }
        if(commandName.equals("award")){
            economicBot.getAwardCommand().execute(event);
        }
        if(commandName.equals("shop") && event.getSubcommandGroup().equals("roles") && event.getSubcommandName().equals("show")){
            economicBot.getShopCommand().execute(event);
        }
        if (commandName.equals("shop") && event.getSubcommandGroup().equals("roles") && event.getSubcommandName().equals("buy")){
            economicBot.getShopBuyCommand().execute(event);
        }
        if (commandName.equals("admin") && event.getSubcommandGroup().equals("shop") && event.getSubcommandName().equals("add") ){
            economicBot.getAddRoleToShopCommand().execute(event);
        }

    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        String memberId = event.getMember().getId();
        if (buttonId.contains("shoprole") && buttonId.contains(memberId)){
            event.getButton().asDisabled();
            economicBot.getShopCommand().buttonExecute(event);
        }
        if (buttonId.contains("buyroleyes")){
            economicBot.getShopBuyCommand().buttonExecuteSuccess(event);
        }

    }
}
