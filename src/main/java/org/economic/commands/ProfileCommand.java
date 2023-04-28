package org.economic.commands;

import org.economic.EconomicBot;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;
import org.economic.database.userxp.UserXp;
import org.economic.database.userxp.UserXpDAOImplement;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.economic.handlers.VoiceXpHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class ProfileCommand implements ICommand {
    EconomicBot economicBot;

    UserDAOImplement userDAOImplement = new UserDAOImplement();

    UserXpDAOImplement userXpDAOImplement = new UserXpDAOImplement();

    VoiceXpHandler voiceXpHandler = new VoiceXpHandler();
    final String parentDirectory = "./Profile/";
    final String coordsDirectory = "./Profile/Coords/";
    final String cacheDirectory = "./Profile/Cache/";

    final BufferedImage image = ImageIO.read(new File(parentDirectory + "fon.png"));


    public ProfileCommand(EconomicBot economicBot) throws IOException {

        this.economicBot = economicBot;

    }

    @Override
    public void upsertCommand() {
        JDA jda = economicBot.getJda();
        jda.upsertCommand("profile", "Профиль")
                .addOption(OptionType.MENTIONABLE, "username", "Пользователь")
                .queue();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        File file = null;
        MessageCreateData msg = null;

        Member user;

        if (Optional.ofNullable(event.getOption("username")).isEmpty())
            user = event.getMember();
        else user = event.getOption("username").getAsMember();

        long userID = user.getIdLong();

        User userEntity = userDAOImplement.findByID(userID);

        UserXp userXpEntity = userXpDAOImplement.findById(userID);

        if (userEntity == null) {
                userDAOImplement.addUser(new User(userID, 0));

                userEntity = userDAOImplement.findByID(userID);
        }

        if (userXpEntity == null) {

                userXpDAOImplement.addUserXp(new UserXp(userID, 0));

                userXpEntity = userXpDAOImplement.findById(userID);
        }

        int balance = userDAOImplement.getBalance(userEntity);

        int reputation = userDAOImplement.getReputation(userEntity);

        long messages = userDAOImplement.getMessages(userEntity);

        int xp = userXpDAOImplement.getXp(userXpEntity) + VoiceXpHandler.getCountExpBeforeExecuteByDB(user);

        try {
            voiceXpHandler.expCount(user);
        } catch (NullPointerException ignored){

        }

        long rank = userXpDAOImplement.getRank(userXpEntity);

        file = avatar(user, balance, xp, user.getEffectiveName(), reputation, messages, rank);

        ReplyCallbackAction a = replyCallbackAction(file, event);

        a.queue();

        file.delete();
    }
    public MessageEmbed message(Member member, int balance, int xp){

        return new EmbedBuilder()
                .setTitle("Profile " + member.getEffectiveName())
                .setThumbnail(member.getEffectiveAvatarUrl())
                .addField("На вашем счету:" , String.valueOf(balance), true)
                .addField("Заработанный опыт:", String.valueOf(xp), true)
                .build();
    }

    private File avatar(Member member, int balance, int xp, String nick, int reputation, long messages, long rank) {

        try {

            BufferedImage imageMain = ImageIO.read(new File(parentDirectory + "profile1.png"));

            Graphics2D g = image.createGraphics();

            g.setFont(g.getFont().deriveFont(25.0F).deriveFont(Font.BOLD));

            BufferedImage img = ImageIO.read(new URL(member.getEffectiveAvatarUrl() + "?size=256"));

            File tmpAvatar = new File(parentDirectory + "avatar" + member.getId()+".png");

            ImageIO.write(img,"png",  tmpAvatar);

            BufferedImage image1 = ImageIO.read(tmpAvatar);

            Scanner sc = new Scanner(new File(coordsDirectory + "coords.txt"));

            int x = sc.nextInt();

            int y = sc.nextInt();


            g.drawImage(image1.getScaledInstance(200,200, Image.SCALE_AREA_AVERAGING), x,y,null);

            g.drawImage(imageMain, 0,0,null);

            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            balanceProfile(g, balance);

            nicknameProfile(g, nick);

            experienceProfile(g, xp);

            reputationProfile(g, reputation);

            messagesProfile(g, messages);

            rankProfile(g, rank);

            File finalProfile = new File(cacheDirectory + member.getId()+".png");

            ImageIO.write(image,"png", finalProfile);

            try {
                return finalProfile;
            } finally {
                g.dispose();
                tmpAvatar.delete();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



    }

    private void balanceProfile(Graphics g, int balance) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(coordsDirectory + "balancecoords.txt"));

            int balanceX = sc.nextInt();

            int balanceY = sc.nextInt();

            double doubleBalance;

            if (balance > 9999 && balance < 999999) {

                String kk = "k";

                doubleBalance = (double) balance / 1000;
                g.drawString(String.format("%.2f", doubleBalance) + kk, balanceX, balanceY);
            } else if (balance > 999999) {

                doubleBalance = (double) balance / 1000000;

                g.drawString(String.format("%.2f", doubleBalance) + "kk", balanceX, balanceY);

            } else if (balance < 1000) g.drawString(String.valueOf(balance), balanceX + 30, balanceY);
            else g.drawString(String.valueOf(balance), balanceX, balanceY);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private void nicknameProfile(Graphics g, String nick){
        try {
            Scanner sc = new Scanner(new File(coordsDirectory + "nickcoords.txt"));

            int nickX = sc.nextInt();

            int nickY = sc.nextInt();

            if (nick.length() > 14) nick = nick.substring(0,14) + "...";

            if (nick.length() < 10) nickX+=35;

            if (nick.length() < 5) nickX+=40;

            g.drawString(nick, nickX, nickY);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    private void experienceProfile(Graphics g, int totalSecs){
        try {
            Scanner sc = new Scanner(new File(coordsDirectory + "timecoords.txt"));

            int timeX = sc.nextInt();

            int timeY = sc.nextInt();

            int day = (int) TimeUnit.SECONDS.toDays(totalSecs);
            int hours = (int) (TimeUnit.SECONDS.toHours(totalSecs) - (day * 24));
            int minutes =  (int)((int) TimeUnit.SECONDS.toMinutes(totalSecs) - (TimeUnit.SECONDS.toHours(totalSecs)* 60));

            g.drawString(String.format("%dd:%dh:%dm", day, hours, minutes ), timeX, timeY);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    private void reputationProfile(Graphics g, int reputation){
        try {
            Scanner sc = new Scanner(new File(coordsDirectory + "reputationcoords.txt"));

            int timeX = sc.nextInt();

            int timeY = sc.nextInt();

            g.drawString(String.valueOf(reputation), timeX, timeY);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    private void messagesProfile(Graphics g, long messages){
        try {
            Scanner sc = new Scanner(new File(coordsDirectory + "messagescoords.txt"));

            int timeX = sc.nextInt();

            int timeY = sc.nextInt();

            double doubleMessage;
            if (messages < 1000){

                g.drawString(String.valueOf(messages), timeX, timeY);

            } else if (messages >= 999 && messages < 999999) {

                doubleMessage = (double) messages / 1000;

                g.drawString(String.format("%.2fk", doubleMessage), timeX, timeY);

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void rankProfile(Graphics g, long rank){
        try {
            Scanner sc = new Scanner(new File(coordsDirectory + "rankcoords.txt"));

            int timeX = sc.nextInt();

            int timeY = sc.nextInt();

            g.drawString(String.valueOf(rank + 1), timeX, timeY);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    ReplyCallbackAction replyCallbackAction(File file, SlashCommandInteractionEvent event){
        return event.deferReply().addFiles(FileUpload.fromData(file));
    }
}
