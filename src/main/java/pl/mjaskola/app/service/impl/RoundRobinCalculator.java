package pl.mjaskola.app.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;
import pl.mjaskola.app.domain.League;
import pl.mjaskola.app.domain.Match;
import pl.mjaskola.app.domain.Round;
import pl.mjaskola.app.domain.Team;

@Component
public class RoundRobinCalculator {

    private List<Team> teams;
    private int rematches;

    private List<Team> array1;
    private List<Team> array2;

    public RoundRobinCalculator(List teams) {
        this.teams = teams;
        prepareArrays();
    }

    public void prepareArrays() {
        array1 = new LinkedList<>();
        array2 = new LinkedList<>();
        for (int i = 0; i < teams.size(); i++) {
            if (i < (teams.size() + 1) / 2) array1.add(teams.get(i)); else array2.add(0, teams.get(i));
        }
    }

    public League createLeague(boolean rematch) {
        rematches = getRematches(rematch);
        List<Round> fixtures = getFixtures(teams, rematch);
        League league = new League();
        return league.rounds(Set.copyOf(fixtures));
    }

    public List<Round> getFixtures(List<Team> teams, boolean rematch) {
        this.teams = teams;
        rematches = getRematches(rematch);
        prepareArrays();
        List<Round> rounds = new ArrayList<>();
        int allRoundsNumber = (this.teams.size() - 1) * rematches;

        for (int i = 1; i <= allRoundsNumber; i++) {
            Round round = createRound(i);
            rounds.add(round);
            moveTeamsForNextRound();
        }

        return rounds;
    }

    private Round createRound(int i) {
        List<Match> matches = getCurrentMatches(i);
        Round round = new Round();
        round.setRoundNumber(i);
        round.setMatches(Set.copyOf(matches));
        return round;
    }

    private void moveTeamsForNextRound() {
        Team fromArray2 = array2.remove(0);
        array1.add(1, fromArray2);

        Team fromArray1 = array1.remove(array1.size() - 1);
        array2.add(fromArray1);
    }

    private List<Match> getCurrentMatches(int roundNumber) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < array1.size(); i++) {
            matches.add(getMatch(i, roundNumber));
        }
        return matches;
    }

    private Match getMatch(int i, int roundNumber) {
        Team t1 = array1.get(i);
        Team t2 = array2.get(i);
        Match match = new Match();

        if (i == 0 && roundNumber % 2 == 0) {
            // swap first team if round is odd
            match.setHomeTeam(t2);
            match.setAwayTeam(t1);
        } else {
            match.setHomeTeam(t1);
            match.setAwayTeam(t2);
        }
        return match;
    }

    public List getTeams() {
        return teams;
    }

    public void setTeams(List teams) {
        this.teams = teams;
    }

    private int getRematches(boolean rematch) {
        return rematch ? 2 : 1;
    }
}
