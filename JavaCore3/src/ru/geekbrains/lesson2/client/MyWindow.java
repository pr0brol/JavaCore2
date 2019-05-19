package ru.geekbrains.lesson2.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.List;

public class MyWindow extends JFrame implements MessageReciever{

    private final JList<TextMessage> messageList;

    private final DefaultListModel<TextMessage> messageListModel;

    private final TextMessageCellRenderer messageCellRenderer;

    private final JScrollPane scroll;

    private final JPanel sendMessagePanel;

    private final JButton sendButton;

    private final JTextField messageField;

    private final JTextField userField;

    private final JList<String> userList;

    private final DefaultListModel<String> userListModel;

    private final Network network;

    private DocFile docFile;

    public MyWindow(){
        setTitle("Онлайн чат");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300, 400, 350, 400);
        setMinimumSize(new Dimension(350, 400));

        setVisible(true);
        setLayout(new BorderLayout());

        messageList = new JList<>();
        messageListModel = new DefaultListModel<>();
        messageCellRenderer = new TextMessageCellRenderer();
        messageList.setModel(messageListModel);
        messageList.setCellRenderer(messageCellRenderer);

        scroll = new JScrollPane(messageList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);

        sendMessagePanel = new JPanel();
        sendMessagePanel.setLayout(new BorderLayout());

        sendButton = new JButton("отправить");
        sendButton.setPreferredSize(new Dimension(100, 25));


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
                    docFile.writeText(msg);
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
                    for(int i=0; i<messageListModel.size(); i++){
                        System.out.println(messageListModel.get(i).getText());
                    }
                    messageField.setText(null);
                    network.sendTextMessage(msg);
                    docFile.writeText(msg);
                }
            }
        };

        sendMessagePanel.add(sendButton, BorderLayout.EAST);
        messageField = new JTextField();
        messageField.addActionListener(action);
        sendMessagePanel.add(messageField, BorderLayout.CENTER);
        userField = new JTextField("", 5);
        sendMessagePanel.add(userField, BorderLayout.WEST);

        add(sendMessagePanel, BorderLayout.SOUTH);

        userList = new JList<>();
        userListModel = new DefaultListModel();
        userList.setModel(userListModel);

        userList.setPreferredSize(new Dimension(60, 0));
        add(userList, BorderLayout.WEST);

        this.network = new Network("localhost", 7777, this);

        if(network.getLogin() != null){
            setTitle("Онлайн чат. " + network.getLogin());
        }

        LoginDialog loginDialog = new LoginDialog(this, network);
        loginDialog.setVisible(true);

        if(!loginDialog.isConnected()){
            System.exit(0);
        }

        this.network.requestConnectedUserList();

        try {
            this.docFile = new DocFile(network.getLogin());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MyWindow.this,
                    "Ошибка",
                    "Не запускается сервис истории сообщений",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        List<TextMessage> last = this.docFile.readText(5);
        for (TextMessage msg: last){
            messageListModel.add(messageListModel.size(), msg);
        }


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(network != null){
                    network.close();
                }
                if(docFile != null){
                    docFile.flush();
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
                messageList.ensureIndexIsVisible(messageListModel.size());
                docFile.writeText(message);
                docFile.flush();
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

    @Override
    public void updateUserList(Set<String> users) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                userListModel.clear();
                for(String usr: users){
                    userListModel.addElement(usr);
                }
            }
        });
    }

}

