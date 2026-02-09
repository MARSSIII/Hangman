package academy.Core.Impl;

import java.util.stream.Collectors;

import academy.Core.GameSession;
import academy.Core.model.GameState;
import academy.Core.model.Enums.GameStatus;
import academy.UI.CliDisplay;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotInteractiveGameSession implements GameSession {
    private final CliDisplay ui;
    private final String secretWord;
    private final String wordToGuessWith;

    @Override
    public void startNewGame() {
        final int maxAttempts = wordToGuessWith.length();
        GameState state = GameState.initialState(secretWord, maxAttempts);

        for (char letter : wordToGuessWith.toCharArray()) {
            state = state.withGuess(letter);

            if (state.status() != GameStatus.IN_PROGRESS) {
                break;
            }
        }

        GameStatus finalStatus = state.status() == GameStatus.IN_PROGRESS ? GameStatus.NEG : GameStatus.POS;

        String output = formatFinalOutput(state, finalStatus);
        ui.display(output);
    }

    private String formatFinalOutput(GameState finalState, GameStatus status) {
        String finalWordState = finalState.secretWord().chars()
                .mapToObj(c -> (char) c)
                .map(c -> finalState.correctLetters().contains(c) ? String.valueOf(c) : "*")
                .collect(Collectors.joining());

        String result = status.toString().toUpperCase();

        return String.format("%s;%s", finalWordState, result);
    }
}
