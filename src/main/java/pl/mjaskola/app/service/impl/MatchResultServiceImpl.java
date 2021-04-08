package pl.mjaskola.app.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mjaskola.app.domain.MatchResult;
import pl.mjaskola.app.repository.MatchResultRepository;
import pl.mjaskola.app.service.MatchResultService;
import pl.mjaskola.app.service.dto.MatchResultDTO;
import pl.mjaskola.app.service.mapper.MatchResultMapper;

/**
 * Service Implementation for managing {@link MatchResult}.
 */
@Service
@Transactional
public class MatchResultServiceImpl implements MatchResultService {

    private final Logger log = LoggerFactory.getLogger(MatchResultServiceImpl.class);

    private final MatchResultRepository matchResultRepository;

    private final MatchResultMapper matchResultMapper;

    public MatchResultServiceImpl(MatchResultRepository matchResultRepository, MatchResultMapper matchResultMapper) {
        this.matchResultRepository = matchResultRepository;
        this.matchResultMapper = matchResultMapper;
    }

    @Override
    public MatchResultDTO save(MatchResultDTO matchResultDTO) {
        log.debug("Request to save MatchResult : {}", matchResultDTO);
        MatchResult matchResult = matchResultMapper.toEntity(matchResultDTO);
        matchResult = matchResultRepository.save(matchResult);
        return matchResultMapper.toDto(matchResult);
    }

    @Override
    public Optional<MatchResultDTO> partialUpdate(MatchResultDTO matchResultDTO) {
        log.debug("Request to partially update MatchResult : {}", matchResultDTO);

        return matchResultRepository
            .findById(matchResultDTO.getId())
            .map(
                existingMatchResult -> {
                    matchResultMapper.partialUpdate(existingMatchResult, matchResultDTO);
                    return existingMatchResult;
                }
            )
            .map(matchResultRepository::save)
            .map(matchResultMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchResultDTO> findAll() {
        log.debug("Request to get all MatchResults");
        return matchResultRepository.findAll().stream().map(matchResultMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MatchResultDTO> findOne(Long id) {
        log.debug("Request to get MatchResult : {}", id);
        return matchResultRepository.findById(id).map(matchResultMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MatchResult : {}", id);
        matchResultRepository.deleteById(id);
    }
}
