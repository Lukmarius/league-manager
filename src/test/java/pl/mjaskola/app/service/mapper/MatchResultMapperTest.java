package pl.mjaskola.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchResultMapperTest {

    private MatchResultMapper matchResultMapper;

    @BeforeEach
    public void setUp() {
        matchResultMapper = new MatchResultMapperImpl();
    }
}
