package com.demo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * The department
 */
@Entity
public class Department extends AbstractIdentifiableEntity
{
    // @Id
    // @GeneratedValue
    // @Column( name = "id", columnDefinition = "uuid", updatable = false,
    // nullable = false )
    // private UUID id;

    /**
     * The name of department
     */
    @NotNull
    @Size( min = 3, max = 50 )
    @JsonView( Views.Default.class )
    private String name;

    /**
     * the users belong to this department
     */
    @JsonView( Views.Dept.class )
    @OneToMany( cascade = CascadeType.ALL, mappedBy = "department", orphanRemoval = true )
    List<UserInfo> users;

    // for JPA
    public Department()
    {
        users = new ArrayList<UserInfo>();
    }

    /**
     * Get the name of this <code>Department</code>
     * 
     * @return the name of this <code>Department</code>
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of this <code>Department</code>
     * 
     * @param name
     *            the name of <code>Department</code>
     */
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * Add new user to this <code>Department</code>
     * 
     * @param user
     *            the user to add
     */
    public void addUser( final UserInfo user )
    {
        user.setDepartment( this );
        users.add( user );
    }

    /**
     * Get all the <code>UserInfo</code>s belongs to this
     * <code>Department</code>.
     * 
     * @return all the <code>UserInfo</code>s
     */
    public List<UserInfo> getUsers()
    {
        return Collections.unmodifiableList( users );
    }

    @Override
    public String toString()
    {
        return "{DepartmentName: " + name + "}";
    }
}
