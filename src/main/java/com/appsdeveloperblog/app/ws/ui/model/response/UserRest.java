package com.appsdeveloperblog.app.ws.ui.model.response;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Data
public class UserRest {
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private List<AddressesRest> addresses;

	
}
