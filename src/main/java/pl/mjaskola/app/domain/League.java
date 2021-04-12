package pl.mjaskola.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A League.
 */
@Entity
@Table(name = "league")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class League implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "league", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "matches", "league" }, allowSetters = true)
    private Set<Round> rounds = new HashSet<>();

    @OneToMany(mappedBy = "league", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "team", "league" }, allowSetters = true)
    private Set<LeagueStanding> leagueStandings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public League id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public League name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Round> getRounds() {
        return this.rounds;
    }

    public League rounds(Set<Round> rounds) {
        this.setRounds(rounds);
        return this;
    }

    public League addRound(Round round) {
        this.rounds.add(round);
        round.setLeague(this);
        return this;
    }

    public League removeRound(Round round) {
        this.rounds.remove(round);
        round.setLeague(null);
        return this;
    }

    public void setRounds(Set<Round> rounds) {
        if (this.rounds != null) {
            this.rounds.forEach(i -> i.setLeague(null));
        }
        if (rounds != null) {
            rounds.forEach(i -> i.setLeague(this));
        }
        this.rounds = rounds;
    }

    public Set<LeagueStanding> getLeagueStandings() {
        return this.leagueStandings;
    }

    public League leagueStandings(Set<LeagueStanding> leagueStandings) {
        this.setLeagueStandings(leagueStandings);
        return this;
    }

    public League addLeagueStanding(LeagueStanding leagueStanding) {
        this.leagueStandings.add(leagueStanding);
        leagueStanding.setLeague(this);
        return this;
    }

    public League removeLeagueStanding(LeagueStanding leagueStanding) {
        this.leagueStandings.remove(leagueStanding);
        leagueStanding.setLeague(null);
        return this;
    }

    public void setLeagueStandings(Set<LeagueStanding> leagueStandings) {
        if (this.leagueStandings != null) {
            this.leagueStandings.forEach(i -> i.setLeague(null));
        }
        if (leagueStandings != null) {
            leagueStandings.forEach(i -> i.setLeague(this));
        }
        this.leagueStandings = leagueStandings;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof League)) {
            return false;
        }
        return id != null && id.equals(((League) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "League{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
