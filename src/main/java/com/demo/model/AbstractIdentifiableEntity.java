package com.demo.model;

import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.proxy.HibernateProxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Base entity class that uses a UUID as the "id". It provides the id field as well as default
 * hashCode and equals methods based on the id alone.
 */
@MappedSuperclass
@Access( AccessType.FIELD )
@JsonIgnoreProperties( { "hibernateLazyInitializer", "handler" } )
public abstract class AbstractIdentifiableEntity
{
    /**
     * Synthetic id as primary key
     */
    @Id
    @GeneratedValue    
    @Column( name = "id", columnDefinition = "uuid", updatable = false, nullable = false )    
    @Access( AccessType.PROPERTY )
    private UUID        id;

    
    /**
     * Do nothing - hides the default constructor.
     */
    protected AbstractIdentifiableEntity()
    {
        super();
    }
    
    /**
     * 
     * @return the uuid of this entity
     */
    public final UUID getId()
    {
        if ( this instanceof HibernateProxy )
        {
            return (UUID)( (HibernateProxy)this ).getHibernateLazyInitializer().getIdentifier();
        }
        else
        {
            return id;
        }
    }

    public void setId( UUID id )
    {
        this.id = id;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        UUID    tmp = getId();
        
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( tmp == null ) ? 0 : tmp.hashCode() );
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null )
        {
            return false;
        }

        //  This relies on UUID's being globally unique, i.e. not just within a class.
        if ( !AbstractIdentifiableEntity.class.isAssignableFrom( obj.getClass() ) )
        {
            return false;
        }
        final AbstractIdentifiableEntity other = (AbstractIdentifiableEntity)obj;
        
        if ( getId() == null )
        {
            if ( other.getId() != null )
            {
                return false;
            }
        }
        else if ( !getId().equals( other.getId() ) )
        {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return toStringForClass( getClass() );
    }

    private String toStringForClass( final Class<? extends AbstractIdentifiableEntity> cls )
    {
        return cls.getSimpleName() + " [id=" + getId() + "]";
    }
    
}
