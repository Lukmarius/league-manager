package pl.mjaskola.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link pl.mjaskola.app.domain.MatchResult} entity.
 */
public class MatchResultDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer homeTeamScore;

    @NotNull
    @Min(value = 0)
    private Integer awayTeamScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchResultDTO)) {
            return false;
        }

        MatchResultDTO matchResultDTO = (MatchResultDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, matchResultDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatchResultDTO{" +
            "id=" + getId() +
            ", homeTeamScore=" + getHomeTeamScore() +
            ", awayTeamScore=" + getAwayTeamScore() +
            "}";
    }
}
