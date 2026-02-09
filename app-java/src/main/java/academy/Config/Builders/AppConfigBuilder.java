package academy.Config.Builders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import academy.Config.AppConfig;
import academy.Config.AppConfig.LevelProperties;
import academy.Config.AppConfig.DictionaryConfig;
import academy.Config.AppConfig.DifficultyConfig;
import academy.Config.AppConfig.FontConfig;

public class AppConfigBuilder {
    private Integer fontSize;

    private final Map<String, List<String>> categories = new LinkedHashMap<>();

    private final Map<String, LevelProperties> difficulties = new LinkedHashMap<>();

    public AppConfigBuilder withFontSize(int size) {
        this.fontSize = size;

        return this;
    }

    public AppConfigBuilder addCategory(String name, List<String> words) {
        this.categories.put(name, words);

        return this;
    }

    public AppConfigBuilder addCategory(String name, String... words) {
        return addCategory(name, Arrays.asList(words));
    }

    public AppConfigBuilder withCategories(Map<String, List<String>> categories) {
        this.categories.clear();
        this.categories.putAll(categories);

        return this;
    }

    public AppConfigBuilder withDiffucilties(Map<String, LevelProperties> difficulties) {
        this.difficulties.clear();
        this.difficulties.putAll(difficulties);

        return this;
    }

    public AppConfig build() {
        FontConfig fontConfig = new FontConfig(fontSize);
        DictionaryConfig dictionaryConfig = new DictionaryConfig(Map.copyOf(categories));
        DifficultyConfig difficultyConfig = new DifficultyConfig(Map.copyOf(difficulties));

        return new AppConfig(fontConfig, dictionaryConfig, difficultyConfig);
    }
}
