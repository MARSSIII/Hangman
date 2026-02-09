package academy.UI;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CliDisplay {
    private final Scanner scanner;

    public CliDisplay() {
        this.scanner = new Scanner(System.in);
    }

    public void display(String message) {
        clearScreen();

        System.out.println(message);
    }

    public char promptForGuess() {
        while (true) {
            System.out.print("\nВведите вашу букву: ");
            String line = scanner.nextLine();

            if (line != null && line.length() == 1 && Character.isLetter(line.charAt(0))) {
                return line.charAt(0);
            } else {
                System.out.println("Неверный ввод. Пожалуйста, введите одну букву.");
            }
        }
    }

    public Optional<String> promptForChoice(List<String> options, String promt) {
        StringBuilder menu = new StringBuilder(promt);
        menu.append("\n");

        for (int i = 0; i < options.size(); i++) {
            menu.append(String.format("%d. %s\n", i + 1, options.get(i)));
        }

        display(menu.toString());

        while (true) {
            System.out.print("Введите номер (или нажмите Enter для случайного выбора): ");
            String line = scanner.nextLine();

            if (line.isEmpty()) {
                return Optional.empty();
            }

            try {
                int choice = Integer.parseInt(line);

                if (choice >= 1 && choice <= options.size()) {
                    return Optional.of(options.get(choice - 1));
                } else {
                    System.out.println("Ошибка: номер вне диапазона. Попробуйте еще раз.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число. Попробуйте еще раз.");
            }
        }
    }

    private void clearScreen() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (final IOException | InterruptedException e) {
            for (int i = 0; i < 50; ++i)
                System.out.println();
        }
    }
}
