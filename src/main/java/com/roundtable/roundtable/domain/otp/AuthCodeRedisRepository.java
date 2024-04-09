package com.roundtable.roundtable.domain.otp;

import org.springframework.data.repository.CrudRepository;

public interface AuthCodeRedisRepository extends CrudRepository<AuthCode, String> {

}
