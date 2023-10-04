package new_database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WordRepository {

    public static void addWord(String word, String language, int fileId) throws SQLException {
        int language_id = LanguageRepository.getIDbyName(language);

        String query = "INSERT INTO words (word, frequency, language_id) "
                + "VALUES (?, 1, ?) "
                + "ON CONFLICT (word, language_id) DO UPDATE "
                + "SET frequency = words.frequency + 1 "
                + "RETURNING word_id";

        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.setString(1, word);
            ps.setInt(2, language_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int wordId = rs.getInt("word_id");
                    WordFileRepository.addWordFileRelation(wordId, fileId);
                }
            }
        }
    }

    public static boolean addWord(String word, String language) throws SQLException {
        int language_id = LanguageRepository.getIDbyName(language);

        String insertQuery = "INSERT INTO words (word, frequency, language_id) "
                + "VALUES (?, 0, ?)";

        if (getWordId(word, language) == -1) {
            PreparedStatement ps = DatabaseConnection.connection.prepareStatement(insertQuery);
            ps.setString(1, word);
            ps.setInt(2, language_id);
            ps.execute();
            return true;
        }
        return false;
    }

    public static boolean deleteWord(String word, String language) throws SQLException{
        WordFileRepository.removeWordFileRelations(word);

        String query = "DELETE FROM words WHERE word = '" + word + "' and language_id = " + LanguageRepository.getIDbyName(language);

        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)){
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean changeWord(String word1, String word2, String language) throws SQLException, IOException {
        //TODO добавить запрос
        FileRepository.changeWordsInFiles(word1, word2);
        int word2_id = getWordId(word2, language);
        DatabaseConnection.getInstance();
        try {
            if (word2_id == -1) {
                // нет такого слова
                String updateQuery = "UPDATE words SET word = ? WHERE word_id = ?";
                PreparedStatement ps = DatabaseConnection.connection.prepareStatement(updateQuery);
                ps.setString(1, word2); // Устанавливаем значение word2 как параметр
                ps.setInt(2, getWordId(word1, language)); // Устанавливаем значение word_id как параметр
                ps.executeUpdate();
            } else {
                // есть слово
                String updateQuery = "UPDATE words SET frequency = frequency + (SELECT frequency FROM words WHERE word_id = ?) WHERE word_id = ?";
                PreparedStatement ps = DatabaseConnection.connection.prepareStatement(updateQuery);
                ps.setInt(1, word2_id); // Устанавливаем значение word2_id как параметр
                ps.setInt(2, getWordId(word1, language)); // Устанавливаем значение word_id как параметр
                ps.executeUpdate();

                String deleteQuery1 = "DELETE FROM wordfiles WHERE word_id = ?";
                ps = DatabaseConnection.connection.prepareStatement(deleteQuery1);
                ps.setInt(1, getWordId(word1, language)); // Устанавливаем значение word_id как параметр
                ps.executeUpdate();

                String deleteQuery2 = "DELETE FROM words WHERE word_id = ?";
                ps = DatabaseConnection.connection.prepareStatement(deleteQuery2);
                ps.setInt(1, getWordId(word1, language)); // Устанавливаем значение word_id как параметр
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static int getWordId(String word, String language) throws SQLException {
        int languageId = LanguageRepository.getIDbyName(language);

        String query = "SELECT word_id FROM words WHERE word = ? AND language_id = ?";

        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.setString(1, word);
            ps.setInt(2, languageId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("word_id");
                }
            }
        }

        // Если слово не найдено, можно вернуть какое-то значение по умолчанию, например -1 или 0.
        // В зависимости от вашей логики.
        return -1; // или 0, в зависимости от вашей логики
    }

    public static int countWords(String language) throws SQLException {
        String query = "SELECT SUM(frequency) FROM words WHERE language_id = ?";

        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.setInt(1, LanguageRepository.getIDbyName(language));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Получить значение счетчика (первую колонку в результате)
                } else {
                    return 0; // Если нет результатов, вернуть 0
                }
            }
        }
    }

    public static int countUnicWords(String language) throws SQLException {
        String query = "SELECT COUNT(*) FROM words WHERE language_id = ?";

        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.setInt(1, LanguageRepository.getIDbyName(language));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Получить значение счетчика (первую колонку в результате)
                } else {
                    return 0; // Если нет результатов, вернуть 0
                }
            }
        }

    }

    public static int countNumOfWordsWithFrequency(int frequency) throws SQLException, IOException {
        String query = "SELECT COUNT(*) FROM words WHERE frequency = ?";

        DatabaseConnection.getInstance();
        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.setInt(1, frequency);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                else return 0;
            }
        }
    }

}






