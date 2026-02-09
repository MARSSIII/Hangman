package academy.Config;

import java.util.List;
import java.util.Map;

public record AppConfig(FontConfig font, DictionaryConfig dictionary, DifficultyConfig difficulty) {

    public record FontConfig(int size) {
    }

    public record DictionaryConfig(Map<String, List<String>> categories) {
    }

    public record DifficultyConfig(Map<String, LevelProperties> levels) {
    }

    public record LevelProperties(String displayName, int attempts) {
    }
}
