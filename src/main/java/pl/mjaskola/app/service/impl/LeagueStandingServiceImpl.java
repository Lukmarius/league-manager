package pl.mjaskola.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mjaskola.app.domain.LeagueStanding;
import pl.mjaskola.app.repository.LeagueStandingRepository;
import pl.mjaskola.app.service.LeagueStandingService;
import pl.mjaskola.app.service.dto.LeagueStandingDTO;
import pl.mjaskola.app.service.mapper.LeagueStandingMapper;

/**
 * Service Implementation for managing {@link LeagueStanding}.
 */
@Service
@Transactional
public class LeagueStandingServiceImpl implements LeagueStandingService {

    private final Logger log = LoggerFactory.getLogger(LeagueStandingServiceImpl.class);

    private final LeagueStandingRepository leagueStandingRepository;

    private final LeagueStandingMapper leagueStandingMapper;

    public LeagueStandingServiceImpl(LeagueStandingRepository leagueStandingRepository, LeagueStandingMapper leagueStandingMapper) {
        this.leagueStandingRepository = leagueStandingRepository;
        this.leagueStandingMapper = leagueStandingMapper;
    }

    @Override
    public LeagueStandingDTO save(LeagueStandingDTO leagueStandingDTO) {
        log.debug("Request to save LeagueStanding : {}", leagueStandingDTO);
        LeagueStanding leagueStanding = leagueStandingMapper.toEntity(leagueStandingDTO);
        leagueStanding = leagueStandingRepository.save(leagueStanding);
        return leagueStandingMapper.toDto(leagueStanding);
    }

    @Override
    public Optional<LeagueStandingDTO> partialUpdate(LeagueStandingDTO leagueStandingDTO) {
        log.debug("Request to partially update LeagueStanding : {}", leagueStandingDTO);

        return leagueStandingRepository
            .findById(leagueStandingDTO.getId())
            .map(
                existingLeagueStanding -> {
                    leagueStandingMapper.partialUpdate(existingLeagueStanding, leagueStandingDTO);
                    return existingLeagueStanding;
                }
            )
            .map(leagueStandingRepository::save)
            .map(leagueStandingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LeagueStandingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LeagueStandings");
        return leagueStandingRepository.findAll(pageable).map(leagueStandingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LeagueStandingDTO> findOne(Long id) {
        log.debug("Request to get LeagueStanding : {}", id);
        return leagueStandingRepository.findById(id).map(leagueStandingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete LeagueStanding : {}", id);
        leagueStandingRepository.deleteById(id);
    }
}
