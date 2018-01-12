package com.program2.table;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="holidays", schema="mtr3")
public class Holiday {
	@Id
	public int id;
	public String name;
	public String Date;
}
