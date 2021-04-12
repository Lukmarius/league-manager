package pl.mjaskola.app.web.websocket.dto;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import pl.mjaskola.app.service.dto.TeamDTO;

public class LeagueCreationRequest {

    private Long id;

    @NotNull
    private String name;

    @NotEmpty
    private List<TeamDTO> teams;

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
