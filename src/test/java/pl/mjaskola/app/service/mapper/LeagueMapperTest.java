package pl.mjaskola.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeagueMapperTest {

    private LeagueMapper leagueMapper;

    @BeforeEach
    public void setUp() {
        leagueMapper = new LeagueMapperImpl();
    }
}
