package pl.mjaskola.app.service.mapper;

import org.mapstruct.*;
import pl.mjaskola.app.domain.*;
import pl.mjaskola.app.service.dto.TeamDTO;

/**
 * Mapper for the entity {@link Team} and its DTO {@link TeamDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TeamMapper extends EntityMapper<TeamDTO, Team> {
    @Mapping(target = "users", source = "users", qualifiedByName = "loginSet")
    TeamDTO toDto(Team s);

    @Mapping(target = "removeUser", ignore = true)
    Team toEntity(TeamDTO teamDTO);
}
