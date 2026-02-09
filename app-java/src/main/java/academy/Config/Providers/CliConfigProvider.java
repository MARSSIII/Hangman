package academy.Config.Providers;

import academy.Config.AppConfig;
import academy.Config.AppConfig.DifficultyConfig;
import academy.Config.AppConfig.LevelProperties;
import academy.Config.Builders.AppConfigBuilder;
import academy.Config.ConfigProvider;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CliConfigProvider implements ConfigProvider {
    private final Integer fontSize;
    private final List<String> categories;

    @Override
    public AppConfig get() {
        AppConfigBuilder configBuilder = new AppConfigBuilder()
                .withFontSize(24)
                .addCategory("Слова по умолчанию", "компьютер", "программист");

        if (fontSize != null) {
            configBuilder.withFontSize(fontSize);
        }

        if (categories != null && !categories.isEmpty()) {
            Map<String, List<String>> parsedCategories = parseCliCategories(categories);

            configBuilder.withCategories(parsedCategories);
        }

        DifficultyConfig difficultyConfig = createDefaultDifficultyConfig();
        configBuilder.withDiffucilties(difficultyConfig.levels());

        return configBuilder.build();
    }

    private DifficultyConfig createDefaultDifficultyConfig() {
        Map<String, LevelProperties> levels = new LinkedHashMap<>();

        levels.put("easy", new LevelProperties("Легкий", 10));
        levels.put("medium", new LevelProperties("Средний", 7));
        levels.put("hard", new LevelProperties("Сложный", 4));

        return new DifficultyConfig(levels);
    }

    private Map<String, List<String>> parseCliCategories(List<String> cliCategories) {
        return cliCategories.stream()
                .map(this::parseCategoryEntry)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new));
    }

    private Map.Entry<String, List<String>> parseCategoryEntry(String catString) {
        String[] parts = catString.split(":", 2);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException("Неверный формат категории: " + catString);
        }

        String name = parts[0].trim();
        List<String> words = Arrays.stream(parts[1].split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        return Map.entry(name, words);
    }
}
