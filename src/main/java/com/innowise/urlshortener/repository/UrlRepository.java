package com.innowise.urlshortener.repository;

import com.innowise.urlshortener.entity.Url;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UrlRepository extends JpaRepository<Url, Long> {

  @Query(value = "SELECT NEXTVAL('url_seq')", nativeQuery = true)
  Long getNextSequenceValue();

  Optional<Url> findByOriginalUrl(String originalUrl);

  Optional<Url> findByShortCode(String shortCode);
}
