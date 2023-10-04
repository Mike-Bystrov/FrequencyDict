package new_dictionary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import new_database.WordRepository;

public class CustomContextMenu extends JPopupMenu {
    public CustomContextMenu() {
        // Создаем элементы меню
        JMenuItem menuItem1 = new JMenuItem("изменить");
        JMenuItem menuItem2 = new JMenuItem("удалить");

        menuItem1.addActionListener( e -> {
            int result = showConfirmationDialog2();

            if (result == JOptionPane.YES_OPTION) {
                // Ваш код для действия "1"
                if (CustomTable.selectedWord != null) {
                    try {
                        String newWord = JOptionPane.showInputDialog(null, "Введите новое слово:", "Изменение слова", JOptionPane.PLAIN_MESSAGE);
                        // добавь диалоговое окно для слова, на которое надо изменить
                        if(WordRepository.changeWord(CustomTable.selectedWord, newWord, CustomTable.getLanguage())) {
                            JOptionPane.showConfirmDialog(null, "слово было успешно изменено" + CustomTable.selectedWord +"?", "Подтверждение", JOptionPane.DEFAULT_OPTION);
                        } else {
                            JOptionPane.showConfirmDialog(null, "удалить слово не удалось" + CustomTable.selectedWord +"?", "Подтверждение", JOptionPane.DEFAULT_OPTION);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        });

        // Добавляем обработчики событий для элементов меню
        menuItem2.addActionListener( e -> {
            // Ваш код для действия "1"
            int result = showConfirmationDialog1();

            if (result == JOptionPane.YES_OPTION) {
                // Ваш код для действия "1"
                if (CustomTable.selectedWord != null) {
                    try {

                        if(WordRepository.deleteWord(CustomTable.selectedWord, CustomTable.getLanguage())) {
                            JOptionPane.showConfirmDialog(null, "слово было успешно удалено" + CustomTable.selectedWord +"?", "Подтверждение", JOptionPane.DEFAULT_OPTION);
                        } else {
                            JOptionPane.showConfirmDialog(null, "удалить слово не удалось" + CustomTable.selectedWord +"?", "Подтверждение", JOptionPane.DEFAULT_OPTION);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        // Добавляем элементы меню в контекстное меню
        add(menuItem1);
        add(menuItem2);
    }

    private int showConfirmationDialog1() {
        return JOptionPane.showConfirmDialog(null, "Вы уверены, что хотите удалить слово " + CustomTable.selectedWord +"?", "Подтверждение", JOptionPane.YES_NO_OPTION);
    }
    private int showConfirmationDialog2() {
        return JOptionPane.showConfirmDialog(null, "Вы уверены, что хотите изменить слово " + CustomTable.selectedWord + " во всех текстах?", "Подтверждение", JOptionPane.YES_NO_OPTION);
    }
}
