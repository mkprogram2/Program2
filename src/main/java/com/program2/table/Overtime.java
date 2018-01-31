package com.program2.table;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@TypeDef(name="interval", typeClass = Interval.class)
@Entity
@Table(name="overtimes", schema="mtr3")
public class Overtime {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	public Date date;
	@Column(name = "person_id")
	public String personid;
	public String information;
	@Type(type = "interval") public int duration;
	public int status;
}
