package ru.geekbrains.lesson2.server.auth;

import ru.geekbrains.lesson2.client.RegException;
import ru.geekbrains.lesson2.server.User;
import ru.geekbrains.lesson2.server.persistance.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthServiceJdbcImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceJdbcImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public boolean authUser(User user) throws SQLException, RegException {

        String sql = String.format("select *from users where login = '%s'", user.getLogin());
        ResultSet resultSet = userRepository.preparedStatement.executeQuery(sql);
        String login = null, pass = null;
        if(resultSet.next()){
            login = resultSet.getString(2);
            pass = resultSet.getString(3);
        }
        if(user.getLogin().equals(login) && user.getPassword().equals(pass)){return true;}

        return false;
    }
}
