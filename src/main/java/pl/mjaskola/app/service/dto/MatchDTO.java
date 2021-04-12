package pl.mjaskola.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.mjaskola.app.domain.Match} entity.
 */
public class MatchDTO implements Serializable {

    private Long id;

    private TeamDTO homeTeam;

    private TeamDTO awayTeam;

    private MatchResultDTO matchResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TeamDTO getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TeamDTO homeTeam) {
        this.homeTeam = homeTeam;
    }

    public TeamDTO getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(TeamDTO awayTeam) {
        this.awayTeam = awayTeam;
    }

    public MatchResultDTO getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MatchResultDTO matchResult) {
        this.matchResult = matchResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchDTO)) {
            return false;
        }

        MatchDTO matchDTO = (MatchDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matchDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatchDTO{" +
            "id=" + getId() +
            ", homeTeam=" + getHomeTeam() +
            ", awayTeam=" + getAwayTeam() +
            ", matchResult=" + getMatchResult() +
            "}";
    }
}
