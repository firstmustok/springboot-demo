package com.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.UserInfo;

import io.swagger.annotations.Api;

/**
 * Simple user management RESTful API, which save the users in memory.
 */

// @Scope( "request" )
@RestController
@Api( value = "/user" )
@RequestMapping( value = "/user", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE } )
public class SimpleUserController
{
    private List<UserInfo> users = new ArrayList<UserInfo>();

    /**
     * List all the users
     * 
     * @return all the users.
     */
    @RequestMapping( method = RequestMethod.GET )
    private ResponseEntity<?> listUsers()
    {
        return new ResponseEntity<>( users, HttpStatus.OK );
    }

    /**
     * Create a new user with specified info.
     * 
     * @param user
     *            the new user info
     * @param errors
     *            validate errors if errors exist
     * @return 201 if create user successfully, 400 if any error in validation
     */
    @RequestMapping( method = RequestMethod.POST )
    private ResponseEntity<?> createUser(
                                          @Valid @RequestBody UserInfo user,

                                          final Errors errors )
    {
        // if the parameters provided are not valid return bad request
        if ( errors.hasErrors() )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        // save user
        user.setId( UUID.randomUUID() );
        users.add( user );

        return new ResponseEntity<>( user, HttpStatus.CREATED );
    }

    /**
     * Delete the specified user
     * 
     * @param id
     *            the user id which will be deleted
     * @return 201 if the user is deleted successfully, 404 if the user is not
     *         found
     */
    @RequestMapping( value = "/{id}", method = RequestMethod.DELETE )
    private ResponseEntity<?> deleteUser( @PathVariable( "id" ) final UUID id )
    {
        List<UserInfo> list = users.stream().filter( ( user ) -> user.getId().equals( id ) )
                .collect( Collectors.toList() );

        if ( list.isEmpty() )
        {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }

        // remove the user
        list.forEach( ( u ) ->
        {
            users.remove( u );
        } );

        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }

    /**
     * Update the user profile
     * 
     * @param user
     *            the user info
     * @param id
     *            the user id
     * @return 204 if the user is updated successfully, 404 if the user is not
     *         found
     */
    @RequestMapping( value = "/{id}", method = RequestMethod.PUT )
    private ResponseEntity<?> updateUser( @Valid @RequestBody UserInfo user,
                                          @PathVariable( "id" ) final UUID id )
    {
        UserInfo existUser = users.stream().filter( ( u ) -> u.getId().equals( id ) ).findFirst()
                .orElse( null );

        if ( existUser == null )
        {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }

        existUser.setName( user.getName() );
        existUser.setPassword( user.getPassword() );

        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }
}
