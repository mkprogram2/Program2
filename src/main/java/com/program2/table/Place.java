package com.program2.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @Table(name="places", schema="mtr3")
public class Place {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public int id;
	public String name;
	public String description;
	@Column(name = "place_type")
	public int placetype;
	@ManyToOne
	@JoinColumn(name = "location_id", referencedColumnName = "id")
	public Location Location = new Location();
}
