package uni.myosotis.gui;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Language {
    /**
     * The language.
     */
    private final String language;

    /**
     * The mapping from terms to the terms in the language.
     */
    private Map<String, String> map;

    /**
     * Creates a new language.
     *
     * @param language The language.
     */
    public Language(String language){
        this.language = language;
        try {
            map = reader(language);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes in a name and returns the value of the name in the language.
     *
     * @param name The name.
     * @return The value of the name in the language.
     */
    public String getName(String name){
        if(map.get(name) == null)
            return ("checkLanguage: " + name);
        else
            return map.get(name);
    }

    /**
     * Takes in a txt file which has the language in his first line
     *
     * @param language The language.
     * @return The mapping from terms to the given language.
     */
    public static Map<String, String> reader(String language) throws IOException {
        Map<String, String> map = new HashMap<>();
        File file = new File("src/main/resources/LanguagesFile.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String[] value = br.readLine().split(";");
        int index = -1;
        for (int i = 0; i < value.length; i++) { //find the index of the language
            if (value[i].equals(language)) {
                index = i;
                break;
            }
        }
        while ((line = br.readLine()) != null) { //put the values in the map
            String[] values = line.split(";");
            map.put(values[0], values[index]);
        }
        br.close();
        return map;
    }

    /**
     * Returns the possible languages from the first line of the file.
     *
     * @return The possible languages.
     */
    public Object[] getLanguages() {
        try {
            File file = new File("src/main/resources/LanguagesFile.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String[] tempLanguages = br.readLine().split(";");
            br.close();
            // Remove first Object-Line
            String[] languages = new String[tempLanguages.length - 1];
            System.arraycopy(tempLanguages, 1, languages, 0, tempLanguages.length - 1);
            return languages;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
