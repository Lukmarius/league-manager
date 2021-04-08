package pl.mjaskola.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link pl.mjaskola.app.domain.LeagueStanding} entity. This class is used
 * in {@link pl.mjaskola.app.web.rest.LeagueStandingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /league-standings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeagueStandingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter position;

    private IntegerFilter points;

    private IntegerFilter scoredGoals;

    private IntegerFilter lostGoals;

    private IntegerFilter wins;

    private IntegerFilter draws;

    private IntegerFilter losses;

    private LongFilter teamId;

    private LongFilter leagueId;

    public LeagueStandingCriteria() {}

    public LeagueStandingCriteria(LeagueStandingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.position = other.position == null ? null : other.position.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.scoredGoals = other.scoredGoals == null ? null : other.scoredGoals.copy();
        this.lostGoals = other.lostGoals == null ? null : other.lostGoals.copy();
        this.wins = other.wins == null ? null : other.wins.copy();
        this.draws = other.draws == null ? null : other.draws.copy();
        this.losses = other.losses == null ? null : other.losses.copy();
        this.teamId = other.teamId == null ? null : other.teamId.copy();
        this.leagueId = other.leagueId == null ? null : other.leagueId.copy();
    }

    @Override
    public LeagueStandingCriteria copy() {
        return new LeagueStandingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPosition() {
        return position;
    }

    public IntegerFilter position() {
        if (position == null) {
            position = new IntegerFilter();
        }
        return position;
    }

    public void setPosition(IntegerFilter position) {
        this.position = position;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public IntegerFilter points() {
        if (points == null) {
            points = new IntegerFilter();
        }
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
    }

    public IntegerFilter getScoredGoals() {
        return scoredGoals;
    }

    public IntegerFilter scoredGoals() {
        if (scoredGoals == null) {
            scoredGoals = new IntegerFilter();
        }
        return scoredGoals;
    }

    public void setScoredGoals(IntegerFilter scoredGoals) {
        this.scoredGoals = scoredGoals;
    }

    public IntegerFilter getLostGoals() {
        return lostGoals;
    }

    public IntegerFilter lostGoals() {
        if (lostGoals == null) {
            lostGoals = new IntegerFilter();
        }
        return lostGoals;
    }

    public void setLostGoals(IntegerFilter lostGoals) {
        this.lostGoals = lostGoals;
    }

    public IntegerFilter getWins() {
        return wins;
    }

    public IntegerFilter wins() {
        if (wins == null) {
            wins = new IntegerFilter();
        }
        return wins;
    }

    public void setWins(IntegerFilter wins) {
        this.wins = wins;
    }

    public IntegerFilter getDraws() {
        return draws;
    }

    public IntegerFilter draws() {
        if (draws == null) {
            draws = new IntegerFilter();
        }
        return draws;
    }

    public void setDraws(IntegerFilter draws) {
        this.draws = draws;
    }

    public IntegerFilter getLosses() {
        return losses;
    }

    public IntegerFilter losses() {
        if (losses == null) {
            losses = new IntegerFilter();
        }
        return losses;
    }

    public void setLosses(IntegerFilter losses) {
        this.losses = losses;
    }

    public LongFilter getTeamId() {
        return teamId;
    }

    public LongFilter teamId() {
        if (teamId == null) {
            teamId = new LongFilter();
        }
        return teamId;
    }

    public void setTeamId(LongFilter teamId) {
        this.teamId = teamId;
    }

    public LongFilter getLeagueId() {
        return leagueId;
    }

    public LongFilter leagueId() {
        if (leagueId == null) {
            leagueId = new LongFilter();
        }
        return leagueId;
    }

    public void setLeagueId(LongFilter leagueId) {
        this.leagueId = leagueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LeagueStandingCriteria that = (LeagueStandingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(position, that.position) &&
            Objects.equals(points, that.points) &&
            Objects.equals(scoredGoals, that.scoredGoals) &&
            Objects.equals(lostGoals, that.lostGoals) &&
            Objects.equals(wins, that.wins) &&
            Objects.equals(draws, that.draws) &&
            Objects.equals(losses, that.losses) &&
            Objects.equals(teamId, that.teamId) &&
            Objects.equals(leagueId, that.leagueId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, points, scoredGoals, lostGoals, wins, draws, losses, teamId, leagueId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeagueStandingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (position != null ? "position=" + position + ", " : "") +
            (points != null ? "points=" + points + ", " : "") +
            (scoredGoals != null ? "scoredGoals=" + scoredGoals + ", " : "") +
            (lostGoals != null ? "lostGoals=" + lostGoals + ", " : "") +
            (wins != null ? "wins=" + wins + ", " : "") +
            (draws != null ? "draws=" + draws + ", " : "") +
            (losses != null ? "losses=" + losses + ", " : "") +
            (teamId != null ? "teamId=" + teamId + ", " : "") +
            (leagueId != null ? "leagueId=" + leagueId + ", " : "") +
            "}";
    }
}
