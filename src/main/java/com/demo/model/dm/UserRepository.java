package com.demo.model.dm;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.demo.model.UserInfo;

public interface UserRepository extends CrudRepository<UserInfo, UUID>
{
    List<UserInfo> findUserByName( final String name );
}
