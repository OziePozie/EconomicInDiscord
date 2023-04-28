package org.economic.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import org.economic.EconomicBot;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.time.Duration;

public class ReputationCommand implements ICommand{

    EconomicBot economicBot;

    UserDAOImplement userDAOImplement = new UserDAOImplement();

    public ReputationCommand(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }

    @Override
    public void upsertCommand() {
        JDA jda = economicBot.getJda();
        jda.upsertCommand("rep","Поднять репутацию пользователю")
                .addOption(OptionType.MENTIONABLE,"username","Пользователь", true)
                .queue();
    }
//TODO Добавить кулдаун на команду
    @Override
    public void execute(SlashCommandInteractionEvent event) {

        Member giverReputation = event.getMember();

        Member user = event.getOption("username").getAsMember();

        long userID = user.getIdLong();

        long giverID = giverReputation.getIdLong();

        User botUser = userDAOImplement.findByID(userID);

        User giver = userDAOImplement.findByID(giverID);

        if (giver == (null)){
            userDAOImplement.addUser(new User(giverID, 0));
            giver = userDAOImplement.findByID(giverID);
        }

        Duration cooldown = userDAOImplement.getCooldown(giver);

        if (event.getMember().equals(user)){
            event.reply("Вы не можете дать репутацию самому себе").queue();
        } else if (cooldown.equals(Duration.ZERO)) {

            if (botUser == (null)){

                userDAOImplement.addUser(new User(userID, 0));

                botUser = userDAOImplement.findByID(userID);
            }

            userDAOImplement.setReputation(userDAOImplement.findByID(userID));

            userDAOImplement.setCooldown(giver);

            event.replyEmbeds(successMessage(user, userDAOImplement.getReputation(botUser) + 1, giverReputation).build()).queue();
            System.out.println(successMessage(user, userDAOImplement.getReputation(botUser) + 1, giverReputation).build().getAuthor().getName());
        } else {
            long totalSecs = Math.abs(cooldown.toSeconds());
            int hours = (int) (totalSecs / 3600);
            int minutes = (int) ((totalSecs % 3600) / 60);
            int seconds = (int) (totalSecs % 60);
            event.reply(String.format("Подождите %02d:%02d:%02d перед тем как снова поднять репутацию",hours,minutes,seconds)).setEphemeral(true).queue();
        }
    }

    public EmbedBuilder successMessage(Member member, int rep, Member giver){
        return new EmbedBuilder()
                .setColor(Color.decode("#2b2d31"))
                .setThumbnail(member.getEffectiveAvatarUrl())
                .setDescription("**<a:XX_Tenderly_52:1098878483603669012> Вы подняли репутацию** *" + member.getEffectiveName() + "#" + member.getUser().getDiscriminator()
                        + ".* \n **Вы сможете повторить через 12 часов.**")
                .setAuthor(giver.getEffectiveName() + "#" + giver.getUser().getDiscriminator(),null, giver.getEffectiveAvatarUrl())
                .setFooter("Теперь у " + member.getEffectiveName() + " " + rep + " репутации", member.getEffectiveAvatarUrl());
    }
}
