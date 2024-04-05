package com.roundtable.roundtable.entity.otp;

import org.springframework.data.repository.CrudRepository;

public interface AuthCodeRedisRepository extends CrudRepository<AuthCode, String> {

}
