package com.api.v1;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class Moderator {

	private int id;
	
	@NotNull @NotEmpty @Size(min=2, max=25)
	private String name;
	
	@NotNull @NotEmpty @Email
	private String email;
	
	@NotNull @NotEmpty @Size(min=2)
	private String password;
	
	private String createdAt;
}
