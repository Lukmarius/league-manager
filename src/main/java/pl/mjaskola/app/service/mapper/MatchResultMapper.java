package pl.mjaskola.app.service.mapper;

import org.mapstruct.*;
import pl.mjaskola.app.domain.*;
import pl.mjaskola.app.service.dto.MatchResultDTO;

/**
 * Mapper for the entity {@link MatchResult} and its DTO {@link MatchResultDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MatchResultMapper extends EntityMapper<MatchResultDTO, MatchResult> {}
