package com.dprol.social.repository;

import com.dprol.social.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM country WHERE country_name :country")
    Country findByCountry(String country);
}
