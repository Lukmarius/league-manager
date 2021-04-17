package pl.mjaskola.app.service;

import pl.mjaskola.app.domain.Match;

public interface LeagueStandingRefresher {
    void refreshLeagueStanding(Match matchEntity);
}
