package com.program2.table;

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
	public int role;
	public double salary;
	@ManyToOne
	@JoinColumn(name = "shift_id", referencedColumnName = "id")
	public Shift Shift;
}
