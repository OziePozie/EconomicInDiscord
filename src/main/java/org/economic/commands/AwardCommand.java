package org.economic.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.economic.EconomicBot;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;

import java.awt.*;
import java.time.Instant;

public class AwardCommand implements ICommand {
    EconomicBot economicBot;

    UserDAOImplement userDAOImplement = new UserDAOImplement();
    Dotenv dotenv = Dotenv.load();
    public AwardCommand(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }

    @Override
    public void upsertCommand() {
        JDA jda = economicBot.getJda();
        jda.upsertCommand("award", "Наградить пользователя")
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR, Permission.MANAGE_EVENTS))
                .addOption(OptionType.MENTIONABLE, "username", "Пользователь", true)
                .addOption(OptionType.INTEGER, "quantity", "Награда", true)
                .queue();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member getterMember = event.getOption("username").getAsMember();

        long getterID = getterMember.getIdLong();

        int quantity = event.getOption("quantity").getAsInt();

        User getter = userDAOImplement.findByID(getterID);

        if (getter == null) {
            userDAOImplement.addUser(new User(getterID, 0));
        }
        getter = userDAOImplement.findByID(getterID);

        userDAOImplement.setBalance(getter, +quantity);
        event
                .reply(MessageCreateData
                        .fromEmbeds(message(getterMember, quantity, event.getMember())))
                .queue();
    }

    public MessageEmbed message(Member getter, int quantity, Member author) {
        return new EmbedBuilder()
                .setColor(Color.decode("#2b2d31"))
                .setAuthor(author.getUser().getAsTag(), null, author.getEffectiveAvatarUrl())
                .setTimestamp(Instant.now())
                .setDescription("Вы успешно наградили **" + getter.getUser().getAsTag() + "** " + quantity + dotenv.get("CURRENCY_EMOJI"))
                .build();
    }
}
