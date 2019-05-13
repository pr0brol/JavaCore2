package ru.geekbrains.lesson2.server.auth;

import ru.geekbrains.lesson2.client.RegException;
import ru.geekbrains.lesson2.server.User;

import java.sql.SQLException;

public interface AuthService {
    boolean authUser(User user) throws SQLException, RegException;
}