package new_database;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRepository {
    public static int addFile(String fileName, int languageId) throws SQLException {
        String query = "INSERT INTO files (file_name, language_id) VALUES (?, ?) RETURNING file_id";
        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.setString(1, fileName);
            ps.setInt(2, languageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("file_id");
                }
            }
        }
        throw new SQLException("Ошибка при добавлении файла в базу данных");
    }

    public static int getFileId(String fileName, int languageId) throws SQLException {
        String query = "SELECT file_id FROM files WHERE file_name = ? AND language_id = ?";
        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.setString(1, fileName);
            ps.setInt(2, languageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("file_id");
                }
            }
        }
        return -1; // Файл не найден
    }

    public static void addWordsFromFile(File file, String language, String regular) throws IOException, SQLException {
        // Проверяем существование файла в базе данных по его имени
        String fileName = file.getName();
        int languageId = LanguageRepository.getIDbyName(language); // Замените "название_языка" на актуальное название языка
        int fileId = getFileId(fileName, languageId);

        // Если файл отсутствует в базе, добавляем его
        if (fileId == -1) {
            fileId = addFile(fileName, languageId);
        }

        // Читаем слова из файла


        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\\s+");

                for (String word : words) {
                    word = word.toLowerCase().replaceAll(regular, "");

                    if (!word.equals("")) {
                        WordRepository.addWord(word, language, fileId);
                    }
                }
            }
        }
    }

    public static void changeWordsInFiles(String word1, String word2) {
        try {
            // Шаг 1: Получить идентификаторы файлов, связанных с word1
            String selectFilesQuery = "SELECT f.* FROM files f " +
                    "INNER JOIN wordfiles wf ON f.file_id = wf.file_id " +
                    "INNER JOIN words w ON wf.word_id = w.word_id " +
                    "WHERE w.word = ?";

            Connection connection = DatabaseConnection.connection;
            PreparedStatement selectFilesStatement = connection.prepareStatement(selectFilesQuery);
            selectFilesStatement.setString(1, word1);

            ResultSet filesResultSet = selectFilesStatement.executeQuery();

            // Шаги 2-4: Обход и обновление файлов
            while (filesResultSet.next()) {
                int fileId = filesResultSet.getInt("file_id");
                String filePath = filesResultSet.getString("file_name");

                // Прочитать содержимое файла
                StringBuilder fileContent = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.append(line).append(System.lineSeparator());
                    }
                }

                // Заменяем word1 на word2 в содержимом файла
                String updatedContent = fileContent.toString().replaceAll("\\b" + Pattern.quote(word1) + "\\b", word2);

                // Записываем обновленное содержимое обратно в файл
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                    writer.write(updatedContent);
                }
            }

            // Закрываем ресурсы
            filesResultSet.close();
            selectFilesStatement.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
