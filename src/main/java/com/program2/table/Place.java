package com.program2.table;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name="places", schema="mtr3")
public class Place {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public int id;
	public String name;
	public String description;
	public Date created;
	@Column(name = "place_type")
	public int placetype;
}
