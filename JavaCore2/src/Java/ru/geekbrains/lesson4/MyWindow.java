package Java.ru.geekbrains.lesson4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MyWindow extends JFrame implements MessageReciever{

    private final JList<TextMessage> messageList;

    private final DefaultListModel<TextMessage> messageListModel;

    private final TextMessageCellRenderer messageCellRenderer;

    private final JButton sendButton;

    private final JTextField messageField;

    private final JTextField userField;

    private final JList<String> userList;

    private final DefaultListModel<String> userListModel;

    private final JScrollPane scroll;

    private final Container container;

    private final Network network;

    public MyWindow(){
        setTitle("Онлайн чат");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300, 400, 350, 400);
        setMinimumSize(new Dimension(350, 400));

        setVisible(true);
        setLayout(new BorderLayout());

        sendButton = new JButton("отправить");
        sendButton.setPreferredSize(new Dimension(100, 25));
        container = new Container();
        messageListModel = new DefaultListModel();
        messageList = new JList(messageListModel);
        add(messageList, BorderLayout.CENTER);
        messageCellRenderer = new TextMessageCellRenderer();
        messageList.setCellRenderer(messageCellRenderer);

        scroll = new JScrollPane(messageList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                              JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        messageField = new JTextField(14);
        userField = new JTextField(4);
        messageField.setMinimumSize(new Dimension(100, 30));
        userField.setMinimumSize(new Dimension(50, 30));
        container.setLayout(new FlowLayout());
        container.add(userField);
        container.add(messageField);
        container.add(sendButton);
        add(container, BorderLayout.SOUTH);

        userList = new JList<>();
        userListModel = new DefaultListModel();
        userList.setModel(userListModel);
        userList.setPreferredSize(new Dimension(60, 0));
        add(userList, BorderLayout.WEST);

        add(new JScrollPane(messageList), BorderLayout.CENTER);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = messageField.getText();
                String userTo = userList.getSelectedValue();
                if(text != null && !text.trim().isEmpty()){
                    TextMessage msg = new TextMessage(network.getLogin(), userTo, text);
                    messageListModel.add(messageListModel.size(), msg);
                    messageField.setText(null);
                    network.sendTextMessage(msg);
                }
            }
        });

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = messageField.getText();
                String userTo = userList.getSelectedValue();
                if(text != null && !text.trim().isEmpty()){
                    TextMessage msg = new TextMessage(network.getLogin(), userTo, text);
                    messageListModel.add(messageListModel.size(), msg);
                    messageField.setText(null);
                    network.sendTextMessage(msg);
                }
            }
        };
        messageField.addActionListener(action);

        this.network = new Network("localhost", 7777, this);

        if(network.getLogin() != null){
            setTitle("Онлайн чат. " + network.getLogin());
        }

        LoginDialog loginDialog = new LoginDialog(this, network);
        loginDialog.setVisible(true);

        if(!loginDialog.isConnected()){
            System.exit(0);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(network != null){
                    network.close();
                }
                super.windowClosing(e);
            }
        });

        setTitle("Онлайн чат. Пользователь " + network.getLogin());
    }

    @Override
    public void submitMessage(TextMessage message){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                messageListModel.add(messageListModel.size(), message);
                messageList.ensureIndexIsVisible(messageListModel.size() - 1);
            }
        });
    }

    @Override
    public void userConnected(String login) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int ix = userListModel.indexOf(login);
                if(ix == -1){
                    userListModel.add(userListModel.size(), login);
                }
            }
        });
    }

    @Override
    public void userDisconnected(String login) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int ix = userListModel.indexOf(login);
                if(ix >= 0){
                    userListModel.remove(ix);
                }
            }
        });
    }
}
