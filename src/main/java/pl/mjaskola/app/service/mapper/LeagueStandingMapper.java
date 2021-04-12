package pl.mjaskola.app.service.mapper;

import org.mapstruct.*;
import pl.mjaskola.app.domain.*;
import pl.mjaskola.app.service.dto.LeagueStandingDTO;

/**
 * Mapper for the entity {@link LeagueStanding} and its DTO {@link LeagueStandingDTO}.
 */
@Mapper(componentModel = "spring", uses = { TeamMapper.class, LeagueMapper.class })
public interface LeagueStandingMapper extends EntityMapper<LeagueStandingDTO, LeagueStanding> {
    @Mapping(target = "team", source = "team", qualifiedByName = "name")
    LeagueStandingDTO toDto(LeagueStanding s);
}
