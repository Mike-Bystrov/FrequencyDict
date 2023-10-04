package new_dictionary;

public class RussianDictionary extends FrequencyDictionary {
    public RussianDictionary(String language) {
        super(language);
        regular = "[^а-яА-ЯёЁ]+";
    }
}
