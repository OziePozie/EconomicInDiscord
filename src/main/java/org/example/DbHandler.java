package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;


public class DbHandler {
    String url = "jdbc:postgresql://194.87.93.64:5432/femaledb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow";
    String name = "female";
    String pass = "1";
    Dotenv dotenv = Dotenv.load();
    JDA jda;
    Guild guild;
    dayFormat dayFormat = new dayFormat();
    private Statement stmt;
    private ResultSet rs;

    public void createUsers() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
        this.stmt = connection.createStatement();
        String create = "CREATE TABLE IF NOT EXISTS users (memberid bigint PRIMARY KEY, eventbancount INT DEFAULT 0, closebancount INT DEFAULT 0, tournamentbancount INT DEFAULT 0);";
        PreparedStatement preparedStatement = connection.prepareStatement(create);
        preparedStatement.execute();
        preparedStatement.close();
        connection.close();
    }

    public void createCloseBansTable() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
        this.stmt = connection.createStatement();
        String create = "CREATE TABLE IF NOT EXISTS closebans (id SERIAL PRIMARY KEY, memberid bigint, date timestamp with time zone, reason VARCHAR(255), days INT);SET time zone 'Europe/Moscow';";
        PreparedStatement preparedStatement1 = connection.prepareStatement(create);
        preparedStatement1.execute();
        preparedStatement1.close();
        connection.close();
    }

    public void createEventBansTable() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
        this.stmt = connection.createStatement();
        String create = "CREATE TABLE IF NOT EXISTS eventbans (id SERIAL PRIMARY KEY, memberid bigint, date timestamp with time zone, reason VARCHAR(255), days INT);SET time zone 'Europe/Moscow';";
        PreparedStatement preparedStatement1 = connection.prepareStatement(create);
        preparedStatement1.execute();
        preparedStatement1.close();
        connection.close();
    }

    public void createTournamentBansTable() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
        this.stmt = connection.createStatement();
        String create = "CREATE TABLE IF NOT EXISTS tournamentbans (id SERIAL PRIMARY KEY, memberid bigint, date timestamp with time zone, reason VARCHAR(255), days INT);SET time zone 'Europe/Moscow';";
        PreparedStatement preparedStatement1 = connection.prepareStatement(create);
        preparedStatement1.execute();
        preparedStatement1.close();
        connection.close();
    }

    public void createSchedulerBansTable() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
        this.stmt = connection.createStatement();
        String create = "CREATE TABLE IF NOT EXISTS tournamentscheduler (memberid bigint PRIMARY KEY, role VARCHAR(255), expiredate timestamp with time zone) ;SET time zone 'Europe/Moscow';";
        String create1 = "CREATE TABLE IF NOT EXISTS eventscheduler (memberid bigint PRIMARY KEY, role VARCHAR(255), expiredate timestamp with time zone) ;SET time zone 'Europe/Moscow';";
        String create2 = "CREATE TABLE IF NOT EXISTS closescheduler (memberid bigint PRIMARY KEY, role VARCHAR(255), expiredate timestamp with time zone) ;SET time zone 'Europe/Moscow';";
        PreparedStatement preparedStatement1 = connection.prepareStatement(create);
        PreparedStatement preparedStatement2 = connection.prepareStatement(create1);
        PreparedStatement preparedStatement3 = connection.prepareStatement(create2);
        preparedStatement1.execute();
        preparedStatement2.execute();
        preparedStatement3.execute();
        preparedStatement1.close();
        preparedStatement2.close();
        preparedStatement3.close();
        connection.close();
    }

    public void createSchedulerMassRole() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
        this.stmt = connection.createStatement();
        String create = "CREATE TABLE IF NOT EXISTS massrolescheduler (memberid bigint, role VARCHAR(255), expiredate timestamp with time zone, CONSTRAINT uq_memberandrole UNIQUE (memberid,role));SET time zone 'Europe/Moscow';";
        PreparedStatement preparedStatement = connection.prepareStatement(create);
        preparedStatement.execute();
        preparedStatement.close();
        connection.close();
    }

    public void updateMassRoleScheduler(String memberid, String role, int days) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
        if (connection != null) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().plusDays(days));
            String sql = "INSERT INTO massrolescheduler (memberid, role, expiredate) VALUES (?,?,?) ON CONFLICT (memberid,role) DO UPDATE SET expiredate=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, Long.parseLong(memberid));
            preparedStatement.setString(2, role);
            preparedStatement.setTimestamp(3, timestamp);
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        }
    }

    public Set<MemberAndRole> selectFromMassRoleScheduler() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Set<MemberAndRole> hashMap = new HashSet<>();
        try {
            Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
            if (connection != null) {
                String sql = "SELECT memberid,role from massrolescheduler WHERE expiredate < NOW()";
                this.stmt = connection.createStatement();
                ResultSet rs = this.stmt.executeQuery(sql);
                while (rs.next()) {
                    String memberid = rs.getString("memberid");
                    String role = rs.getString("role");
                    hashMap.add(new MemberAndRole(memberid, role));
                }
                String deleteSql = "DELETE from massrolescheduler WHERE expiredate < NOW()";
                PreparedStatement ps1 = connection.prepareStatement(deleteSql);
                ps1.execute();
                ps1.close();
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hashMap;
    }

    public void insertUsers(String memberid) throws SQLException {
        Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
        String sql = "INSERT INTO users (memberid) VALUES (?) ON CONFLICT (memberid) DO NOTHING;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, Long.parseLong(memberid));
        preparedStatement.execute();
        preparedStatement.close();
        connection.close();
    }

    public int selectEventBanCount(String memberName, String ban_type) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try {
            Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
            if (connection != null) {
                System.out.println("Connect");
                this.stmt = connection.createStatement();
                this.rs = this.stmt.executeQuery("SELECT " + ban_type + "bancount FROM users WHERE memberid = '" + memberName + "'");
                if (this.rs.next()) {
                    int eventbancount = this.rs.getInt(1);
                    System.out.println(eventbancount);
                    connection.close();
                    return eventbancount;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public String selectName(String memberName) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try {
            Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
            if (connection != null) {
                System.out.println("Connect");
                this.stmt = connection.createStatement();
                this.rs = this.stmt.executeQuery("SELECT name FROM users WHERE memberid = '" + memberName + "'");
                if (this.rs.next()) {
                    String name = this.rs.getString(1);
                    System.out.println(name);
                    connection.close();
                    return name;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public String selectIdName(int id, String ban_type) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try {
            Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
            if (connection != null) {
                System.out.println("Connect");
                this.stmt = connection.createStatement();
                this.rs = this.stmt.executeQuery("SELECT memberid FROM " + ban_type + "bans WHERE id = " + id);
                if (this.rs.next()) {
                    String nameid = this.rs.getString(1);
                    connection.close();
                    return nameid;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateScheduler(String memberid, String role, int days, String banType) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
        if (connection != null) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().plusDays(days));
            String sql = "INSERT INTO " + banType + "scheduler (memberid, role, expiredate) VALUES (?,?,?) ON CONFLICT (memberid) DO UPDATE SET expiredate=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, Long.parseLong(memberid));
            preparedStatement.setString(2, role);
            preparedStatement.setTimestamp(3, timestamp);
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        }
    }

    public Map<String, String> selectFromScheduler(String banType) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        Map<String, String> hashMap = new HashMap<>();
        try {
            Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
            if (connection != null) {
                String sql = "SELECT memberid,role from " + banType + "scheduler WHERE expiredate < NOW()";
                this.stmt = connection.createStatement();
                ResultSet rs = this.stmt.executeQuery(sql);
                while (rs.next()) {
                    String memberid = rs.getString("memberid");
                    String role = rs.getString("role");
                    hashMap.put(memberid, role);
                }
                String deleteSql = "DELETE from " + banType + "scheduler WHERE expiredate < NOW()";
                PreparedStatement ps1 = connection.prepareStatement(deleteSql);
                ps1.execute();
                ps1.close();
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hashMap;
    }

    public void updateBanCountSQL(String memberName, String reason, int days, String ban_type) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try {
            Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
            if (connection != null) {
                System.out.println("Connect");
                this.stmt = connection.createStatement();
                this.stmt.execute("UPDATE users SET " + ban_type + "bancount = " + ban_type + "bancount +1WHERE memberid = '" + memberName + "'");
                this.stmt = connection.createStatement();
                this.stmt.execute("INSERT INTO " + ban_type + "bans (memberid,date,reason,days) VALUES('" + memberName + "' ,(NOW()),'" + reason + "'," + days + ")");
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public EmbedBuilder selectEventBanHistory(String memberName, String ban_type) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        EmbedBuilder msg = new EmbedBuilder();
        try {
            Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
            if (connection != null) {
                this.stmt = connection.createStatement();
                this.rs = this.stmt.executeQuery("SELECT id,date::timestamptz at time zone 'Europe/Moscow',reason,days FROM " + ban_type + "bans WHERE memberid = '" + memberName + "'\nORDER BY date Desc \nLIMIT 10");
                while (this.rs.next()) {
                    int id = this.rs.getInt(1);
                    Timestamp timeStamp = this.rs.getTimestamp(2);
                    int days = this.rs.getInt(4);
                    String reason = this.rs.getString(3);
                    connection.close();
                    msg.addField("", "**#" + id + ". **" + timeStamp

                            .toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + ". **" + days + " " + this.dayFormat
                            .numberDayFormat(days) + "\n **" + reason, false);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return msg;
    }

    public void removeEventBan(String memberName, int id, String ban_type) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        try {
            Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
            if (connection != null) {
                System.out.println("Connect");
                this.stmt = connection.createStatement();
                this.stmt.execute("UPDATE users SET " + ban_type + "bancount = " + ban_type + "bancount -1WHERE memberid = '" + memberName + "'");
                this.stmt.execute("DELETE FROM " + ban_type + "bans WHERE id = " + id + " ");
                connection.close();
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void embedEventBanList(MessageReceivedEvent event) throws SQLException, ClassNotFoundException {
        Member memberWithBan;
        String message = event.getMessage().getContentRaw();
        String[] eventBan = message.split(" ");
        try {
            memberWithBan = event.getMessage().getMentions().getMembers().get(0);
        } catch (IndexOutOfBoundsException e) {
            memberWithBan = event.getMember();
        }
        EmbedBuilder eb = new EmbedBuilder();
        String authorId = event.getMember().getId();
        String memberBan = memberWithBan.getId();
        eb.setTitle(memberWithBan.getEffectiveName());
        eb.setDescription("");
        Button buttonEvent = Button.success(authorId + ":event:" + authorId, "Event Bans");
        Button buttonClose = Button.success(authorId + ":close:" + authorId, "Close Bans");
        Button buttonTournament = Button.success(authorId + ":tournament:" + authorId, "Tournament Bans");
        eb.setFooter(event.getAuthor().getName());
        event.getChannel().sendMessageEmbeds(eb.build(), new MessageEmbed[0])
                .addActionRow(new ItemComponent[]{buttonEvent, buttonClose, buttonTournament}).queue();
    }

    public List<MessageEmbed.Field> fieldListBans(String banType, String memberBan) throws ClassNotFoundException {
        return selectEventBanHistory(memberBan, banType).getFields();
    }

    public MessageEmbed eventBanSelect(String ban_type, String memberBan, Member memberWithBan, ButtonInteractionEvent event, String member) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(banTypeToRus(ban_type) + " " + banTypeToRus(ban_type));
        try {
            if (selectEventBanCount(memberBan, ban_type) > 0) {
                eb.setDescription("Всего: " + selectEventBanCount(memberBan, ban_type) + " " + banTypeToRus(ban_type));
            } else {
                eb.setDescription("Всего: " + banTypeToRus(ban_type));
            }
            List<MessageEmbed.Field> fieldList = fieldListBans(ban_type, memberBan);
            for (MessageEmbed.Field field : fieldList)
                eb.addField(field);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        eb.setFooter(event.getGuild().getMemberById(member).getEffectiveName());
        eb.setThumbnail(event.getGuild().getIconUrl());
        eb.setColor(Color.blue);
        return eb.build();
    }

    public String banTypeToRus(String ban_type) {
        switch (ban_type) {
            case "close":

            case "tournament":

            case "event":

        }
        return

                ban_type;
    }

    public String banTypeToRusSimple(String ban_type) {
        switch (ban_type) {
            case "close":

            case "tournament":

            case "event":

        }
        return

                ban_type;
    }
}
