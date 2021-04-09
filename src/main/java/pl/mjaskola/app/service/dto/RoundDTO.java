package pl.mjaskola.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.mjaskola.app.domain.Round} entity.
 */
public class RoundDTO implements Serializable {

    private Long id;

    private Integer roundNumber;

    private LeagueDTO league;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public LeagueDTO getLeague() {
        return league;
    }

    public void setLeague(LeagueDTO league) {
        this.league = league;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoundDTO)) {
            return false;
        }

        RoundDTO roundDTO = (RoundDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roundDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoundDTO{" +
            "id=" + getId() +
            ", roundNumber=" + getRoundNumber() +
            ", league=" + getLeague() +
            "}";
    }
}
