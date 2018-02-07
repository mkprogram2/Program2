package com.program2.table;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="behaviors", schema="mtr3")
public class Behavior {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	public Timestamp checkin;
	public Timestamp checkout;
	@Column(name = "person_id")
	public String personid;
}
