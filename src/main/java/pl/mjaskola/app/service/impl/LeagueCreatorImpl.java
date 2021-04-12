package pl.mjaskola.app.service.impl;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pl.mjaskola.app.domain.League;
import pl.mjaskola.app.domain.LeagueStanding;
import pl.mjaskola.app.domain.Round;
import pl.mjaskola.app.domain.Team;
import pl.mjaskola.app.repository.LeagueRepository;
import pl.mjaskola.app.service.LeagueCreator;
import pl.mjaskola.app.service.StandingCreator;
import pl.mjaskola.app.service.dto.LeagueDTO;
import pl.mjaskola.app.service.dto.TeamDTO;
import pl.mjaskola.app.service.mapper.LeagueMapper;
import pl.mjaskola.app.service.mapper.TeamMapper;
import pl.mjaskola.app.web.rest.errors.BadRequestAlertException;
import pl.mjaskola.app.web.websocket.dto.LeagueCreationRequest;

@Service
@Transactional
public class LeagueCreatorImpl implements LeagueCreator {

    private final TeamMapper teamMapper;
    private final RoundRobinCalculator calculator;
    private final StandingCreator standingService;
    private final LeagueMapper leagueMapper;
    private final LeagueRepository leagueRepository;

    public LeagueCreatorImpl(
        TeamMapper teamMapper,
        RoundRobinCalculator calculator,
        StandingCreator standingService,
        LeagueMapper leagueMapper,
        LeagueRepository leagueRepository
    ) {
        this.teamMapper = teamMapper;
        this.calculator = calculator;
        this.standingService = standingService;
        this.leagueMapper = leagueMapper;
        this.leagueRepository = leagueRepository;
    }

    @Override
    public LeagueDTO create(LeagueCreationRequest request) {
        if (isNull(request.getTeams()) || CollectionUtils.isEmpty(request.getTeams())) {
            throw new BadRequestAlertException("teams are null", "League", "teamsarenull");
        }

        List<TeamDTO> teamDTOS = request.getTeams();
        List<Team> teams = teamMapper.toEntity(teamDTOS);
        List<Round> rounds = calculator.getFixtures(teams, true);
        List<LeagueStanding> standings = standingService.create(teams);

        League league = new League();
        league.setName(request.getName());
        league.setRounds(Set.copyOf(rounds));
        league.setLeagueStandings(Set.copyOf(standings));

        League saved = leagueRepository.save(league);
        return leagueMapper.toDto(saved);
    }
}
