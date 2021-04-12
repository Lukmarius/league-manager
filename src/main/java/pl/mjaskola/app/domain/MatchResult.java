package pl.mjaskola.app.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MatchResult.
 */
@Entity
@Table(name = "match_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MatchResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "home_team_score", nullable = false)
    private Integer homeTeamScore;

    @NotNull
    @Min(value = 0)
    @Column(name = "away_team_score", nullable = false)
    private Integer awayTeamScore;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id", referencedColumnName = "id")
    private Match match;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MatchResult id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getHomeTeamScore() {
        return this.homeTeamScore;
    }

    public MatchResult homeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
        return this;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return this.awayTeamScore;
    }

    public MatchResult awayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
        return this;
    }

    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchResult)) {
            return false;
        }
        return id != null && id.equals(((MatchResult) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MatchResult{" +
            "id=" + getId() +
            ", homeTeamScore=" + getHomeTeamScore() +
            ", awayTeamScore=" + getAwayTeamScore() +
            "}";
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
