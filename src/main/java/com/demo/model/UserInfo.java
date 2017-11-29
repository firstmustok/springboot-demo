package com.demo.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class UserInfo extends AbstractIdentifiableEntity
{
    // @Id
    // @GeneratedValue
    // @Column( name = "id", columnDefinition = "uuid", updatable = false,
    // nullable = false )
    // private UUID id;

    // @JsonView( Views.User.class )
    @ManyToOne( optional = true, fetch = FetchType.LAZY )
    private Department department;

    @NotNull
    @Size( min = 3, max = 50 )
    @JsonView( { Views.Default.class } )
    private String name;

    @JsonView( { Views.User.class } )
    private String password;

    public UserInfo()
    {
    }

    public Department getDepartment()
    {
        return department;
    }

    public void setDepartment( Department department )
    {
        this.department = department;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "{name: " + name + "password: " + password + "}";
    }
}
