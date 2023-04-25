package org.economic.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface ICommand {

     void upsertCommand();
     void execute(SlashCommandInteractionEvent event);

}
