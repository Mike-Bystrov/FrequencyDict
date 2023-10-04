package new_dictionary;

public class EnglishDictionary extends FrequencyDictionary {


    public EnglishDictionary(String language) {
        super(language);

        regular = "[^a-zA-Z]";
    }
}
