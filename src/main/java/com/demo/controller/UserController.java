package com.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.demo.model.UserInfo;
import com.demo.model.Views;
import com.demo.model.dm.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api( value = "/v2/user" )
@RequestMapping( value = "/v2/user", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE } )
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @RequestMapping( method = RequestMethod.GET )
    @ApiOperation( value = "List all the users", notes = "return all the users",
            response = UserInfo.class )

    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "get all the list user" ),
    } )
    @JsonView( Views.User.class )
    private ResponseEntity<?> getUsers()
    {
        List<UserInfo> users = new ArrayList<UserInfo>();

        userRepository.findAll().forEach( ( it ) ->
        {
            users.add( it );
        } );

        return new ResponseEntity<>( users, HttpStatus.OK );
    }

    @RequestMapping( method = RequestMethod.POST )
    @ApiOperation( value = "Create new user with the spcefied detail",
            notes = "return the new user info", response = UserInfo.class )

    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "success" ),
            @ApiResponse( code = 400, message = "User exists or parameters error" ),
    } )
    private ResponseEntity<?> createUser(
                                          @Valid @RequestBody UserInfo user,

                                          final Errors errors )
    {
        // if the parameters provided are not valid return bad request
        if ( errors.hasErrors() )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        List<UserInfo> users = userRepository.findUserByName( user.getName() );
        if ( !users.isEmpty() )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        user = userRepository.save( user );

        return new ResponseEntity<>( user, HttpStatus.CREATED );
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.DELETE )
    private ResponseEntity<?> deleteUser( @PathVariable( "id" ) final UUID id )
    {
        userRepository.delete( id );
        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.PUT )
    private ResponseEntity<?> updateUser( @Valid @RequestBody UserInfo user,
                                          @PathVariable( "id" ) final UUID id )
    {
        user.setId( id );
        userRepository.save( user );
        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }
}
