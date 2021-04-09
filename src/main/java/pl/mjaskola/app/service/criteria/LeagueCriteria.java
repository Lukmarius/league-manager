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
 * Criteria class for the {@link pl.mjaskola.app.domain.League} entity. This class is used
 * in {@link pl.mjaskola.app.web.rest.LeagueResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leagues?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LeagueCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter roundId;

    private LongFilter leagueStandingId;

    public LeagueCriteria() {}

    public LeagueCriteria(LeagueCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.roundId = other.roundId == null ? null : other.roundId.copy();
        this.leagueStandingId = other.leagueStandingId == null ? null : other.leagueStandingId.copy();
    }

    @Override
    public LeagueCriteria copy() {
        return new LeagueCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getRoundId() {
        return roundId;
    }

    public LongFilter roundId() {
        if (roundId == null) {
            roundId = new LongFilter();
        }
        return roundId;
    }

    public void setRoundId(LongFilter roundId) {
        this.roundId = roundId;
    }

    public LongFilter getLeagueStandingId() {
        return leagueStandingId;
    }

    public LongFilter leagueStandingId() {
        if (leagueStandingId == null) {
            leagueStandingId = new LongFilter();
        }
        return leagueStandingId;
    }

    public void setLeagueStandingId(LongFilter leagueStandingId) {
        this.leagueStandingId = leagueStandingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LeagueCriteria that = (LeagueCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(roundId, that.roundId) &&
            Objects.equals(leagueStandingId, that.leagueStandingId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roundId, leagueStandingId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeagueCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (roundId != null ? "roundId=" + roundId + ", " : "") +
            (leagueStandingId != null ? "leagueStandingId=" + leagueStandingId + ", " : "") +
            "}";
    }
}
