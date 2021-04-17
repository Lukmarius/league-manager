package pl.mjaskola.app.service.mapper;

import org.mapstruct.Mapper;
import pl.mjaskola.app.domain.League;
import pl.mjaskola.app.service.dto.LeagueWithListsDTO;

@Mapper(componentModel = "spring", uses = {})
public interface LeagueWithListsMapper {
    LeagueWithListsDTO toDto(League entity);
}
