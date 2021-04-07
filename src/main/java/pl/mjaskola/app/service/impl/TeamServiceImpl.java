package pl.mjaskola.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mjaskola.app.domain.Team;
import pl.mjaskola.app.domain.User;
import pl.mjaskola.app.repository.TeamRepository;
import pl.mjaskola.app.service.TeamService;
import pl.mjaskola.app.service.UserService;
import pl.mjaskola.app.service.dto.TeamDTO;
import pl.mjaskola.app.service.mapper.TeamMapper;

/**
 * Service Implementation for managing {@link Team}.
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {

    private final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

    private final TeamRepository teamRepository;
    private final UserService userService;
    private final TeamMapper teamMapper;

    public TeamServiceImpl(TeamRepository teamRepository, UserService userService, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.teamMapper = teamMapper;
    }

    @Override
    public TeamDTO save(TeamDTO teamDTO) {
        log.debug("Request to save Team : {}", teamDTO);
        Team team = teamMapper.toEntity(teamDTO);
        team = teamRepository.save(team);
        return teamMapper.toDto(team);
    }

    @Override
    public Optional<TeamDTO> partialUpdate(TeamDTO teamDTO) {
        log.debug("Request to partially update Team : {}", teamDTO);

        return teamRepository
            .findById(teamDTO.getId())
            .map(
                existingTeam -> {
                    teamMapper.partialUpdate(existingTeam, teamDTO);
                    return existingTeam;
                }
            )
            .map(teamRepository::save)
            .map(teamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamDTO> findAll() {
        log.debug("Request to get all Teams");
        return teamRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(teamMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamDTO> findAllByUser() {
        log.debug("Request to get all Teams");
        Optional<User> optionalUser = userService.getUserWithAuthorities();

        return optionalUser
            .map(user -> teamRepository.findDistinctByUsersContains(user).stream().map(teamMapper::toDto).collect(Collectors.toList()))
            .orElseGet(LinkedList::new);
    }

    public Page<TeamDTO> findAllWithEagerRelationships(Pageable pageable) {
        return teamRepository.findAllWithEagerRelationships(pageable).map(teamMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TeamDTO> findOne(Long id) {
        log.debug("Request to get Team : {}", id);
        return teamRepository.findOneWithEagerRelationships(id).map(teamMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Team : {}", id);
        teamRepository.deleteById(id);
    }
}
