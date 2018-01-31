package com.program2.table;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="persons_details", schema="mtr3")
public class PersonDetail {
	@Id
	@Column(name = "person_id")
	public String personid;
	@Column(name = "assign_work")
	public Date assignwork;
	@ManyToOne
	@JoinColumn(name = "shift_id", referencedColumnName = "id")
	public Shift Shift = new Shift();
}
