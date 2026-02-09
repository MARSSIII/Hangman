package academy.Dictionaries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.List;

import academy.Config.AppConfig.DictionaryConfig;

public class ConfigDictionaryGame {
    private final Map<String, List<String>> wordsByCategory;

    private final Random random;

    public ConfigDictionaryGame(DictionaryConfig config) {
        this.random = new Random();

        if (config == null || config.categories() == null) {
            this.wordsByCategory = Collections.emptyMap();
            return;
        }

        this.wordsByCategory = config.categories()
                .entrySet()
                .stream()
                .filter(this::isValidCategory)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isValidCategory(Map.Entry<String, List<String>> categoryEntry) {
        return categoryEntry != null
                && categoryEntry.getKey() != null
                && !categoryEntry.getKey().trim().isEmpty()
                && categoryEntry.getValue() != null
                && !categoryEntry.getValue().isEmpty();
    }

    public Optional<String> getRandomWord(String categoryName) {
        List<String> wordsInCategory = wordsByCategory.get(categoryName);

        if (wordsInCategory == null || wordsInCategory.isEmpty()) {
            return Optional.empty();
        }

        int randomIndex = random.nextInt(wordsInCategory.size());

        return Optional.of(wordsInCategory.get(randomIndex));
    }

    public Optional<String> getRandomCategories() {
        List<String> categories = getAvaibleCategories();

        if (categories.isEmpty()) {
            return Optional.empty();
        }

        int randomIndex = random.nextInt(categories.size());

        return Optional.of(categories.get(randomIndex));
    }

    public List<String> getAvaibleCategories() {
        return new ArrayList<>(wordsByCategory.keySet());
    }
}
