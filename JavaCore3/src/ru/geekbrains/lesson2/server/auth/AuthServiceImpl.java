package ru.geekbrains.lesson2.server.auth;

import ru.geekbrains.lesson2.client.RegException;
import ru.geekbrains.lesson2.server.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl implements AuthService {

    public Map<String, String> users = new HashMap<>();

    public AuthServiceImpl(){
        users.put("ivan", "123");
        users.put("petr", "456");
        users.put("jul", "789");
    }
    @Override
    public boolean authUser(User user){
        String pwd = users.get(user.getLogin());
        return pwd !=null && pwd.equals(user.getPassword());
    }

    @Override
    public boolean regUser(User user) throws RegException, SQLException {
        return false;
    }
}



