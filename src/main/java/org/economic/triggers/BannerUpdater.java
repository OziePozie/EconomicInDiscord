package org.economic.triggers;

import de.cerus.jgif.GifImage;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.managers.GuildManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BannerUpdater {
    Dotenv dotenv = Dotenv.load();

    Guild guild;

    public void updateOnline(GenericEvent event){

        this.guild = event.getJDA().getGuildById(this.dotenv.get("GUILD_ID"));

        assert this.guild != null;

        GuildManager gm = this.guild.getManager();

        long online = this.guild.getVoiceStates().stream().filter(GuildVoiceState::inAudioChannel).count();

        System.out.println(online);

        onlineStatusUpdate(online);

        try {

            System.out.println("Смена баннера:");

            gm.setBanner(Icon.from(new File("output.gif"))).queueAfter(10L, TimeUnit.SECONDS);

            System.out.println(LocalDateTime.now(ZoneOffset.ofHours(3)) + " Смена баннера успешна Онлайн = " + LocalDateTime.now(ZoneOffset.ofHours(3)));

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    public void updateActivityMemberOnBanner(GenericEvent event, Long id) throws IOException {

        GifImage image = new GifImage();
        GifImage finalImage = new GifImage();

        finalImage.setOutputFile(new File("bannerWithMember.gif"));

        Scanner sc = new Scanner(new File("coords.txt"));

        BufferedImage img = ImageIO.read(new URL(event.getJDA().getUserById(id).getEffectiveAvatarUrl() + "?size=256"));

        image.loadFrom(new File("servbannerGIF.gif"));

        finalImage.setDelay(400);

        int x = sc.nextInt();

        int y = sc.nextInt();

        for(int i = 0; i < image.getFrames().size(); i++){
            BufferedImage frame = image.getFrame(i);
            Graphics2D graphics2D = frame.createGraphics();
            graphics2D.drawImage(img.getScaledInstance(200, 200, Image.SCALE_AREA_AVERAGING), x, y, null);
            finalImage.addFrame(frame);
        }
        finalImage.repeatInfinitely(true);
        finalImage.save();
        image.finish();
        finalImage.finish();
    }

    public void onlineStatusUpdate(Long online) {

        GifImage image = new GifImage();
        GifImage outputImage = new GifImage();
        outputImage.setOutputFile(new File("output.gif"));
        try {
            image.loadFrom(new File("bannerWithMember.gif"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        outputImage.setDelay(400);
        for (int i = 0; i < image.getFrames().size() - 8; i++) {
            BufferedImage frame = image.getFrame(i);
            Graphics2D graphics =  frame.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setFont(graphics.getFont().deriveFont(50.0F).deriveFont(Font.BOLD));
            if (online < 10L)
                graphics.drawString("0" + online, 100, 493);
            if (online >= 10L)
                graphics.drawString(String.valueOf(online), 100, 493);

            outputImage.addFrame(frame);

        }

        outputImage.repeatInfinitely(true);
        outputImage.save();
        image.finish();
        outputImage.finish();

    }

}
