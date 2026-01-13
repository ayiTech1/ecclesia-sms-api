package com.ayitech.ecclesiasms.repository;

import com.ayitech.ecclesiasms.model.UserData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDataRepository extends CrudRepository<UserData, Long> {
    Optional<UserData> findByIdAndStatus(Long id, String status);
}
