package academy.Core.Formatters;

import java.util.stream.Collectors;

import academy.Core.model.GameState;

public class GameStateFormatter implements Formatter {
    private static final String[] HANGMAN_STAGES = {
            "  +---+\n  |   |\n      |\n      |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n      |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n  |   |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|   |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n      |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n /    |\n      |\n=========",
            "  +---+\n  |   |\n  O   |\n /|\\  |\n / \\  |\n      |\n========="
    };

    public String format(GameState state) {
        StringBuilder sb = new StringBuilder();
        int incorrectAttempts = state.maxAttempts() - state.attemptsLeft();

        sb.append(getHangmanArt(incorrectAttempts, state.maxAttempts()));

        sb.append("\n\nСлово: ").append(state.currentWordState());
        sb.append("\nОсталось попыток: ").append(state.attemptsLeft());

        if (!state.incorrectLetters().isEmpty()) {
            String incorrectLettersString = state.incorrectLetters()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            sb.append("\nНеверные буквы: ").append(incorrectLettersString);
        }

        return sb.toString();
    }

    private String getHangmanArt(int incorrectAttempts, int maxAttempts) {
        if (maxAttempts <= 0) {
            return HANGMAN_STAGES[0];
        }

        int stageIndex = (incorrectAttempts * (HANGMAN_STAGES.length - 1)) / maxAttempts;

        stageIndex = Math.min(stageIndex, HANGMAN_STAGES.length - 1);
        stageIndex = Math.max(stageIndex, 0);

        return HANGMAN_STAGES[stageIndex];
    }
}
