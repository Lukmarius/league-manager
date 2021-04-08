package pl.mjaskola.app.service.mapper;

import org.mapstruct.*;
import pl.mjaskola.app.domain.*;
import pl.mjaskola.app.service.dto.RoundDTO;

/**
 * Mapper for the entity {@link Round} and its DTO {@link RoundDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoundMapper extends EntityMapper<RoundDTO, Round> {
    @Named("roundNumber")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "roundNumber", source = "roundNumber")
    RoundDTO toDtoRoundNumber(Round round);
}
