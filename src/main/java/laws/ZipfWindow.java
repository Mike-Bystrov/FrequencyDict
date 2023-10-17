package laws;
import new_dictionary.FrequencyDictionary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ZipfWindow extends JFrame{
    private static FrequencyDictionary dictionary;

    public ZipfWindow() {
        init();
    }
    public void setDictionary(FrequencyDictionary dictionary) {
        this.dictionary = dictionary;

    }

    private void init() {
        setSize(600, 400);
        JPanel panel = new JPanel();
        add(panel);

        // Кнопка "Первый закон Ципфа"
        JButton firstLawButton = new JButton("Первый закон Ципфа");
        firstLawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Здесь можно добавить код для выполнения первого закона Ципфа
            }
        });
        panel.add(firstLawButton);

        // Кнопка "Второй закон Ципфа"
        JButton secondLawButton = new JButton("Второй закон Ципфа");
        secondLawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Здесь можно добавить код для выполнения второго закона Ципфа
            }
        });
        panel.add(secondLawButton);

        // Кнопка "Третий закон Ципфа"
        JButton thirdLawButton = new JButton("Третий закон Ципфа");
        thirdLawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Здесь можно добавить код для выполнения третьего закона Ципфа
            }
        });
        panel.add(thirdLawButton);

        // Кнопка "Третий закон Ципфа"
        JButton exitButton = new JButton("exit");
        exitButton.addActionListener(e -> {
            dispose();
            setVisible(false);
            // Здесь можно добавить код для выполнения третьего закона Ципфа
        });

        panel.add(exitButton);
        add(panel);
    }

    public static void checkingFirstLaw() throws SQLException {
        int numOfAllWords = dictionary.getCount();
        
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {new ZipfWindow().setVisible(true);});
    }
}
