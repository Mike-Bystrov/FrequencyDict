package new_GUI;

import new_database.DatabaseConnection;
import new_dictionary.EnglishDictionary;
import new_dictionary.FrenchDictionary;
import new_dictionary.RussianDictionary;
import old.windows.Language;
import old.windows.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class LanguageSelectionWindow extends JFrame {
    private static LanguageSelectionWindow menu;
    private FrequencyDictionaryWindow mainFrame;

    public LanguageSelectionWindow() {
        initWindow();
        initComponents();
    }

    public void initWindow() {
        setTitle("выбор языка");

        setSize(650, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Получаем размеры окна
        Dimension windowSize = getSize();

        // Рассчитываем координаты, чтобы окно было по центру экрана
        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;

        // Устанавливаем координаты окна
        setLocation(x, y);

    }

    public void initComponents() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Устанавливаем FlowLayout с выравниванием по центру

        JButton but1 = new JButton("английский");
        JButton but2 = new JButton("русский");
        JButton but3 = new JButton("белорусский");


        buttonPanel.add(but1);
        buttonPanel.add(but2);
        buttonPanel.add(but3);

        but1.addActionListener(e -> {
            mainFrame = new FrequencyDictionaryWindow();
            mainFrame.setDictionary(new EnglishDictionary(but1.getText().toLowerCase()));

            mainFrame.setVisible(true);
            this.setVisible(false);
            this.dispose();
        });
        but2.addActionListener(e -> {
            mainFrame = new FrequencyDictionaryWindow();
            mainFrame.setDictionary(new RussianDictionary(but2.getText().toLowerCase()));

            mainFrame.setVisible(true);
            this.setVisible(false);
            this.dispose();
        });
        but3.addActionListener(e -> {
            mainFrame = new FrequencyDictionaryWindow();
            mainFrame.setDictionary(new FrenchDictionary(but3.getText().toLowerCase()));

            mainFrame.setVisible(true);
            this.setVisible(false);
            this.dispose();
        });

        add(buttonPanel);

    }

    public static void main(String[] args) throws SQLException, IOException {
        DatabaseConnection.getInstance();
        SwingUtilities.invokeLater(() -> {
            menu = new LanguageSelectionWindow();
            menu.setVisible(true);
        });
    }
}
