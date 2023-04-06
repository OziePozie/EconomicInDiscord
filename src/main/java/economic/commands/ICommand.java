package economic.commands;

import economic.EconomicBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.Interaction;

public interface ICommand {

     void upsertCommand();
     void execute(SlashCommandInteractionEvent event);

}
