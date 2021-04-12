package pl.mjaskola.app.service;

import java.util.List;
import pl.mjaskola.app.domain.LeagueStanding;
import pl.mjaskola.app.domain.Team;

public interface StandingCreator {
    List<LeagueStanding> create(List<Team> teams);
}
