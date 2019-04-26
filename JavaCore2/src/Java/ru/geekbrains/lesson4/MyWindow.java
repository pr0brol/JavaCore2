package Java.ru.geekbrains.lesson4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyWindow extends JFrame {

    private final JButton buttonSend;

    private final Container container;

    private final DefaultListModel<TextMessage> listModel;

    private final TextMessageCellRenderer messageCellRenderer;

    private final JList<TextMessage> listText;

    private final JTextField textField;

    private final Network network;

    public MyWindow(){
        setTitle("Online Chat");
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

        textField = new JTextField(20);
        textField.setMinimumSize(new Dimension(150, 30));
        container.setLayout(new FlowLayout());
        container.add(textField);
        container.add(buttonSend);
        add(container, BorderLayout.SOUTH);

        add(new JScrollPane(listText), BorderLayout.CENTER);

        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                if(text != null && !text.trim().isEmpty()){
                    TextMessage msg = new TextMessage(network.getLogin(), TextMessage.userTo, text);
                    listModel.add(listModel.size(), msg);
                    textField.setText(null);
                    network.sendTextMessage(msg);
                }
            }
        });

        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                if(text != null && !text.trim().isEmpty()){
                    TextMessage msg = new TextMessage(network.getLogin(), "ivan", text);
                    listModel.add(listModel.size(), msg);
                    textField.setText(null);
                    network.sendTextMessage(msg);
                }
            }
        };
        textField.addActionListener(action);

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
