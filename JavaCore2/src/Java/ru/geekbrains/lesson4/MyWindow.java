package Java.ru.geekbrains.lesson4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Java.ru.geekbrains.lesson4.TextMessage.userTo;

public class MyWindow extends JFrame {

    private final JButton buttonSend;

    private final Container container;

    private final DefaultListModel<TextMessage> listModel;

    private final TextMessageCellRenderer messageCellRenderer;

    private final JList<TextMessage> listText;

    private final JTextField messageField;

    private final JTextField usersField;

    private final Network network;

    public MyWindow(){
        setTitle("Онлайн чат");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300, 400, 350, 400);
        setMinimumSize(new Dimension(350, 400));
        setVisible(true);
        setLayout(new BorderLayout());

        buttonSend = new JButton("отправить");
        buttonSend.setPreferredSize(new Dimension(100, 25));
        container = new Container();
        listModel = new DefaultListModel();
        listText = new JList(listModel);
        add(listText, BorderLayout.CENTER);
        messageCellRenderer = new TextMessageCellRenderer();
        listText.setCellRenderer(messageCellRenderer);

        messageField = new JTextField(14);
        usersField = new JTextField(4);
        messageField.setMinimumSize(new Dimension(100, 30));
        usersField.setMinimumSize(new Dimension(50, 30));
        container.setLayout(new FlowLayout());
        container.add(usersField);
        container.add(messageField);
        container.add(buttonSend);
        add(container, BorderLayout.SOUTH);

        add(new JScrollPane(listText), BorderLayout.CENTER);

        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = messageField.getText();
                if(text != null && !text.trim().isEmpty()){
                    TextMessage msg = new TextMessage(network.getLogin(), userTo, text);
                    listModel.add(listModel.size(), msg);
                    messageField.setText(null);
                    network.sendTextMessage(msg);
                }
            }
        });

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = messageField.getText();
                if(text != null && !text.trim().isEmpty()){
                    TextMessage msg = new TextMessage(network.getLogin(), userTo, text);
                    listModel.add(listModel.size(), msg);
                    messageField.setText(null);
                    network.sendTextMessage(msg);
                }
            }
        };
        messageField.addActionListener(action);

        this.network = new Network("localhost", 7777, this::submitMessage);
        LoginDialog loginDialog = new LoginDialog(this, network);
        loginDialog.setVisible(true);

        if(!loginDialog.isConnected()){
            System.exit(0);
        }
    }

    public void submitMessage(TextMessage message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listModel.add(listModel.size(), message);
                listText.ensureIndexIsVisible(listModel.size() - 1);
            }
        });
    }
}
