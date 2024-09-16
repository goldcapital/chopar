package com.example.chopar_1.repository;

import com.example.chopar_1.entity.ProfileEntity;
import com.example.chopar_1.entity.SmsHistoryEntity;
import com.example.chopar_1.enums.ProfileStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity ,String> {
    Optional<ProfileEntity> findByEmail(String email);
    @Transactional
    @Modifying
   @Query("delete from ProfileEntity where email=?1")
    void deleteByEmail(String email);
   @Transactional
   @Modifying
   @Query("update ProfileEntity set status=?1 where email=?2")
    void update(ProfileStatus active, String email);

    @Query("from ProfileEntity where email=?1")
    Optional<ProfileEntity> getId(String id);

    @Query(" from ProfileEntity s where s.phone=?1 and s.createdDate between ?2 and  ?3")
    Optional <ProfileEntity> getSmsBatwenCreatedDate(String phone, LocalDateTime from, LocalDateTime to);
    @Query("from  ProfileEntity  where email=?1 or phone=?2")
    Optional<ProfileEntity> findByEmailOrPhone(String email, String phone);
    @Query("from ProfileEntity  where phone=?1")
    Optional<ProfileEntity> findByPhone(String phone);
    @Transactional
    @Modifying
    @Query("update ProfileEntity set status=?2 where phone=?1 ")
    void updateByPhone(String phone, ProfileStatus profileStatus);
}
