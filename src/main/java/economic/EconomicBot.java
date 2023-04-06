package economic;

import economic.commands.BalanceCommand;
import economic.commands.GiveCommand;
import economic.handlers.CommandHandler;
import economic.handlers.VoiceXpHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class EconomicBot {
    JDA jda;
    BalanceCommand balanceCommand;
    GiveCommand giveCommand;
    public void run(){
        jda = JDABuilder.createDefault("MTA4NTk3MzMyMDM4MzYwMjg0OQ.G__dwg.IvMZFtXN6Zo0etVSrhoy52GjI11E9Pk9AsVi2k",
                        GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(new CommandHandler(this), new VoiceXpHandler())
                .setStatus(OnlineStatus.ONLINE)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();
        balanceCommand =  new BalanceCommand(this);
        giveCommand = new GiveCommand(this);
    }

    public JDA getJda() {
        return jda;
    }

    public BalanceCommand getBalanceCommand() {
        return balanceCommand;
    }

    public GiveCommand getGiveCommand() {
        return giveCommand;
    }
}
