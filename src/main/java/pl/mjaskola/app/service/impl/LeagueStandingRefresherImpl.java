package pl.mjaskola.app.service.impl;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mjaskola.app.domain.*;
import pl.mjaskola.app.repository.LeagueRepository;
import pl.mjaskola.app.repository.LeagueStandingRepository;
import pl.mjaskola.app.repository.RoundRepository;
import pl.mjaskola.app.service.LeagueStandingRefresher;

@Service
@Transactional
public class LeagueStandingRefresherImpl implements LeagueStandingRefresher {

    private final LeagueStandingRepository leagueStandingRepository;
    private final LeagueRepository leagueRepository;
    private final RoundRepository roundRepository;

    public LeagueStandingRefresherImpl(
        LeagueStandingRepository leagueStandingRepository,
        LeagueRepository leagueRepository,
        RoundRepository roundRepository
    ) {
        this.leagueStandingRepository = leagueStandingRepository;
        this.leagueRepository = leagueRepository;
        this.roundRepository = roundRepository;
    }

    @Override
    public void refreshLeagueStanding(Match matchEntity) {
        Optional<League> leagueOptional = leagueRepository.findOneByMatch(matchEntity);
        League league = leagueOptional.orElse(new League());
        Set<LeagueStanding> standings = league.getLeagueStandings();
        Set<Round> rounds = roundRepository.findAllByLeague(league);
        Set<Match> allMatches = rounds
            .stream()
            .map(Round::getMatches)
            .collect(Collectors.toSet())
            .stream()
            .flatMap(Set::stream)
            .filter(match -> nonNull(match.getMatchResult()))
            .collect(Collectors.toSet());

        standings.forEach(
            standing -> {
                Team team = standing.getTeam();
                resetStandings(standing);
                setStandingForHomeGames(allMatches, standing, team);
                setStandingForAwayGames(allMatches, standing, team);
            }
        );

        List<LeagueStanding> sorted = standings.stream().sorted(this::sortAndSetPositions).collect(Collectors.toList());

        for (int i = 1; i <= sorted.size(); i++) {
            sorted.get(i - 1).setPosition(i);
        }

        leagueStandingRepository.saveAll(sorted);
    }

    private void resetStandings(LeagueStanding standing) {
        standing.setPoints(0);
        standing.setScoredGoals(0);
        standing.setLostGoals(0);
        standing.setWins(0);
        standing.setDraws(0);
        standing.setLosses(0);
    }

    private void setStandingForHomeGames(Set<Match> allMatches, LeagueStanding standing, Team team) {
        allMatches
            .stream()
            .filter(match -> match.getHomeTeam().getId().equals(team.getId()))
            .forEach(
                match -> {
                    MatchResult result = match.getMatchResult();
                    int score = result.getHomeTeamScore();
                    int lost = result.getAwayTeamScore();
                    setPointsAndGoals(standing, score, lost);
                }
            );
    }

    private void setStandingForAwayGames(Set<Match> allMatches, LeagueStanding standing, Team team) {
        allMatches
            .stream()
            .filter(match -> match.getAwayTeam().getId().equals(team.getId()))
            .forEach(
                match -> {
                    MatchResult result = match.getMatchResult();
                    int score = result.getAwayTeamScore();
                    int lost = result.getHomeTeamScore();
                    setPointsAndGoals(standing, score, lost);
                }
            );
    }

    private void setPointsAndGoals(LeagueStanding standing, int score, int lost) {
        int points;
        if (score == lost) {
            points = 1;
            standing.setDraws(standing.getDraws() + 1);
        } else if (score > lost) {
            points = 3;
            standing.setWins(standing.getWins() + 1);
        } else {
            points = 0;
            standing.setLosses(standing.getLosses() + 1);
        }

        standing.setPoints(standing.getPoints() + points);
        standing.setScoredGoals(standing.getScoredGoals() + score);
        standing.setLostGoals(standing.getLostGoals() + lost);
    }

    private int sortAndSetPositions(LeagueStanding t1, LeagueStanding t2) {
        int compare = t2.getPoints().compareTo(t1.getPoints());
        if (compare == 0) {
            Integer goals1 = t1.getScoredGoals() - t1.getLostGoals();
            Integer goals2 = t2.getScoredGoals() - t2.getLostGoals();
            compare = goals2.compareTo(goals1);
        }
        if (compare == 0) {
            compare = t2.getScoredGoals().compareTo(t1.getScoredGoals());
        }

        return compare;
    }
}
