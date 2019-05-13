package ru.geekbrains.lesson2.server.persistance;

import ru.geekbrains.lesson2.server.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final Connection conn;
    public static List<User> usersDataBase;
    public PreparedStatement preparedStatement;


    public UserRepository(Connection conn) throws SQLException {
        this.conn = conn;
        PreparedStatement preparedStatement = conn.prepareStatement("insert into users(login, password) values (?, ?)");
        this.preparedStatement = preparedStatement;
        usersDataBase = new ArrayList<>();
        String sql = "create table users(id int auto_increment primary key, login varchar(25), password varchar(25));";

        try{
            if((preparedStatement.executeUpdate("show tables") != -1)){
                preparedStatement.executeUpdate(sql);
            }
        }catch (SQLSyntaxErrorException e){
            e.printStackTrace();
        }
    }

    public void insert(User user) {
        try {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean findByLogin(String login) {
        String sql = String.format("select *from users where login = '%s'", login);
        try {
            ResultSet resultSet = preparedStatement.executeQuery(sql);
            if(resultSet.next()) {
                if(login == resultSet.getString(1)){
                    System.out.printf("Пользователь с именем %s найден%n", login);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<User> getAllUsers() {
        String sql = "select *from users";
        try {
            ResultSet resultSet = preparedStatement.executeQuery(sql);
            while(resultSet.next()) {
                int id = resultSet.getInt(1);
                String login = resultSet.getString(2);
                String password = resultSet.getString(3);
                usersDataBase.add(new User(id, login, password));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
