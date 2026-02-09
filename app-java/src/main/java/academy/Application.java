package academy;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import static java.util.Objects.nonNull;

import academy.Config.AppConfig;
import academy.Config.ConfigProvider;
import academy.Config.Providers.YamlConfigProvider;
import academy.Config.Providers.CliConfigProvider;

import academy.Dictionaries.ConfigDictionaryGame;

import academy.UI.CliDisplay;

import academy.Core.Selectors.DifficultySelector;
import academy.Core.GameSession;
import academy.Core.Formatters.Formatter;
import academy.Core.Formatters.GameStateFormatter;
import academy.Core.Impl.InteractiveGameSession;
import academy.Core.Impl.NotInteractiveGameSession;

@Command(name = "Hangman", version = "1.0", mixinStandardHelpOptions = true)
public class Application implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    private static final Predicate<String[]> IS_TESTING_MODE = words -> nonNull(words) && words.length == 2;

    @Option(names = { "-s", "--font-size" }, description = "Font size. Overrides value from config file.")
    private Integer fontSize;

    @Option(names = { "--category" }, description = "Define a category and its words directly from CLI. " +
            "Format: \"<name>:<word1>,<word2>,...\". " +
            "Can be used multiple times. Overrides all categories from config file.")
    private List<String> categoriesCli;

    @Option(names = { "-c", "--config" }, description = "Path to YAML config file.")
    private File configPath;

    @Parameters(paramLabel = "<word>", description = "Words pair for testing mode")
    private String[] words;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Application()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        AppConfig config = loadConfig();
        LOGGER.atInfo().addKeyValue("config", config).log("Config content");

        GameSession session;
        CliDisplay display = new CliDisplay();

        if (IS_TESTING_MODE.test(words)) {
            LOGGER.atInfo().log("Non-interactive testing mode enabled");

            var secretWord = words[0];
            var wordToGuessWith = words[1];

            session = new NotInteractiveGameSession(display, secretWord, wordToGuessWith);

        } else {
            LOGGER.atInfo().log("Interactive mode enabled");

            ConfigDictionaryGame dictionary = new ConfigDictionaryGame(config.dictionary());
            Formatter formatter = new GameStateFormatter();
            DifficultySelector selector = new DifficultySelector(display, config.difficulty());

            session = new InteractiveGameSession(dictionary, display, formatter, selector);
        }

        session.startNewGame();
    }

    private AppConfig loadConfig() {
        ConfigProvider provider = (configPath == null)
                ? new CliConfigProvider(fontSize, categoriesCli)
                : new YamlConfigProvider(configPath);

        return provider.get();
    }
}
