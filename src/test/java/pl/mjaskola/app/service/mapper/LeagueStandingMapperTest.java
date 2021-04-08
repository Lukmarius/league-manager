package pl.mjaskola.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeagueStandingMapperTest {

    private LeagueStandingMapper leagueStandingMapper;

    @BeforeEach
    public void setUp() {
        leagueStandingMapper = new LeagueStandingMapperImpl();
    }
}
