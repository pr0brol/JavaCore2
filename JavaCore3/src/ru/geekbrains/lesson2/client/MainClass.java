package ru.geekbrains.lesson2.client;

import javax.swing.*;

public class MainClass {
    private static MyWindow myWindow;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                myWindow = new MyWindow();
            }
        });
    }
}
