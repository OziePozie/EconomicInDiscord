package org.economic.commands;

import org.economic.EconomicBot;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class GiveCommand implements ICommand {
    EconomicBot economicBot;

    UserDAOImplement userDAOImplement = new UserDAOImplement();
    public GiveCommand(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }
    @Override
    public void upsertCommand() {
        JDA jda = economicBot.getJda();
        jda.upsertCommand("give","Передача денег")
                .addOption(OptionType.MENTIONABLE,"username","Пользователь, которому передаете деньги", true)
                .addOption(OptionType.INTEGER, "quantity", "Сколько передать", true)
                .queue();
    }
    @Override
    public void execute(SlashCommandInteractionEvent event) {

        Member authorMember = event.getMember();

        Member getterMember = event.getOption("username").getAsMember();

        long authorID = authorMember.getIdLong();

        long getterID = getterMember.getIdLong();

        int quantity = event.getOption("quantity").getAsInt();

        User author = userDAOImplement.findByID(authorID);

        User getter = userDAOImplement.findByID(getterID);

        if (author == null){
            userDAOImplement.addUser(new User(authorID,0));
        }
        if (getter== null){
            userDAOImplement.addUser(new User(getterID,0));
        }

        author = userDAOImplement.findByID(authorID);

        getter = userDAOImplement.findByID(getterID);

        if (checkBalance(userDAOImplement.getBalance(author), quantity)){
            userDAOImplement.setBalance(author, -quantity);
            userDAOImplement.setBalance(getter, +quantity);
            event
                    .reply(MessageCreateData
                            .fromEmbeds(message(authorMember,getterMember, quantity)))
                    .queue();
        } else event
                .reply(MessageCreateData
                        .fromEmbeds(errorMessage()))
                .setEphemeral(true).queue();

    }

    public MessageEmbed message(Member author, Member getter, int quantity) {
        return new EmbedBuilder()
                .setTitle("Вы успешно передали **"+ getter.getEffectiveName() + "** " +quantity)
                .build();
    }
    public MessageEmbed errorMessage(){
        return new EmbedBuilder()
                .setTitle("У вас недостаточно денег на балансе")
                .build();
    }
    public boolean checkBalance(int balance, int quantity){
        return balance >= quantity;
    }
}
