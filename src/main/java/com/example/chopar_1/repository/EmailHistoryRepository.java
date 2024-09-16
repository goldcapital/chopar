package com.example.chopar_1.repository;

import com.example.chopar_1.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface EmailHistoryRepository  extends CrudRepository<EmailHistoryEntity,String> {
    @Query("select count (s) from  EmailHistoryEntity  s where s.email=?1  and s.createdDate between ?2 and ?3")
    Long countSendEmail(String email, LocalDateTime from, LocalDateTime to);
   // Long countByEmailAndCreatDataBetween(String emil,LocalDateTime from,LocalDateTime to);
}
