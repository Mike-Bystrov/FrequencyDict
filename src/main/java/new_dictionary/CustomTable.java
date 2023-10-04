package new_dictionary;

import new_GUI.FrequencyDictionaryWindow;
import new_database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomTable extends JTable {
    private static DefaultTableModel tableModel;
    private static Connection connection;
    static String selectedWord;
    private static String language;
    private static FrequencyDictionaryWindow window;

    public void setLanguage(String language) {
        this.language = language;
    }

    public static String getLanguage() {
        return language;
    }

    public CustomTable() {

        connection = DatabaseConnection.connection;
        // Устанавливаем модель данных для таблицы
        tableModel = new DefaultTableModel();

        // Добавляем колонки в модель
        tableModel.addColumn("Слово");
        tableModel.addColumn("Частота");

        // Устанавливаем модель данных для таблицы
        setModel(tableModel);

        // Создаем TableRowSorter для сортировки таблицы
        addSorter();

        getSelectionModel().addListSelectionListener( e -> {
            int selectedRow = getSelectedRow();
            if (selectedRow != -1) {
                selectedWord = getValueAt(selectedRow, 0).toString(); // Получаем значение слова из первой колонки
            }
        });

        CustomContextMenu contextMenu = new CustomContextMenu();
        setComponentPopupMenu(contextMenu);
    }

    public void showTable(String query) {
        if (connection != null) {
            try {
                // Создаем запрос к базе данных
                Statement statement = connection.createStatement();

                // Выполняем SQL-запрос для извлечения данных
                ResultSet resultSet = statement.executeQuery(query);

                // Заполняем таблицу данными из результата запроса
                while (resultSet.next()) {
                    String word = resultSet.getString("word");
                    int frequency = resultSet.getInt("frequency");

                    // Добавляем строку в модель таблицы
                    tableModel.addRow(new Object[]{word, frequency});
                }

                // Закрываем ресурсы
                resultSet.close();
                statement.close();
                //connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Обработайте ошибку выполнения запроса
            }
        }

    }

    public void deleteWord() {

    }

    public void addSorter() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        setRowSorter(sorter);

        // Добавляем слушателя событий на заголовок таблицы
        getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = getTableHeader().columnAtPoint(evt.getPoint());
                // По количеству слов (второй столбец)
                if (column == 1) {
                    sorter.setComparator(column, (o1, o2) -> {
                        Integer int1 = Integer.parseInt(o1.toString());
                        Integer int2 = Integer.parseInt(o2.toString());
                        return int1.compareTo(int2);
                    });
                } else {
                    // По словам (первый столбец)
                    sorter.setComparator(column, (o1, o2) -> {
                        String str1 = o1.toString();
                        String str2 = o2.toString();
                        return str1.compareTo(str2);
                    });
                }
            }
        });

    }

}
