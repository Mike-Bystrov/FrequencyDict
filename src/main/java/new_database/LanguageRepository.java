package new_database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageRepository {
    public static int getIDbyName(String language) throws SQLException {
        String query = "SELECT language_id FROM languages WHERE language_name = ?";
        try (PreparedStatement ps = DatabaseConnection.connection.prepareStatement(query)) {
            ps.setString(1, language);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("language_id");
                }
            }
        }
        throw new SQLException("Язык не найден в базе данных");
    }

    public static void addLanguage(){}


}
