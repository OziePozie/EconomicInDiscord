package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class BanHandler {
    Dotenv dotenv = Dotenv.load();

    JDA jda;

    Guild guild;

    CheckRoles checkRoles = new CheckRoles();

    dayFormat dayFormat = new dayFormat();

    DbHandler dbHandler = new DbHandler();

    public void banApply(MessageReceivedEvent event, String ban_type) {
        this.guild = event.getJDA().getGuildById(event.getGuild().getId());
        String message = event.getMessage().getContentRaw();
        Member author = event.getMember();
        try {
            String[] eventBan = message.split(" ");
            String memberBan = eventBan[1].substring(2, eventBan[1].length() - 1);
            this.dbHandler.insertUsers(memberBan);
            Role banRole = this.guild.getRoleById(this.dotenv.get(ban_type.toUpperCase() + "_BAN_ROLE"));
            Member memberWithBan = this.guild.getMemberById(memberBan);
            int reasonIndex = message.indexOf(eventBan[3]);
            String reason = message.substring(reasonIndex);
            int parseTime = Integer.parseInt(eventBan[2]);
            try {
                event.getGuild().addRoleToMember(UserSnowflake.fromId(memberBan), banRole).queue();
                this.dbHandler.updateBanCountSQL(memberBan, reason, parseTime, ban_type);
                this.dbHandler.updateScheduler(memberBan, banRole.getId(), parseTime, ban_type);
                event.getMessage().getChannel().sendMessage("Пользователь " + memberWithBan.getAsMention() + " получил " + this.dbHandler
                        .selectEventBanCount(memberBan, ban_type) + " бан " + this.dbHandler.banTypeToRus(ban_type) + " на " + parseTime + " " + this.dayFormat.numberDayFormat(parseTime) + ".").queue();
            } catch (ClassNotFoundException | SQLException e) {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage("!" + this.dbHandler.banTypeToRusSimple(ban_type) + "Ban @Tag [Время в днях] [причина]").queue();
                throw new RuntimeException(e);
            }
            event.getMessage().delete().queue();
        } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException | NumberFormatException e) {
            event.getChannel().sendMessage("!" + this.dbHandler.banTypeToRusSimple(ban_type) + "Ban @Tag [Время в днях] [причина]").queue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void banRemove(MessageReceivedEvent event, String ban_type) {
        this.guild = event.getJDA().getGuildById(event.getGuild().getId());
        String[] messageSplitted = event.getMessage().getContentRaw().split(" ");
        Role banRole = this.guild.getRoleById(this.dotenv.get(ban_type.toUpperCase() + "_BAN_ROLE"));
        int id = Integer.parseInt(messageSplitted[1]);
        try {
            String memberBannedName = this.dbHandler.selectIdName(id, ban_type);
            this.dbHandler.removeEventBan(memberBannedName, id, ban_type);
            event.getGuild().removeRoleFromMember(UserSnowflake.fromId(memberBannedName), banRole).queue();
            event.getMessage().getChannel().sendMessage("Убран" + this.dbHandler.banTypeToRus(ban_type) + " бан у пользователя " + event
                    .getGuild().getMemberById(memberBannedName).getAsMention() + ". Теперь у него " + this.dbHandler
                    .selectEventBanCount(memberBannedName, ban_type) + " " + this.dbHandler.banTypeToRus(ban_type) + " банов.").queue();
            event.getMessage().delete().queue();
        } catch (SQLException | ClassNotFoundException e) {
            event.getChannel().sendMessage("Скорее всего такого" + this.dbHandler.banTypeToRus(ban_type) + "бана не существует...").queue();
            event.getMessage().delete().queue();
        }
    }

    public void banHistory(MessageReceivedEvent event, String id) {
        try {
            this.dbHandler.insertUsers(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            this.dbHandler.embedEventBanList(event);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}