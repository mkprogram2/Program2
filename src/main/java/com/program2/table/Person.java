package com.program2.table;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="persons", schema="mtr3")
public class Person 
{
	@Id
	public String id;
	public String name;
	@Column(name = "app_password")
	public String apppassword;
	@Column(name = "birth_date")
	public Date birthdate;
	public String card;
	@Column(name = "e_mail")
	public String email;
	public char gender;
	public byte[] image;
	public String npwp;
	public String phone;
	
	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	public Role Role = new Role();
	@ManyToOne
	@JoinColumn(name = "place_id", referencedColumnName = "id")
	public Place Place = new Place();
	@OneToOne
	@JoinColumn(name = "id", referencedColumnName = "person_id")
	public PersonDetail PersonDetail = new PersonDetail();
}
