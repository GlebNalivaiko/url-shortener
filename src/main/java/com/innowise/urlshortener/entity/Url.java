package com.innowise.urlshortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "urls")
public class Url {

  @Id
  @SequenceGenerator(name = "url_seq", sequenceName = "url_seq", allocationSize = 1)
  private Long id;

  @Column(name = "original_url", length = 2048, unique = true, nullable = false)
  private String originalUrl;

  @Column(name = "short_code", length = 11, unique = true, nullable = false)
  private String shortCode;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Url url)) {
      return false;
    }

    return Objects.equals(originalUrl, url.originalUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(originalUrl);
  }
}
