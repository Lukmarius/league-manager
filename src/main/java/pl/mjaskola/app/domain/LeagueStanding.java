package pl.mjaskola.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeagueStanding.
 */
@Entity
@Table(name = "league_standing")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeagueStanding implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "position", nullable = false)
    private Integer position;

    @NotNull
    @Column(name = "points", nullable = false)
    private Integer points;

    @NotNull
    @Min(value = 0)
    @Column(name = "scored_goals", nullable = false)
    private Integer scoredGoals;

    @NotNull
    @Min(value = 0)
    @Column(name = "lost_goals", nullable = false)
    private Integer lostGoals;

    @NotNull
    @Min(value = 0)
    @Column(name = "wins", nullable = false)
    private Integer wins;

    @NotNull
    @Min(value = 0)
    @Column(name = "draws", nullable = false)
    private Integer draws;

    @NotNull
    @Min(value = 0)
    @Column(name = "losses", nullable = false)
    private Integer losses;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Team team;

    @ManyToOne
    private League league;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeagueStanding id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getPosition() {
        return this.position;
    }

    public LeagueStanding position(Integer position) {
        this.position = position;
        return this;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPoints() {
        return this.points;
    }

    public LeagueStanding points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getScoredGoals() {
        return this.scoredGoals;
    }

    public LeagueStanding scoredGoals(Integer scoredGoals) {
        this.scoredGoals = scoredGoals;
        return this;
    }

    public void setScoredGoals(Integer scoredGoals) {
        this.scoredGoals = scoredGoals;
    }

    public Integer getLostGoals() {
        return this.lostGoals;
    }

    public LeagueStanding lostGoals(Integer lostGoals) {
        this.lostGoals = lostGoals;
        return this;
    }

    public void setLostGoals(Integer lostGoals) {
        this.lostGoals = lostGoals;
    }

    public Integer getWins() {
        return this.wins;
    }

    public LeagueStanding wins(Integer wins) {
        this.wins = wins;
        return this;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getDraws() {
        return this.draws;
    }

    public LeagueStanding draws(Integer draws) {
        this.draws = draws;
        return this;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    public Integer getLosses() {
        return this.losses;
    }

    public LeagueStanding losses(Integer losses) {
        this.losses = losses;
        return this;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Team getTeam() {
        return this.team;
    }

    public LeagueStanding team(Team team) {
        this.setTeam(team);
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public League getLeague() {
        return this.league;
    }

    public LeagueStanding league(League league) {
        this.setLeague(league);
        return this;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeagueStanding)) {
            return false;
        }
        return id != null && id.equals(((LeagueStanding) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeagueStanding{" +
            "id=" + getId() +
            ", position=" + getPosition() +
            ", points=" + getPoints() +
            ", scoredGoals=" + getScoredGoals() +
            ", lostGoals=" + getLostGoals() +
            ", wins=" + getWins() +
            ", draws=" + getDraws() +
            ", losses=" + getLosses() +
            "}";
    }
}
