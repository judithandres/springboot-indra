package com.formacionjava.apirest.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "clientes")
public class Cliente  implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String nombre, apellido, email;
		
	private int telefono;
	
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	
	private Date createAt;
	
	@PrePersist
	public void PrePersist() {
		if(createAt==null) {
			createAt = new Date();
		}
	}
	

	
	//GETTERS Y SETTERS

	public long getId() {return id;}
	public void setId(long id) {this.id = id;}
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	public String getApellido() {return apellido;}
	public void setApellido(String apellido) {this.apellido = apellido;}
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	public int getTelefono() {return telefono;}
	public void setTelefono(int telefono) {this.telefono = telefono;}
	public Date getCreateAt() {return createAt;}
	public void setCreateAt(Date createAt) {this.createAt = createAt;}







	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
