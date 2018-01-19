package com.program2.table;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="persons", schema="mtr3")
public class Person 
{
	@Id
	public String id;
	public String name;
	public String password;
	public double salary;
	@Column(name = "assign_work")
	public Date assignwork;
	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	public Role Role = new Role();
	@ManyToOne
	@JoinColumn(name = "shift_id", referencedColumnName = "id")
	public Shift Shift = new Shift();
}
