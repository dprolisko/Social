package com.dprol.social.repository.contact;

import com.dprol.social.entity.contact.ContactPreference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactPreferenceRepository extends CrudRepository<ContactPreference, Long> {

    Optional<ContactPreference> findByUser(String username);
}
