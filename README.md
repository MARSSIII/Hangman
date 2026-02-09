# Hangman

A console word-guessing game where the player tries to guess a hidden word one letter at a time before the hangman is fully drawn.

## Features

- Multiple word categories and difficulty levels
- ASCII visualization of the gallows that builds up with each wrong guess
- Case-insensitive input
- Interactive mode (play in console) and non-interactive mode (for automated testing)
- Hint system for each word

## Quick Start

```bash
# Build
mvn clean package

# Run in interactive mode
java -jar target/hangman.jar

# Run in non-interactive (test) mode
java -jar target/hangman.jar "hidden_word" "guessed_letters"
```

## How to Play

1. The game selects a random word from a chosen category and difficulty
2. You see the word as a series of `*` symbols
3. Enter one letter per turn
4. Correct guesses reveal the letter in the word
5. Wrong guesses add a part to the hangman drawing
6. Win by guessing the word before running out of attempts

## Example

```text
  -----
  |   |
  O   |
 /|\  |
 / \  |
      |
=========

Word:     h * l l o
Attempts: 2 remaining
```

## Modes

| Mode | Activation | Description |
|------|-----------|-------------|
| Interactive | No arguments | Play the game in the console |
| Non-interactive | Two arguments | Automated testing mode, outputs `guessed_word;result` |

## Testing

```bash
mvn test
```
