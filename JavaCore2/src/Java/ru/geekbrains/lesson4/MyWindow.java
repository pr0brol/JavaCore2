package Java.ru.geekbrains.lesson4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyWindow extends JFrame {
    public MyWindow(){
        setTitle("Online Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300, 400, 350, 400);
        setMinimumSize(new Dimension(350, 400));
        setVisible(true);
        setLayout(new BorderLayout());

        JButton buttonSend = new JButton("отправить");
        buttonSend.setPreferredSize(new Dimension(100, 25));
        Container container = new Container();

        DefaultListModel listModel = new DefaultListModel();
        JList listText = new JList(listModel);
        add(listText, BorderLayout.CENTER);

        JTextField textField = new JTextField(20);
        textField.setMinimumSize(new Dimension(150, 30));
        container.setLayout(new FlowLayout());
        container.add(textField);
        container.add(buttonSend);
        add(container, BorderLayout.SOUTH);

        add(new JScrollPane(listText), BorderLayout.CENTER);

        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.addElement(textField.getText());
                textField.setText("");
            }
        });

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.addElement(textField.getText());
                textField.setText("");
            }
        };
        textField.addActionListener(action);
    }
}
