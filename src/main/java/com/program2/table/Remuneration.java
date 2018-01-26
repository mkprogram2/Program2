package com.program2.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="remunerations", schema="mtr3")
public class Remuneration {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;
	public Double salary;
	public Double trans;
	public Double meal;
	public Double communication;
	public Double diligent;
	public Double health;
	public Double overtime;
	public Double pension;
	public Double commision;
	@Column(name = "min_salary")
	public Double minsalary;
	@Column(name = "min_trans")
	public Double mintrans;
	@Column(name = "min_meal")
	public Double minmeal;
	@Column(name = "min_diligent")
	public Double mindiligent;
	@Column(name = "net_salary")
	public Double netsalary;
	public Double deduction;
	public Double income;
	public int month;
	public int year;
	@ManyToOne
	@JoinColumn(name = "person_id", referencedColumnName = "id")
	public Person person;
}
