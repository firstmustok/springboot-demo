package com.demo.model.dm;

import java.util.List;

import com.demo.model.Department;

public interface DepartmentRepository extends AbstractIdentifiableRepository<Department>
{
    List<Department> findDepartmentByName( final String name );
}
