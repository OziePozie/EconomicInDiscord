package org.economic.commands.games;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import org.economic.EconomicBot;
import org.economic.commands.ICommand;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;

import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FlipCommand implements ICommand {
    final String gamesPaths = "./Games/Flip/";
    EconomicBot economicBot;
    UserDAOImplement userDAOImplement = new UserDAOImplement();
    Dotenv dotenv = Dotenv.load();

    public FlipCommand(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }

    @Override
    public void upsertCommand() {
        JDA jda = economicBot.getJda();
        OptionData option = new OptionData(OptionType.STRING, "coin",
                "Выберите сторону", true)
                .addChoice("Орел", "Орел")
                .addChoice("Решка", "Решка");
        jda.upsertCommand("flip", "Сыграть в монетку")
                .addOption(OptionType.INTEGER, "quantity", "Ставка", true)
                .addOptions(option).queue();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        Member authorMember = event.getMember();

        long authorID = authorMember.getIdLong();

        int quantity = event.getOption("quantity").getAsInt();

        User author = userDAOImplement.findByID(authorID);


        if (author == null) {
            userDAOImplement.addUser(new User(authorID, 0));
            event.deferReply(true).addContent("У вас недостаточно денег");
        } else if (author.getBalance() < quantity) {
            event.deferReply(true).addContent("У вас недостаточно денег");
        } else {
            boolean win = new Random().nextBoolean();
            String choice = event.getOption("coin").getAsString();
            String difference = choice.equals("Орел") ? "Решка" : "Орел";
            System.out.println(win);
            if (win) {
                File file = new File(gamesPaths + choice + ".gif");

                int winPull = (int) (quantity * 0.75);

                userDAOImplement.setBalance(author, winPull);

                event.deferReply().addFiles(FileUpload.fromData(file)).queue();

                event.getHook().editOriginalEmbeds(messageWin(authorMember, winPull)).queueAfter(5, TimeUnit.SECONDS);
            } else {

                File file = new File(gamesPaths + difference + ".gif");

                userDAOImplement.setBalance(author, -quantity);

                event.deferReply().addFiles(FileUpload.fromData(file)).queue();

                event.getHook().editOriginalEmbeds(messageLose(authorMember, quantity)).queueAfter(5, TimeUnit.SECONDS);

            }
        }

    }

    public MessageEmbed messageWin(Member member, int quantity) {
        return new EmbedBuilder()
                .setColor(Color.decode("#2b2d31"))
                .setAuthor(member.getEffectiveName() + "#" + member.getUser().getDiscriminator(), null, member.getEffectiveAvatarUrl())
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setDescription("Вы выиграли **" + dotenv.get("CURRENCY_EMOJI") + quantity + "** ")
                .build();
    }

    public MessageEmbed messageLose(Member member, int quantity) {
        return new EmbedBuilder()
                .setColor(Color.decode("#2b2d31"))
                .setAuthor(member.getEffectiveName() + "#" + member.getUser().getDiscriminator(), null, member.getEffectiveAvatarUrl())
                .setDescription("Вы проиграли**" + dotenv.get("CURRENCY_EMOJI") + quantity + "** ")
                .build();
    }
}
