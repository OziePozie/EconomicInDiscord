package economic.handlers;

import economic.EconomicBot;
import economic.commands.BalanceCommand;
import economic.commands.GiveCommand;
import economic.commands.ICommand;
import economic.utils.ExpController;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {

    EconomicBot economicBot;

    public CommandHandler(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }



    @Override
    public void onReady(ReadyEvent event) {

        new BalanceCommand(economicBot).upsertCommand();

        new GiveCommand(economicBot).upsertCommand();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        if (commandName.equals("balance")){
            economicBot.getBalanceCommand().execute(event);
        }
        if (commandName.equals("give")){
            economicBot.getGiveCommand().execute(event);
        }
    }



}
