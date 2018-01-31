package com.program2.table;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="holidays", schema="mtr3")
public class Holiday {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	public String name;
	public Date date;
}
