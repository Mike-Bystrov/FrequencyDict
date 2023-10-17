package new_GUI;

import laws.ZipfWindow;
import new_database.LanguageRepository;
import new_dictionary.FrequencyDictionary;
import new_dictionary.CustomTable;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class FrequencyDictionaryWindow extends JFrame {
    private JScrollPane tableScrollPane;
    private JTextField textField1;
    private JTextField textField2;
    private JTable table;
    private JPanel mainPanel;
    private FrequencyDictionary dictionary;

    private static LanguageSelectionWindow menuFrame;
    private static FrequencyDictionaryWindow mainFrame;
    private static ZipfWindow zipfWindow;

    public FrequencyDictionaryWindow() {
        initWindow();
        initComponents();
    }

    public void initWindow() {
        setTitle("FrequencyDictionaryWindow");
        setSize(1000, 400);
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
        menuFrame = new LanguageSelectionWindow();
        mainPanel = new JPanel(new BorderLayout());
        table = new JTable();
        tableScrollPane = new JScrollPane(table);

        mainPanel.add(tableScrollPane, BorderLayout.WEST);

        // Создаем панель для ввода текста и кнопок с GridBagLayout
        JPanel inputPanel = new JPanel(new GridBagLayout());

        // Создаем кнопку 1
        JButton button1 = new JButton("слова, которые начинаются на");
        GridBagConstraints button1Constraints = new GridBagConstraints();
        button1Constraints.gridx = 1;
        button1Constraints.gridy = 0;
        button1Constraints.insets = new Insets(0, 10, 0, 0);
        inputPanel.add(button1, button1Constraints);

        button1.addActionListener(e -> {
            CustomTable update = new CustomTable();
            update.setLanguage(dictionary.getLanguage());
            update.showTable("SELECT word, frequency from " + "words" + " WHERE word LIKE '" + textField1.getText() + "%'");
            updateTable(update);
            System.out.println(textField1.getText());
        });

        // Создаем кнопку 2
        JButton button2 = new JButton("добавить слово");
        GridBagConstraints button2Constraints = new GridBagConstraints();
        button2Constraints.gridx = 1;
        button2Constraints.gridy = 1;
        button2Constraints.insets = new Insets(0, 10, 0, 0);
        inputPanel.add(button2, button2Constraints);

        button2.addActionListener( e -> {
            String word = textField2.getText();
            try {
                dictionary.addWordToDictionary(word, dictionary.getLanguage());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Создаем кнопку 3
        JButton button3 = new JButton("добавить текст");
        GridBagConstraints button3Constraints = new GridBagConstraints();
        button3Constraints.gridx = 0;
        button3Constraints.gridy = 2;
        button3Constraints.insets = new Insets(0, 10, 0, 0);
        inputPanel.add(button3, button3Constraints);

        button3.addActionListener( e -> {

            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            File selectedFile;

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                if (selectedFile.getName().endsWith(".txt")) {
                    try {
                        FileReader fileReader = new FileReader(selectedFile);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            // Ваши операции с содержимым файла (например, вывод на консоль)
                            System.out.println(line);
                        }

                        bufferedReader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(mainFrame, "Ошибка при чтении файла.");
                    }
                } else {
                    JOptionPane.showMessageDialog(menuFrame, "Выберите файл с расширением .txt");
                }
                try {
                    JOptionPane.showMessageDialog(mainFrame, "текст в обработке, пожалуйста подождите");
                    dictionary.addWordsFromFile(selectedFile);

                } catch (SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(mainFrame, "Спасибо за ожидание, слова добавлены");

            }
        });



        // Создаем поле для ввода 1
        textField1 = new JTextField();
        GridBagConstraints textField1Constraints = new GridBagConstraints();
        textField1Constraints.gridx = 0;
        textField1Constraints.gridy = 0;
        textField1Constraints.fill = GridBagConstraints.HORIZONTAL;
        textField1Constraints.weightx = 1.0;
        inputPanel.add(textField1, textField1Constraints);

        // Создаем поле для ввода 2
        textField2 = new JTextField();
        GridBagConstraints textField2Constraints = new GridBagConstraints();
        textField2Constraints.gridx = 0;
        textField2Constraints.gridy = 1;
        textField2Constraints.fill = GridBagConstraints.HORIZONTAL;
        textField2Constraints.weightx = 1.0;
        inputPanel.add(textField2, textField2Constraints);

        // Добавляем панель для ввода текста и кнопок в центральную часть окна
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Создаем панель для кнопок снизу
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Добавляем кнопки внизу окна
        JButton bottomButton1 = new JButton("показать все слова");
        JButton bottomButton2 = new JButton("в меню");
        JButton bottomButton3 = new JButton("всего слов ");
        JButton bottomButton4 = new JButton("всего уникальных слов ");
        JButton bottomButton5 = new JButton("проверка законов ципфа ");

        buttonPanel.add(bottomButton1);
        buttonPanel.add(bottomButton2);
        buttonPanel.add(bottomButton3);
        buttonPanel.add(bottomButton4);
        buttonPanel.add(bottomButton5);

        bottomButton1.addActionListener(e -> {
            CustomTable table1 = new CustomTable();
            table1.setLanguage(dictionary.getLanguage());
            dictionary.setTable(table1);
            try {
                table1.showTable("SELECT word, frequency FROM words WHERE language_id = " + LanguageRepository.getIDbyName(dictionary.getLanguage()));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            updateTable(table1);
        });
        bottomButton2.addActionListener(e -> {
            this.dispose();
            this.setVisible(false);

            menuFrame.setVisible(true);
            revalidate();
        });
        bottomButton3.addActionListener(e -> {
            try {
                JOptionPane.showMessageDialog(this, "всего слов -- " + dictionary.getCount());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        bottomButton4.addActionListener(e -> {
            try {
                JOptionPane.showMessageDialog(this, "всего уникальных слов -- " + dictionary.getUnicCount());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        bottomButton5.addActionListener(e -> {
            zipfWindow = new ZipfWindow();
            zipfWindow.setDictionary(dictionary);
            zipfWindow.setVisible(true);
        });

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Добавляем основную панель в окно
        add(mainPanel);
    }

    public void setDictionary(FrequencyDictionary dictionary) {
        this.dictionary = dictionary;
        setTitle(dictionary.getLanguage() + "_dictionary");
        revalidate();
    }

    public void updateTable(CustomTable update) {
        mainPanel.remove(tableScrollPane);
        table = update;
        tableScrollPane = new JScrollPane(table);
        mainPanel.add(tableScrollPane, BorderLayout.WEST);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        new FrequencyDictionaryWindow().setVisible(true);
    }
}


