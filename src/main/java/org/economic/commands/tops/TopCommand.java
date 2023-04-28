package org.economic.commands.tops;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.economic.EconomicBot;
import org.economic.commands.ICommand;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class TopCommand implements ICommand {
    EconomicBot economicBot;

    UserDAOImplement userDAOImplement = new UserDAOImplement();

    public TopCommand(EconomicBot economicBot) {

        this.economicBot = economicBot;
    }

    @Override
    public void upsertCommand() {
        JDA jda = economicBot.getJda();
        jda.upsertCommand("top", "Топ")
                .addSubcommands(new SubcommandData("balance","Баланс"))
                .addSubcommands(new SubcommandData("reputation", "Репутация"))
                .addSubcommands(new SubcommandData("messages", "Сообщения"))
                .queue();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String option = event.getSubcommandName();

        event
                .deferReply()
                .addEmbeds(messageEmbed(userDAOImplement.topTen(option), event.getMember(), event, option)).queue();

    }
    public MessageEmbed messageEmbed(List<User> userList, Member member, SlashCommandInteractionEvent event, String option){
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Top by " + option.toUpperCase())
                .setColor(Color.decode("#2b2d31"))
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setTimestamp(Instant.now())
                .setAuthor(member.getEffectiveName() + "#" + member.getUser().getDiscriminator(),null, member.getEffectiveAvatarUrl());
        int i = 1;


        for (User u:userList.subList(0, 8)) {
            net.dv8tion.jda.api.entities.User tops = event.getJDA().getUserById(u.getId());
            String count = null;
            switch (option) {
                case "reputation" -> count = String.valueOf(userDAOImplement.getReputation(u));
                case "balance" -> count = String.valueOf(userDAOImplement.getBalance(u));
                case "messages" -> count = String.valueOf(userDAOImplement.getMessages(u));
            }
            String m = tops.getName() + "#" + tops.getDiscriminator();
            System.out.println(tops);
            MessageEmbed.Field f = new MessageEmbed.Field("<:lstar_exclusive:1098696194429026375> **" + i++ + ". " + "**" + m, option.substring(0,1).toUpperCase() + option.substring(1) + ": "+ count , false);
            embedBuilder.addField(f);
        }

        embedBuilder.addField("Ваше место в топе: ", String.valueOf(userDAOImplement.getRank(userDAOImplement.findByID(member.getIdLong()), option) + 1), false);
        return embedBuilder.build();
    }

}
