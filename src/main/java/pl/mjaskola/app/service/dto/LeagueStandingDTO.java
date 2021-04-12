package pl.mjaskola.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link pl.mjaskola.app.domain.LeagueStanding} entity.
 */
public class LeagueStandingDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer position;

    @NotNull
    private Integer points;

    @NotNull
    @Min(value = 0)
    private Integer scoredGoals;

    @NotNull
    @Min(value = 0)
    private Integer lostGoals;

    @NotNull
    @Min(value = 0)
    private Integer wins;

    @NotNull
    @Min(value = 0)
    private Integer draws;

    @NotNull
    @Min(value = 0)
    private Integer losses;

    private TeamDTO team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getScoredGoals() {
        return scoredGoals;
    }

    public void setScoredGoals(Integer scoredGoals) {
        this.scoredGoals = scoredGoals;
    }

    public Integer getLostGoals() {
        return lostGoals;
    }

    public void setLostGoals(Integer lostGoals) {
        this.lostGoals = lostGoals;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeagueStandingDTO)) {
            return false;
        }

        LeagueStandingDTO leagueStandingDTO = (LeagueStandingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leagueStandingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeagueStandingDTO{" +
            "id=" + getId() +
            ", position=" + getPosition() +
            ", points=" + getPoints() +
            ", scoredGoals=" + getScoredGoals() +
            ", lostGoals=" + getLostGoals() +
            ", wins=" + getWins() +
            ", draws=" + getDraws() +
            ", losses=" + getLosses() +
            ", team=" + getTeam() +
            "}";
    }
}
