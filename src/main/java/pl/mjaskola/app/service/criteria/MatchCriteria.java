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
 * Criteria class for the {@link pl.mjaskola.app.domain.Match} entity. This class is used
 * in {@link pl.mjaskola.app.web.rest.MatchResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /matches?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MatchCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter homeTeamId;

    private LongFilter awayTeamId;

    private LongFilter matchResultId;

    public MatchCriteria() {}

    public MatchCriteria(MatchCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.homeTeamId = other.homeTeamId == null ? null : other.homeTeamId.copy();
        this.awayTeamId = other.awayTeamId == null ? null : other.awayTeamId.copy();
        this.matchResultId = other.matchResultId == null ? null : other.matchResultId.copy();
    }

    @Override
    public MatchCriteria copy() {
        return new MatchCriteria(this);
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

    public LongFilter getHomeTeamId() {
        return homeTeamId;
    }

    public LongFilter homeTeamId() {
        if (homeTeamId == null) {
            homeTeamId = new LongFilter();
        }
        return homeTeamId;
    }

    public void setHomeTeamId(LongFilter homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public LongFilter getAwayTeamId() {
        return awayTeamId;
    }

    public LongFilter awayTeamId() {
        if (awayTeamId == null) {
            awayTeamId = new LongFilter();
        }
        return awayTeamId;
    }

    public void setAwayTeamId(LongFilter awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public LongFilter getMatchResultId() {
        return matchResultId;
    }

    public LongFilter matchResultId() {
        if (matchResultId == null) {
            matchResultId = new LongFilter();
        }
        return matchResultId;
    }

    public void setMatchResultId(LongFilter matchResultId) {
        this.matchResultId = matchResultId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MatchCriteria that = (MatchCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(homeTeamId, that.homeTeamId) &&
            Objects.equals(awayTeamId, that.awayTeamId) &&
            Objects.equals(matchResultId, that.matchResultId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, homeTeamId, awayTeamId, matchResultId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatchCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (homeTeamId != null ? "homeTeamId=" + homeTeamId + ", " : "") +
            (awayTeamId != null ? "awayTeamId=" + awayTeamId + ", " : "") +
            (matchResultId != null ? "matchResultId=" + matchResultId + ", " : "") +
            "}";
    }
}
