package com.program2.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="roles", schema="mtr3")
public class Role {
	@Id
	public int id;
	public String name;
	public byte[] permissions;
	@Column(name = "rights_level")
	public int rightslevel;
	@OneToOne
	@JoinColumn(name = "id", referencedColumnName = "role_id")
	public RoleDetail RoleDetail = new RoleDetail();
}
