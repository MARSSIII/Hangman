package academy.ProvidersTests;

import academy.Config.AppConfig;
import academy.Config.Providers.CliConfigProvider;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CliConfigProviderTest {

    @Test
    void Success_Receive_Default_Config() {
        // Given
        CliConfigProvider provider = new CliConfigProvider(null, null);

        // When
        AppConfig config = provider.get();

        // Then
        assertNotNull(config);
        assertEquals(24, config.font().size());
        assertTrue(config.dictionary().categories().containsKey("Слова по умолчанию"));
        assertEquals(3, config.difficulty().levels().size());
        assertEquals("Легкий", config.difficulty().levels().get("easy").displayName());
    }

    @Test
    void Success_Receive_Config_With_FontSize() {
        // Given
        CliConfigProvider provider = new CliConfigProvider(18, null);

        // When
        AppConfig config = provider.get();

        // Then
        assertEquals(18, config.font().size());
    }

    @Test
    void Success_Receive_Config_With_Categories() {
        // Given
        List<String> categories = List.of(
                "Животные:собака,кошка,слон",
                "Фрукты:яблоко,банан,апельсин");
        CliConfigProvider provider = new CliConfigProvider(null, categories);

        // When
        AppConfig config = provider.get();

        // Then
        Map<String, List<String>> configCategories = config.dictionary().categories();
        assertEquals(2, configCategories.size());
        assertEquals(List.of("собака", "кошка", "слон"), configCategories.get("Животные"));
        assertEquals(List.of("яблоко", "банан", "апельсин"), configCategories.get("Фрукты"));
    }

    @Test
    void Fail_Receive_Config_When_Invalid_Category_Format() {
        // Given
        List<String> invalidCategories = List.of(
                "valid:cat1,cat2",
                "invalid-category-format", // нет разделителя :
                ":words", // пустое название
                "name:" // пустые слова
        );
        CliConfigProvider provider = new CliConfigProvider(null, invalidCategories);

        // When & Then - ожидаем исключение при попытке получить конфиг
        assertThrows(IllegalArgumentException.class, provider::get);
    }

    @Test
    void Success_Receive_Config_With_Empty_Or_Blank_Words() {
        // Given
        List<String> categoriesWithBlanks = List.of(
                "Животные:собака,,кошка, ,", // пустые и пробельные слова
                "Фрукты: , ," // только пробельные слова
        );
        CliConfigProvider provider = new CliConfigProvider(null, categoriesWithBlanks);

        // When
        AppConfig config = provider.get();

        // Then
        Map<String, List<String>> configCategories = config.dictionary().categories();
        assertEquals(List.of("собака", "кошка"), configCategories.get("Животные"));
        assertEquals(List.of(), configCategories.get("Фрукты"));
    }

}
