package com.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.demo.model.Department;
import com.demo.model.UserInfo;
import com.demo.model.Views;
import com.demo.model.dm.DepartmentRepository;
import com.demo.model.dm.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api( value = "/dept" )
@RequestMapping( value = "/dept", produces = { MediaType.APPLICATION_JSON_UTF8_VALUE } )
public class DepartmentController
{
    @Autowired
    private DepartmentRepository deptRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager em;

    @RequestMapping( method = RequestMethod.GET )
    @ApiOperation( value = "List all the departments", notes = "return all the departments",
            response = Department.class )
    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "get all the departments" ),
    } )
    @JsonView( Views.Dept.class )
    private ResponseEntity<?> getDepartments()
    {
        List<Department> depts = new ArrayList<Department>();

        deptRepository.findAll().forEach( ( it ) ->
        {
            depts.add( it );
        } );

        return new ResponseEntity<>( depts, HttpStatus.OK );
    }

    /**
     * 
     * @param dept
     * @param errors
     * @return
     */
    @RequestMapping( method = RequestMethod.POST )
    @ApiOperation( value = "Create department with the spcefied detail", notes = "",
            response = Department.class )

    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "success" ),
            @ApiResponse( code = 400, message = "Department exists or parameters error" ),
    } )
    private ResponseEntity<?> createDept(
                                          @Valid @RequestBody Department dept,

                                          final Errors errors )
    {
        // if the parameters provided are not valid return bad request
        if ( errors.hasErrors() )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        List<Department> depts = deptRepository.findDepartmentByName( dept.getName() );
        if ( !depts.isEmpty() )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        dept = deptRepository.save( dept );

        return new ResponseEntity<>( dept, HttpStatus.CREATED );
    }

    /// DLETE, PUT
    // .....

    /**
     * 
     * @param deptId
     * @param userId
     * @return
     */
    @RequestMapping( value = "/{deptId}/user/{userId}", method = RequestMethod.PATCH )
    @ApiOperation( value = "Create department user with the spcefied detail", notes = "",
            response = Department.class )

    @ApiResponses( value = {
            @ApiResponse( code = 204, message = "success" ),
            @ApiResponse( code = 400, message = "Department/User is not exists" ),
    } )
    @Transactional
    ResponseEntity<?> addUser(
                               @PathVariable( "deptId" ) //
                               final UUID deptId,

                               @PathVariable( "userId" ) //
                               final UUID userId )
    {
        final Department dept = deptRepository.findOne( deptId );
        if ( dept == null )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        final UserInfo user = userRepository.findOne( userId );
        if ( user == null )
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
        }

        // em.lock( user, LockModeType.PESSIMISTIC_WRITE );

        // persist to db
        dept.addUser( user );
        deptRepository.save( dept );

        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }
}
