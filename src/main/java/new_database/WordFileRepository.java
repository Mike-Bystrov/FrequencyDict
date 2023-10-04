package new_database;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WordFileRepository {
    public static void addWordFileRelation(int wordId, int fileId) throws SQLException {
        if (!wordFileRelationExists(wordId, fileId)){
            String query = "INSERT INTO wordfiles (word_id, file_id) VALUES (?, ?)";
            try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
                ps.setInt(1, wordId);
                ps.setInt(2, fileId);
                ps.executeUpdate();
            }
        }
    }

    public static boolean wordFileRelationExists(int wordId, int fileId) throws SQLException {
        String query = "SELECT COUNT(*) FROM wordfiles WHERE word_id = ? AND file_id = ?";
        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.setInt(1, wordId);
            ps.setInt(2, fileId);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public static void removeWordFileRelations(String word) throws SQLException{
        String query = "DELETE FROM wordfiles WHERE word_id = (SELECT word_id FROM words WHERE word = '"+word+"');";
        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.execute();
        }
    }
}
