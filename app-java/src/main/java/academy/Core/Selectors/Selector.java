package academy.Core.Selectors;

import java.util.List;

import academy.Core.model.DifficultyLevel;

public interface Selector {

    DifficultyLevel select();

    List<DifficultyLevel> getAvailableLevels();

    boolean hasAvailableLevels();

    DifficultyLevel selectRandom();
}
