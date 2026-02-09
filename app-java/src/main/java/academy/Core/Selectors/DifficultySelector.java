package academy.Core.Selectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import academy.Config.AppConfig.DifficultyConfig;
import academy.Core.model.DifficultyLevel;
import academy.UI.CliDisplay;

public class DifficultySelector implements Selector {

    private final CliDisplay ui;
    private final List<DifficultyLevel> availableLevels;
    private final Random random = new Random();

    public DifficultySelector(CliDisplay ui, DifficultyConfig difficultyConfig) {
        this.ui = ui;
        this.availableLevels = new ArrayList<>();

        if (difficultyConfig != null && difficultyConfig.levels() != null) {
            difficultyConfig.levels().forEach((id, properties) -> availableLevels
                    .add(new DifficultyLevel(id, properties.displayName(), properties.attempts())));
        }
    }

    @Override
    public DifficultyLevel select() {
        if (availableLevels.isEmpty()) {
            throw new IllegalStateException("В конфигурации не определено ни одного уровня сложности.");
        }

        List<String> displayNames = availableLevels.stream()
                .map(DifficultyLevel::displayName)
                .toList();

        Optional<String> choiceOpt = ui.promptForChoice(displayNames, "Пожалуйста, выберите уровень сложности:");

        if (choiceOpt.isEmpty()) {
            ui.display("Выбран случайный уровень сложности.");
            return selectRandom();
        }

        String chosenDisplayName = choiceOpt.get();

        return availableLevels.stream()
                .filter(level -> level.displayName().equals(chosenDisplayName))
                .findFirst()
                .orElseGet(() -> {
                    ui.display("Произошла ошибка выбора, выбран случайный уровень.");
                    return selectRandom();
                });
    }

    @Override
    public List<DifficultyLevel> getAvailableLevels() {
        return new ArrayList<>(availableLevels);
    }

    @Override
    public boolean hasAvailableLevels() {
        return !availableLevels.isEmpty();
    }

    @Override
    public DifficultyLevel selectRandom() {
        if (availableLevels.isEmpty()) {
            throw new IllegalStateException("Нет доступных уровней сложности для случайного выбора.");
        }
        return availableLevels.get(random.nextInt(availableLevels.size()));
    }
}
