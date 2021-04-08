package pl.mjaskola.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link pl.mjaskola.app.domain.League} entity.
 */
public class LeagueDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LeagueDTO)) {
            return false;
        }

        LeagueDTO leagueDTO = (LeagueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, leagueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeagueDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
