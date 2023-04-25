package org.economic.commands.shopcommands;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.internal.DotenvParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.dv8tion.jda.api.utils.Timestamp;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditData;
import net.dv8tion.jda.internal.requests.restaction.interactions.ReplyCallbackActionImpl;
import org.economic.EconomicBot;
import org.economic.commands.ICommand;
import org.economic.database.shop.Shop;
import org.economic.database.shop.ShopDAOImplement;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class ShopCommand implements ICommand {
    EconomicBot economicBot;

    ShopDAOImplement shopDAOImplement = new ShopDAOImplement();
    Dotenv dotenv = Dotenv.load();

    public ShopCommand(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }

    @Override
    public void upsertCommand() {
        SubcommandGroupData roles = new SubcommandGroupData("roles", "Магазин ролей")
                .addSubcommands(new SubcommandData("buy","Купить роль")
                        .addOption(OptionType.INTEGER, "role", "id Роли для покупки",true))
                .addSubcommands(new SubcommandData("show", "Магазин ролей"));
        economicBot.getJda().upsertCommand("shop","магазин")
                .addSubcommandGroups(roles)
                .setDefaultPermissions(DefaultMemberPermissions.ENABLED)
                .queue();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().applyData(logic(event,0)).queue();
       // event.getHook().sendMessage(logic(event, 0)).queue();

    }

    public void buttonExecute(ButtonInteractionEvent event){

        String array[] = event.getButton().getId().split(":");
        int page = Integer.parseInt(array[0]);
        event.deferEdit().applyCreateData(logic(event, page)).queue();

    }
    public MessageCreateData logic(GenericInteractionCreateEvent event, int page){
        List<Shop> shopList = shopDAOImplement.getListRoles();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.decode("0xc64200"))
                .setTitle("ㅤㅤ<:lstar_exclusive:1098696194429026375>  **SHOP ROLES** <:lstar_exclusive:1098696194429026375> \n")
                .setThumbnail("https://media.discordapp.net/attachments/1097470242357264514/1099209655500156968/Screenshot_75.png")
                .setFooter("Запросил: "+ event.getMember().getEffectiveName(),event.getMember().getEffectiveAvatarUrl())
                .setTimestamp(Instant.now());
        int i = 1;
        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
        for (Shop s:shopList) {
            MessageEmbed.Field f = new MessageEmbed.Field(i++ + ". " + " Цена: **"+ s.getPrice() + "**" + dotenv.get("CURRENCY_EMOJI"),
                    economicBot.getJda().getGuildById("1092817379899211896").getRoleById(s.getRoleId()).getAsMention(), false);
            fields.add(f);
        }
        ArrayList<Button> buttonArrayList = new ArrayList<>();
        int min = Math.min(shopList.size() / 5, 5);
        for (int j = 0; j < min; j++) {
            Button button = Button.success( j + ":shoprole:" + event.getMember().getId(), j+1 + "");
            buttonArrayList.add(button);
            System.out.println(button);
        }

        int start = page * 5;
        for (int k = start; k < start+5; k++) {
            try {
                embedBuilder.addField(fields.get(k));
            } catch (Exception e){
                break;
            }
        }
        MessageCreateData messageCreateData = new MessageCreateBuilder().addEmbeds(embedBuilder.build()).addActionRow(buttonArrayList).build();
        return messageCreateData;
    }

}
