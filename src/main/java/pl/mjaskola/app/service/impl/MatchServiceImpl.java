package pl.mjaskola.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mjaskola.app.domain.Match;
import pl.mjaskola.app.repository.MatchRepository;
import pl.mjaskola.app.repository.MatchResultRepository;
import pl.mjaskola.app.service.MatchService;
import pl.mjaskola.app.service.dto.MatchDTO;
import pl.mjaskola.app.service.mapper.MatchMapper;

/**
 * Service Implementation for managing {@link Match}.
 */
@Service
@Transactional
public class MatchServiceImpl implements MatchService {

    private final Logger log = LoggerFactory.getLogger(MatchServiceImpl.class);

    private final MatchRepository matchRepository;

    private final MatchMapper matchMapper;

    private final MatchResultRepository matchResultRepository;

    public MatchServiceImpl(MatchRepository matchRepository, MatchMapper matchMapper, MatchResultRepository matchResultRepository) {
        this.matchRepository = matchRepository;
        this.matchMapper = matchMapper;
        this.matchResultRepository = matchResultRepository;
    }

    @Override
    public MatchDTO save(MatchDTO matchDTO) {
        log.debug("Request to save Match : {}", matchDTO);
        Match match = matchMapper.toEntity(matchDTO);
        Long matchResultId = matchDTO.getMatchResult().getId();
        matchResultRepository.findById(matchResultId).ifPresent(match::matchResult);
        match = matchRepository.save(match);
        return matchMapper.toDto(match);
    }

    @Override
    public Optional<MatchDTO> partialUpdate(MatchDTO matchDTO) {
        log.debug("Request to partially update Match : {}", matchDTO);

        return matchRepository
            .findById(matchDTO.getId())
            .map(
                existingMatch -> {
                    matchMapper.partialUpdate(existingMatch, matchDTO);
                    return existingMatch;
                }
            )
            .map(matchRepository::save)
            .map(matchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Matches");
        return matchRepository.findAll(pageable).map(matchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MatchDTO> findOne(Long id) {
        log.debug("Request to get Match : {}", id);
        return matchRepository.findById(id).map(matchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Match : {}", id);
        matchRepository.deleteById(id);
    }
}
