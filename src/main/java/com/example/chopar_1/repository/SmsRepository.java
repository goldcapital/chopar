package com.example.chopar_1.repository;

import com.example.chopar_1.entity.SmsHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsRepository extends CrudRepository<SmsHistoryEntity,String> {
    @Query("select  count (s) from SmsHistoryEntity s where s.phone=?1 and s.createdDate between ?2 and  ?3")
    Long countSendSms(String phone, LocalDateTime from, LocalDateTime to);
@Query("select  s.code from SmsHistoryEntity s where s.phone=?1 and s.createdDate between  ?2 and ?3")
    String gedCodeByPhone(String phone, LocalDateTime from, LocalDateTime to);
}
