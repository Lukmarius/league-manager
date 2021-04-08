package pl.mjaskola.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Round.
 */
@Entity
@Table(name = "round")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "round_number")
    private Integer roundNumber;

    @OneToMany(mappedBy = "round")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "homeTeam", "awayTeam", "matchResult", "round" }, allowSetters = true)
    private Set<Match> matches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Round id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRoundNumber() {
        return this.roundNumber;
    }

    public Round roundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
        return this;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Set<Match> getMatches() {
        return this.matches;
    }

    public Round matches(Set<Match> matches) {
        this.setMatches(matches);
        return this;
    }

    public Round addMatch(Match match) {
        this.matches.add(match);
        match.setRound(this);
        return this;
    }

    public Round removeMatch(Match match) {
        this.matches.remove(match);
        match.setRound(null);
        return this;
    }

    public void setMatches(Set<Match> matches) {
        if (this.matches != null) {
            this.matches.forEach(i -> i.setRound(null));
        }
        if (matches != null) {
            matches.forEach(i -> i.setRound(this));
        }
        this.matches = matches;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Round)) {
            return false;
        }
        return id != null && id.equals(((Round) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Round{" +
            "id=" + getId() +
            ", roundNumber=" + getRoundNumber() +
            "}";
    }
}
