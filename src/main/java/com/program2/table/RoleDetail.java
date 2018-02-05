package com.program2.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roles_details", schema="mtr3")
public class RoleDetail {
	@Id
	@Column(name = "role_id")
	public int roleid;
	@Column(name = "max_salary")
	public double maxsalary;
	@Column(name = "min_salary")
	public double minsalary;
}
