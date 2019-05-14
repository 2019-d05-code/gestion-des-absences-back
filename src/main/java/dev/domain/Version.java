package dev.domain;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private ZonedDateTime dateTimeCreated;
    private String gitVersion;

    public Version() {
    }

    public Version(String gitVersion) {
        this.gitVersion = gitVersion;
    }

    @PrePersist
    public void createDefaultDateTimeCreated() {
        this.dateTimeCreated = ZonedDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ZonedDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(ZonedDateTime dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public String getGitVersion() {
        return gitVersion;
    }

    public void setGitVersion(String gitVersion) {
        this.gitVersion = gitVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version that = (Version) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dateTimeCreated, that.dateTimeCreated) &&
                Objects.equals(gitVersion, that.gitVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTimeCreated, gitVersion);
    }
}
