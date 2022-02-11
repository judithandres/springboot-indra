package com.formacionjava.apirest.entity;
import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="regiones")
public class Region implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	
	private String nombre;
	
	
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}






	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
