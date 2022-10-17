package com.appsdeveloperblog.app.ws.io.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "roles")
@Getter
@Setter
public class RoleEntity implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3626115357009129227L;
	@Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
}
