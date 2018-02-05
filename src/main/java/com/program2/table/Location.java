package com.program2.table;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name="locations", schema="public")
public class Location {
	@Id
	public int id;
	public double x;
	public double y;
	public String name;
	public String description;
}