package academy.Config.Providers;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import academy.Config.AppConfig;
import academy.Config.ConfigProvider;

public class YamlConfigProvider implements ConfigProvider {

    private final File configPath;
    private final ObjectMapper YAML_READER = new ObjectMapper(new YAMLFactory());

    public YamlConfigProvider(File configPath) {
        if (configPath == null) {
            throw new IllegalArgumentException("Путь к файлу не может быть null.");
        }

        this.configPath = configPath;
    }

    @Override
    public AppConfig get() {
        try {
            return YAML_READER.readValue(configPath, AppConfig.class);
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка чтения YAML-файла: " + configPath, e);
        }
    }
}
