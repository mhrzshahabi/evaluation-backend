package com.nicico.evaluation.common;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;



@RestController
@RequestMapping("/api/permission")
public class UserPermissionController {

    @Autowired
    PermissionService permissionService;


    @ApiOperation(value = "Get All Users' Permission")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get authorities "),
            @ApiResponse(code = 401, message = "You are not authorized"),
            @ApiResponse(code = 409, message = "It is duplicate"),
            @ApiResponse(code = 500, message = "Server error")
    })
    @GetMapping("/")
    public  ResponseEntity<Set<String>> getAuthorities(){

        return ResponseEntity.ok(permissionService.getAuthorities());

    }

    }



