package com.demo.model.dm;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.demo.model.AbstractIdentifiableEntity;

@NoRepositoryBean
public interface AbstractIdentifiableRepository<T extends AbstractIdentifiableEntity>
        extends CrudRepository<T, UUID>
{

}
