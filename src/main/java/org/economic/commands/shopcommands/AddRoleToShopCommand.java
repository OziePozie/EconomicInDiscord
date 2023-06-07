package org.economic.commands.shopcommands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
import org.economic.EconomicBot;
import org.economic.commands.ICommand;
import org.economic.database.shop.Shop;
import org.economic.database.shop.ShopDAOImplement;

public class AddRoleToShopCommand implements ICommand {
    EconomicBot economicBot;

    ShopDAOImplement shopDAOImplement = new ShopDAOImplement();

    public AddRoleToShopCommand(EconomicBot economicBot) {
        this.economicBot = economicBot;
    }


    @Override
    public void upsertCommand() {
        SubcommandGroupData adminRoles = new SubcommandGroupData("shop", "Работа с магазином");
        economicBot.getJda().upsertCommand("admin", "Настройка сервера")
                .addSubcommandGroups(adminRoles
                        .addSubcommands(
                                new SubcommandData("add", "Добавить роль в магазин")
                                        .addOption(OptionType.ROLE, "role", "Роль", true)
                                        .addOption(OptionType.INTEGER, "price", "Цена", true))
                )
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                .queue();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Shop shop = new Shop(event.getOption("role").getAsLong(),
                event.getOption("price").getAsInt());
        shopDAOImplement
                .addRoleToShop(shop);
        assert shopDAOImplement.isRoleInShop(shop);
        event.reply("Успешно добавлена роль").setEphemeral(true).queue();
        System.out.println(shopDAOImplement.getListRoles());
    }
}
