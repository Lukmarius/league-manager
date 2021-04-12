package pl.mjaskola.app.service.mapper;

import org.mapstruct.*;
import pl.mjaskola.app.domain.*;
import pl.mjaskola.app.service.dto.MatchDTO;

/**
 * Mapper for the entity {@link Match} and its DTO {@link MatchDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamMapper.class, MatchResultMapper.class, RoundMapper.class })
public interface MatchMapper extends EntityMapper<MatchDTO, Match> {
    @Mapping(target = "homeTeam", source = "homeTeam", qualifiedByName = "name")
    @Mapping(target = "awayTeam", source = "awayTeam", qualifiedByName = "name")
    @Mapping(target = "matchResult", source = "matchResult", qualifiedByName = "id")
    MatchDTO toDto(Match s);
}
