package com.example.chopar_1.repository;

import com.example.chopar_1.entity.ProfileEntity;
import com.example.chopar_1.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity ,String> {
    Optional<ProfileEntity> findByEmail(String email);
@Query("delete from ProfileEntity where email=?1")
    void deleteByEmail(String email);
@Transactional
@Modifying
@Query("update ProfileEntity set status=?1 where email=?1")
    void update(ProfileStatus active, String id);

@Query("from ProfileEntity where email=?1")
    Optional<ProfileEntity> getId(String id);
}
