package academy.Core.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import academy.Dictionaries.ConfigDictionaryGame;
import academy.UI.CliDisplay;
import academy.Core.model.DifficultyLevel;
import academy.Core.model.Enums.GameStatus;
import academy.Core.GameSession;
import academy.Core.Formatters.Formatter;
import academy.Core.Selectors.DifficultySelector;
import academy.Core.model.GameState;

@AllArgsConstructor
public class InteractiveGameSession implements GameSession {

    private final ConfigDictionaryGame dictionaryGame;
    private final CliDisplay ui;
    private final Formatter formatter;
    private final DifficultySelector selector;

    public void startNewGame() {
        ui.display("Добро пожаловать в игру 'Виселица'!");

        Optional<String> secretWordOpt = prepareSecretWord();
        if (secretWordOpt.isEmpty()) {
            return;
        }

        String secretWord = secretWordOpt.get();

        DifficultyLevel difficultyLevel = selector.select();

        final int maxAttempts = difficultyLevel.attempts();
        GameState state = GameState.initialState(secretWord, maxAttempts);

        while (state.status() == GameStatus.IN_PROGRESS) {
            String gameScreen = buildGameScreen(state);

            ui.display(gameScreen);
            char guess = ui.promptForGuess();

            state = state.withGuess(guess);
        }

        String finalScreen = buildFinalScreen(state);
        ui.display(finalScreen);
    }

    private Optional<String> prepareSecretWord() {
        Optional<String> chosenCategory = chooseCategory();
        if (chosenCategory.isEmpty()) {
            return Optional.empty();
        }

        Optional<String> secretWordOpt = dictionaryGame.getRandomWord(chosenCategory.get());
        if (secretWordOpt.isEmpty()) {
            ui.display("Ошибка: не удалось получить слово из категории '" + chosenCategory + "'");

            return Optional.empty();
        }

        return secretWordOpt;
    }

    private Optional<String> chooseCategory() {
        List<String> categories = dictionaryGame.getAvaibleCategories();
        if (categories.isEmpty()) {
            ui.display("Ошибка: в словаре нет слов для игры.");

            return Optional.empty();
        }

        if (categories.size() == 1) {
            return Optional.of(categories.get(0));
        }

        List<String> categoryOptions = new ArrayList<>();
        categoryOptions.add("Случайная категория");
        categoryOptions.addAll(categories);

        Optional<String> choiceOpt = ui.promptForChoice(categoryOptions, "Пожалуйста, выберите категорию:");

        String chosenCategory = choiceOpt.orElse("Случайная категория");

        if (chosenCategory.equals("Случайная категория")) {
            return dictionaryGame.getRandomCategories();
        } else {
            return Optional.of(chosenCategory);
        }
    }

    private String buildGameScreen(GameState state) {
        return formatter.format(state) + "\n------------------------------------";
    }

    private String buildFinalScreen(GameState state) {
        StringBuilder sb = new StringBuilder();

        sb.append("Игра окончена!\n");
        sb.append("====================================\n");
        sb.append(formatter.format(state)).append("\n\n");

        if (state.status() == GameStatus.POS) {
            sb.append("Поздравляем! Вы победили!");
        } else {
            sb.append("Вы проиграли. Загаданное слово было: ").append(state.secretWord());
        }
        return sb.toString();
    }
}
