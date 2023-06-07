package org.economic;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.economic.commands.*;
import org.economic.commands.games.FlipCommand;
import org.economic.commands.shopcommands.AddRoleToShopCommand;
import org.economic.commands.shopcommands.ShopBuyCommand;
import org.economic.commands.shopcommands.ShopCommand;
import org.economic.commands.tops.TopCommand;
import org.economic.handlers.CommandHandler;
import org.economic.handlers.MessageHandler;
import org.economic.handlers.VoiceXpHandler;

import java.io.IOException;

public class EconomicBot {
    JDA jda;
    BalanceCommand balanceCommand;
    GiveCommand giveCommand;
    ProfileCommand profileCommand;
    ReputationCommand reputationCommand;
    AwardCommand awardCommand;
    ShopCommand shopCommand;
    AddRoleToShopCommand AddRoleToShopCommand;
    ShopBuyCommand shopBuyCommand;
    FlipCommand flipCommand;
    TopCommand topCommand;

    Dotenv dotenv = Dotenv.load();

    public void run() throws IOException {
        jda = JDABuilder.createDefault(dotenv.get("BOT_TOKEN"),
                        GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(new CommandHandler(this), new VoiceXpHandler(), new MessageHandler())
                .setStatus(OnlineStatus.ONLINE)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();

        balanceCommand = new BalanceCommand(this);
        giveCommand = new GiveCommand(this);
        profileCommand = new ProfileCommand(this);
        reputationCommand = new ReputationCommand(this);
        awardCommand = new AwardCommand(this);
        shopCommand = new ShopCommand(this);
        AddRoleToShopCommand = new AddRoleToShopCommand(this);
        shopBuyCommand = new ShopBuyCommand(this);
        flipCommand = new FlipCommand(this);
        topCommand = new TopCommand(this);


        balanceCommand.upsertCommand();
        giveCommand.upsertCommand();
        profileCommand.upsertCommand();
        reputationCommand.upsertCommand();
        awardCommand.upsertCommand();
        shopCommand.upsertCommand();
        AddRoleToShopCommand.upsertCommand();
        flipCommand.upsertCommand();
        topCommand.upsertCommand();
    }

    public JDA getJda() {
        return jda;
    }

    public FlipCommand getFlipCommand() {
        return flipCommand;
    }

    public TopCommand getTopCommand() {
        return topCommand;
    }

    public ShopBuyCommand getShopBuyCommand() {
        return shopBuyCommand;
    }

    public BalanceCommand getBalanceCommand() {
        return balanceCommand;
    }

    public GiveCommand getGiveCommand() {
        return giveCommand;
    }

    public ProfileCommand getProfileCommand() {
        return profileCommand;
    }

    public ReputationCommand getReputationCommand() {
        return reputationCommand;
    }

    public AwardCommand getAwardCommand() {
        return awardCommand;
    }

    public ShopCommand getShopCommand() {
        return shopCommand;
    }

    public AddRoleToShopCommand getAddRoleToShopCommand() {
        return AddRoleToShopCommand;
    }
}
