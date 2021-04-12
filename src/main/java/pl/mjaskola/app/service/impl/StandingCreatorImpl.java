package pl.mjaskola.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;
import pl.mjaskola.app.domain.LeagueStanding;
import pl.mjaskola.app.domain.Team;
import pl.mjaskola.app.service.StandingCreator;

@Component
public class StandingCreatorImpl implements StandingCreator {

    @Override
    public List<LeagueStanding> create(List<Team> teams) {
        List<LeagueStanding> standings = new LinkedList<>();
        for (Team team : teams) {
            LeagueStanding standing = new LeagueStanding();
            standing.setTeam(team);
            standings.add(standing);
        }

        return standings;
    }
}
