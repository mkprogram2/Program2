package com.program2.table;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.datetime.standard.DateTimeContext;
@TypeDef(name="interval", typeClass = Interval.class)
@Entity @Table(name="workhours", schema="mtr3") @IdClass(WorkhourId.class)
public class Workhour {
//	public Workhour(String id, Timestamp wa, Timestamp we, String wai, String wei, String wi)
//	{
//		personid = id;
//		workstart = wa;
//		workend = we;
//		workstartinterval = wai;
//		workendinterval = wei;
//		workinterval = wi;
//	}
//	
//	public Workhour(String id, Timestamp wa, long wai)
//	{
//		personid = id;
//		workstart = wa;
//		workstartinterval = Long.toString(wai);
//	}
//	

	@Id @Column(name = "person_id") public String personid;
	@Id @Column (name = "work_start",nullable = true) public Timestamp workstart;
	@Column(name = "work_end",nullable = true) public Timestamp workend;
	@Column(name = "work_start_interval",nullable = true) @Type(type = "interval") public int workstartinterval;
	@Column(name = "work_end_interval",nullable = true) @Type(type = "interval") public int workendinterval;
	@Column(name = "work_interval",nullable = true) @Type(type = "interval")public int workinterval;
	public int status;
}

class WorkhourId implements Serializable
{
	public String personid;
	public Timestamp workstart;
	
}


