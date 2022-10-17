package com.appsdeveloperblog.app.ws.shared.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
//Author Nasim_Sarwar
@Getter
@Setter
public class UserDto implements Serializable{

	private static final long serialVersionUID = 6835192601898364280L;
	private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private String status;
    private List<AddressDTO> addresses;
   // private Collection<String> roles;
    
	
    
}
