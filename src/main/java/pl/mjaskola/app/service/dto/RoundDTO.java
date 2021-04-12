package pl.mjaskola.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link pl.mjaskola.app.domain.Round} entity.
 */
public class RoundDTO implements Serializable {

    private Long id;

    private Integer roundNumber;

    private Set<MatchDTO> matches;

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
            "}";
    }

    public Set<MatchDTO> getMatches() {
        return matches;
    }

    public void setMatches(Set<MatchDTO> matches) {
        this.matches = matches;
    }
}
