package economic.commands;

import economic.EconomicBot;
import economic.database.user.User;
import economic.database.user.UserDAOImplement;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class BalanceCommand implements ICommand{
    EconomicBot economicBot;

    UserDAOImplement userDAOImplement = new UserDAOImplement();
    public BalanceCommand(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }

    @Override
    public void upsertCommand() {
        JDA jda = economicBot.getJda();
        jda.upsertCommand("balance","Проверка баланса")
                .addOption(OptionType.MENTIONABLE,"username","Пользователь", true)
                .queue();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        Member user = event.getOption("username").getAsMember();

        long userID = user.getIdLong();

        User userEntity = userDAOImplement.findByID(userID);

        if (userEntity == null) {

            userDAOImplement.addUser(new User(userID, 0));

        }
        event.reply(MessageCreateData
                .fromEmbeds(message(event, userDAOImplement.getBalance(userEntity), user)))
                .queue();

    }

    public MessageEmbed message(SlashCommandInteractionEvent event, int balance, Member user) {
        Channel channel = event.getChannel();
        return new EmbedBuilder()
                .setTitle("Balance " + user.getEffectiveName())
                .setThumbnail(user.getEffectiveAvatarUrl())
                .addField("На вашем счету:" , String.valueOf(balance), true)
                .build();
    }
}
