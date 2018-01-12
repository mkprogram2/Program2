package com.program2.table;

import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Table(name="shifts", schema="mtr3")
public class Shift {
	@Id
	public int id;
	@Column (name = "work_start")
	public Time workstart;
	@Column(name = "work_end") 
	public Time workend;
}
