package Java.ru.geekbrains.lesson4;

import javax.swing.*;
import java.awt.*;

public class TextMessageCellRenderer extends JPanel implements ListCellRenderer<TextMessage>{

    private final JLabel created;

    private final JLabel userName;

    private final JTextArea messageText;

    private final JPanel panel;

    public TextMessageCellRenderer(){
        super();
        setLayout(new BorderLayout());

        created = new JLabel();
        userName = new JLabel();
        messageText = new JTextArea();
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(userName);
        panel.add(created);

        Font un = userName.getFont();
        userName.setFont(un.deriveFont(un.getStyle() | Font.BOLD | Font.ITALIC));

        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(messageText, BorderLayout.SOUTH);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends TextMessage> list, TextMessage value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        created.setText(value.getCreated());
        setBackground(list.getBackground());
        userName.setText(value.getUserFrom());
        messageText.setText(value.getText());
        return this;
    }
}
