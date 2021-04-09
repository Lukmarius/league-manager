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
 * Criteria class for the {@link pl.mjaskola.app.domain.Round} entity. This class is used
 * in {@link pl.mjaskola.app.web.rest.RoundResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rounds?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RoundCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter roundNumber;

    private LongFilter matchId;

    private LongFilter leagueId;

    public RoundCriteria() {}

    public RoundCriteria(RoundCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.roundNumber = other.roundNumber == null ? null : other.roundNumber.copy();
        this.matchId = other.matchId == null ? null : other.matchId.copy();
        this.leagueId = other.leagueId == null ? null : other.leagueId.copy();
    }

    @Override
    public RoundCriteria copy() {
        return new RoundCriteria(this);
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

    public IntegerFilter getRoundNumber() {
        return roundNumber;
    }

    public IntegerFilter roundNumber() {
        if (roundNumber == null) {
            roundNumber = new IntegerFilter();
        }
        return roundNumber;
    }

    public void setRoundNumber(IntegerFilter roundNumber) {
        this.roundNumber = roundNumber;
    }

    public LongFilter getMatchId() {
        return matchId;
    }

    public LongFilter matchId() {
        if (matchId == null) {
            matchId = new LongFilter();
        }
        return matchId;
    }

    public void setMatchId(LongFilter matchId) {
        this.matchId = matchId;
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
        final RoundCriteria that = (RoundCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(roundNumber, that.roundNumber) &&
            Objects.equals(matchId, that.matchId) &&
            Objects.equals(leagueId, that.leagueId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roundNumber, matchId, leagueId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoundCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (roundNumber != null ? "roundNumber=" + roundNumber + ", " : "") +
            (matchId != null ? "matchId=" + matchId + ", " : "") +
            (leagueId != null ? "leagueId=" + leagueId + ", " : "") +
            "}";
    }
}
