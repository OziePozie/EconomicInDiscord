package org.economic.commands.shopcommands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.economic.EconomicBot;
import org.economic.commands.ICommand;
import org.economic.database.shop.Shop;
import org.economic.database.shop.ShopDAOImplement;
import org.economic.database.user.User;
import org.economic.database.user.UserDAOImplement;

import java.util.List;

public class ShopBuyCommand implements ICommand {
    EconomicBot economicBot;

    ShopDAOImplement shopDAOImplement = new ShopDAOImplement();

    UserDAOImplement userDAOImplement = new UserDAOImplement();

    public ShopBuyCommand(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }

    @Override
    public void upsertCommand() {

    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        List<Shop> shopList = shopDAOImplement.getListRoles();
        int id = event.getOption("role").getAsInt() - 1;
        int price = shopList.get(id).getPrice();
        User user = userDAOImplement.findByID(event.getMember().getIdLong());
        if (user.getBalance() < price)
            event.reply("У вас недостаточно валюты на покупку роли").setEphemeral(true).queue();
        else {
            Role role = event.getJDA().getRoleById(shopList.get(id).getRoleId());
            Button buttonYes = Button.primary("buyroleyes:" + role.getId(), Emoji.fromFormatted("<:yes:1099791926275883158>"));
            event.reply("Вы точно хотите купить роль " + role.getAsMention() + " ?").addActionRow(buttonYes).setEphemeral(true).queue();
        }
    }
    public void buttonExecuteSuccess(ButtonInteractionEvent event){
        String[] array = event.getButton().getId().split(":");
        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(array[1])).queue();
        event.reply("Вы успешно купили новую роль").setEphemeral(true).queue();
    }

}
