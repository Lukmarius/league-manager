package pl.mjaskola.app.service;

import pl.mjaskola.app.service.dto.LeagueDTO;
import pl.mjaskola.app.web.websocket.dto.LeagueCreationRequest;

public interface LeagueCreator {
    LeagueDTO create(LeagueCreationRequest request);
}
