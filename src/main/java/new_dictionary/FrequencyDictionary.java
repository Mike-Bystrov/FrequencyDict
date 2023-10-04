package new_dictionary;
import new_database.FileRepository;
import new_database.WordRepository;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class FrequencyDictionary {
    protected String language;
    protected String regular;
    protected JTable table;

    public FrequencyDictionary(String language) {
        this.language = language;
    }

    public void addWordToDictionary(String word, String language, int fileId) throws SQLException {
        WordRepository.addWord(word, language, fileId);
    }

    public void addWordToDictionary(String word, String language) throws SQLException {
        WordRepository.addWord(word, language);
    }

    public void addWordsFromFile(File file) throws SQLException, IOException {
        FileRepository.addWordsFromFile(file, language, regular);
    }

    public void initTable() {

    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public String getLanguage() {
        return language.toLowerCase();
    }

    public int getCount() throws SQLException {
        return WordRepository.countWords(getLanguage());
    }

    public int getUnicCount() throws SQLException {
        return WordRepository.countUnicWords(getLanguage());
    }
}
