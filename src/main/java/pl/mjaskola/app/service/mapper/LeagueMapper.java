package pl.mjaskola.app.service.mapper;

import org.mapstruct.*;
import pl.mjaskola.app.domain.*;
import pl.mjaskola.app.service.dto.LeagueDTO;

/**
 * Mapper for the entity {@link League} and its DTO {@link LeagueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeagueMapper extends EntityMapper<LeagueDTO, League> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "rounds", source = "rounds")
    @Mapping(target = "leagueStandings", source = "leagueStandings")
    LeagueDTO toDtoName(League league);
}
