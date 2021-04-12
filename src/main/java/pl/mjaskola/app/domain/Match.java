package pl.mjaskola.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Match.
 */
@Entity
@Table(name = "match")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Team homeTeam;

    @ManyToOne
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Team awayTeam;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "id", nullable = true)
    private MatchResult matchResult;

    @ManyToOne
    @JsonIgnoreProperties(value = { "matches", "league" }, allowSetters = true)
    private Round round;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match id(Long id) {
        this.id = id;
        return this;
    }

    public Team getHomeTeam() {
        return this.homeTeam;
    }

    public Match homeTeam(Team team) {
        this.setHomeTeam(team);
        return this;
    }

    public void setHomeTeam(Team team) {
        this.homeTeam = team;
    }

    public Team getAwayTeam() {
        return this.awayTeam;
    }

    public Match awayTeam(Team team) {
        this.setAwayTeam(team);
        return this;
    }

    public void setAwayTeam(Team team) {
        this.awayTeam = team;
    }

    public MatchResult getMatchResult() {
        return this.matchResult;
    }

    public Match matchResult(MatchResult matchResult) {
        this.setMatchResult(matchResult);
        return this;
    }

    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult;
        if (matchResult != null) {
            this.matchResult.setMatch(this);
        }
    }

    public Round getRound() {
        return this.round;
    }

    public Match round(Round round) {
        this.setRound(round);
        return this;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Match)) {
            return false;
        }
        return id != null && id.equals(((Match) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Match{" +
            "id=" + getId() +
            "}";
    }
}
