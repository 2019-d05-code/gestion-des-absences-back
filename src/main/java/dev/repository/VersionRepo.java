package dev.repository;

import dev.domain.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepo extends JpaRepository<Version, Integer> {
}
