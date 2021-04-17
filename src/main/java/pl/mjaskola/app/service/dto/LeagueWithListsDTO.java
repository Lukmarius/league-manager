package pl.mjaskola.app.service.dto;

import java.io.Serializable;
import java.util.*;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link pl.mjaskola.app.domain.League} entity.
 */
public class LeagueWithListsDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private List<RoundDTO> rounds;

    private List<LeagueStandingDTO> leagueStandings;

    public void setLeagueStandings(Set<LeagueStandingDTO> set) {
        List<LeagueStandingDTO> list = new LinkedList<>(set);
        list.sort((t2, t1) -> t1.getPosition().compareTo(t2.getPosition()));

        this.leagueStandings = list;
    }

    public void setRounds(Set<RoundDTO> set) {
        List<RoundDTO> list = new LinkedList<>(set);
        list.sort((r2, r1) -> r1.getRoundNumber().compareTo(r2.getRoundNumber()));

        this.rounds = list;
    }

    public void setRounds(List<RoundDTO> rounds) {
        this.rounds = rounds;
        this.rounds.sort(Comparator.comparing(RoundDTO::getRoundNumber));
    }

    public void setLeagueStandings(List<LeagueStandingDTO> leagueStandings) {
        this.leagueStandings = leagueStandings;
        this.leagueStandings.sort(Comparator.comparing(LeagueStandingDTO::getPosition));
    }

    public List<RoundDTO> getRounds() {
        return rounds;
    }

    public List<LeagueStandingDTO> getLeagueStandings() {
        return leagueStandings;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeagueWithListsDTO)) {
            return false;
        }

        LeagueWithListsDTO leagueDTO = (LeagueWithListsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leagueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeagueDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
