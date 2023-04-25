package org.economic;

import org.economic.commands.*;
import org.economic.commands.shopcommands.*;
import org.economic.handlers.CommandHandler;
import org.economic.handlers.MessageHandler;
import org.economic.handlers.VoiceXpHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

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


    public void run() throws IOException {
        jda = JDABuilder.createDefault("MTA4NTk3MzMyMDM4MzYwMjg0OQ.G__dwg.IvMZFtXN6Zo0etVSrhoy52GjI11E9Pk9AsVi2k",
                        GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(new CommandHandler(this), new VoiceXpHandler(), new MessageHandler())
                .setStatus(OnlineStatus.ONLINE)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();

        balanceCommand =  new BalanceCommand(this);
        giveCommand = new GiveCommand(this);
        profileCommand = new ProfileCommand(this);
        reputationCommand = new ReputationCommand(this);
        awardCommand = new AwardCommand(this);
        shopCommand = new ShopCommand(this);
        AddRoleToShopCommand = new AddRoleToShopCommand(this);
        shopBuyCommand = new ShopBuyCommand(this);


        balanceCommand.upsertCommand();
        giveCommand.upsertCommand();
        profileCommand.upsertCommand();
        reputationCommand.upsertCommand();
        awardCommand.upsertCommand();
        shopCommand.upsertCommand();
        AddRoleToShopCommand.upsertCommand();

    }

    public JDA getJda() {
        return jda;
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

    public ReputationCommand getReputationCommand(){
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
