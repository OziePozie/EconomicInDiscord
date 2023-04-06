////
//// Decompiled by Procyon v0.5.36
////
//
//package org.example;
//
//import net.dv8tion.jda.api.entities.User;
//import net.dv8tion.jda.api.EmbedBuilder;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.DriverManager;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//public class SQL
//{
//    private Statement stmt;
//    private ResultSet rs;
//    String url;
//    String name;
//    String pass;
//    Connection connection;
//
//    SQL() {
//        this.url = "jdbc:postgresql://localhost:5432/economic";
//        this.name = "postgres";
//        this.pass = "1";
//    }
//
//    public void connect() throws ClassNotFoundException, SQLException {
//        Class.forName("org.postgresql.Driver");
//        final Connection connection = DriverManager.getConnection(this.url, this.name, this.pass);
//        if (connection != null) {
//            System.out.println("Connect");
//        }
//    }
//
//    public void createUsers() throws ClassNotFoundException, SQLException {
//        Class.forName("org.postgresql.Driver");
//        this.connection = DriverManager.getConnection(this.url, this.name, this.pass);
//        this.stmt = this.connection.createStatement();
//        final String create = "CREATE TABLE IF NOT EXISTS users( memberid VARCHAR(255) PRIMARY KEY, balance INT);";
//        final PreparedStatement preparedStatement = this.connection.prepareStatement(create);
//        preparedStatement.execute();
//    }
//
//    public void insertUsers(final String memberid) throws SQLException {
//        final String sql = "INSERT INTO users (memberid,balance) VALUES (?,?) ON CONFLICT (memberid) DO NOTHING;";
//        final PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
//        preparedStatement.setString(1, memberid);
//        preparedStatement.setInt(2, 0);
//        preparedStatement.execute();
//    }
//
//    public int getBalance(final String memberid) throws SQLException {
//        final String balance = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, memberid);
//        final PreparedStatement preparedStatement = this.connection.prepareStatement(balance);
//        this.rs = preparedStatement.executeQuery();
//        return this.rs.getInt(1);
//    }
//
//    public void setBalance(final String memberid, final int value) throws SQLException {
//        final String setValue = "UPDATE users SET balance = ? WHERE memberid = ?;";
//        final PreparedStatement preparedStatement = this.connection.prepareStatement(setValue);
//        preparedStatement.setInt(1, this.getBalance(memberid) + value);
//        preparedStatement.setString(2, memberid);
//        preparedStatement.executeUpdate();
//    }
//
//    public EmbedBuilder getBestBalance() throws SQLException {
//        final String bestBalance = "SELECT * FROM users ORDER BY balance DESC LIMIT 5;";
//        final PreparedStatement preparedStatement = this.connection.prepareStatement(bestBalance);
//        this.rs = preparedStatement.executeQuery();
//        final EmbedBuilder msg = new EmbedBuilder().setTitle("Top balance:");
//        int i = 0;
//        while (this.rs.next()) {
//            this.rs.getInt(2);
//            msg.addField("", invokedynamic(makeConcatWithConstants:(ILjava/lang/String;I)Ljava/lang/String;, ++i, User.fromId(this.rs.getString(1)).getAsMention(), this.rs.getInt(2)), false);
//        }
//        return msg;
//    }
//}
