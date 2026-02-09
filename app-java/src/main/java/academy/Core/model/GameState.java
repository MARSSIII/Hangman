package academy.Core.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import academy.Core.model.Enums.GameStatus;

public record GameState(
        String secretWord,
        String currentWordState,
        Set<Character> correctLetters,
        Set<Character> incorrectLetters,
        int attemptsLeft,
        int maxAttempts,
        GameStatus status) {

    public static GameState initialState(String secretWord, int maxAttempts) {
        String initialWordState = "_".repeat(secretWord.length());

        return new GameState(
                secretWord.toLowerCase(),
                initialWordState,
                Set.of(),
                Set.of(),
                maxAttempts,
                maxAttempts,
                GameStatus.IN_PROGRESS);
    }

    public GameState withGuess(char letter) {
        char lowerCaseLetter = Character.toLowerCase(letter);

        if (status != GameStatus.IN_PROGRESS) {
            return this;
        }

        boolean isAlreadyGuessed = correctLetters.contains(lowerCaseLetter)
                || incorrectLetters.contains(lowerCaseLetter);
        boolean isCorrectLetter = secretWord.indexOf(lowerCaseLetter) >= 0;

        int newAttemptsLeft = isCorrectLetter ? attemptsLeft : attemptsLeft - 1;

        if (isCorrectLetter && !isAlreadyGuessed) {
            Set<Character> newCorrectLetters = updateSet(correctLetters, lowerCaseLetter);
            String newWordState = buildCurrentWordState(secretWord, newCorrectLetters);

            boolean hasWon = newWordState.replace(" ", "").equals(secretWord);
            GameStatus newStatus = hasWon ? GameStatus.POS : GameStatus.IN_PROGRESS;

            return new GameState(secretWord, newWordState, newCorrectLetters, incorrectLetters, newAttemptsLeft,
                    maxAttempts, newStatus);

        } else {
            Set<Character> newIncorrectLetters = (isAlreadyGuessed)
                    ? incorrectLetters
                    : updateSet(incorrectLetters, lowerCaseLetter);

            GameStatus newStatus = (newAttemptsLeft <= 0) ? GameStatus.NEG : GameStatus.IN_PROGRESS;

            return new GameState(secretWord, currentWordState, correctLetters, newIncorrectLetters, newAttemptsLeft,
                    maxAttempts, newStatus);
        }
    }

    private static String buildCurrentWordState(String secretWord, Set<Character> correctLetters) {
        return secretWord.chars()
                .mapToObj(c -> (char) c)
                .map(c -> correctLetters.contains(c) ? String.valueOf(c) : "_")
                .collect(Collectors.joining());
    }

    private static Set<Character> updateSet(Set<Character> original, char element) {
        Set<Character> updated = new HashSet<>(original);
        updated.add(element);

        return updated;
    }
}
