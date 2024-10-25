package com.example.chopar_1.repository;

import com.example.chopar_1.entity.ProfileEntity;
import com.example.chopar_1.entity.SmsHistoryEntity;
import com.example.chopar_1.enums.SmsStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsRepository extends CrudRepository<SmsHistoryEntity, String> {
    @Query("select  count (s) from SmsHistoryEntity s where s.phone=?1 and s.createdDate between ?2 and  ?3")
    Long countSendSms(String phone, LocalDateTime from, LocalDateTime to);

    @Query("select  s from SmsHistoryEntity s where s.phone=?1 and s.createdDate between  ?2 and ?3")
    SmsHistoryEntity gedCodeByPhone(String phone, LocalDateTime from, LocalDateTime to);

    @Transactional
    @Modifying
    @Query("update SmsHistoryEntity set status =status where phone =phone")
    void updateByStatusAndPhone(@Param("status") SmsStatus status, @Param("phone") String phone);
}
