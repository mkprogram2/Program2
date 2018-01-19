package com.program2.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roles", schema="mtr3")
public class Role {

	@Id
	public int id;
	public String name;
	@Column(name = "max_salary")
	public double maxsalary;
	@Column(name = "min_salary")
	public double minsalary;
}
