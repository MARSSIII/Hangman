package academy.ProvidersTests;

import academy.Config.AppConfig;
import academy.Config.Providers.YamlConfigProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Test;

class YamlConfigProviderTest {

    @TempDir
    Path tempDir;

    @Test
    void Success_Read_From_Yaml_file() throws IOException {
        // Given
        String yamlContent = """
                font:
                  size: 16
                dictionary:
                  categories:
                    Техника:
                      - компьютер
                      - мышь
                    Еда:
                      - яблоко
                      - банан
                difficulty:
                  levels:
                    easy:
                      displayName: "Легкий"
                      attempts: 10
                    medium:
                      displayName: "Средний"
                      attempts: 7
                """;

        File yamlFile = tempDir.resolve("config.yaml").toFile();
        Files.writeString(yamlFile.toPath(), yamlContent);

        YamlConfigProvider provider = new YamlConfigProvider(yamlFile);

        // When
        AppConfig config = provider.get();

        // Then
        assertNotNull(config);
        assertEquals(16, config.font().size());
        assertEquals(2, config.dictionary().categories().size());
        assertEquals(2, config.difficulty().levels().size());
        assertEquals(10, config.difficulty().levels().get("easy").attempts());
    }

    @Test
    void Throw_Exception_When_File_Not_Found() {
        // Given
        File nonExistentFile = new File("non-existent-config.yaml");
        YamlConfigProvider provider = new YamlConfigProvider(nonExistentFile);

        // When & Then
        assertThrows(RuntimeException.class, provider::get);
    }

    @Test
    void Throw_Exception_When_Path_Is_Null() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> new YamlConfigProvider(null));
    }
}
