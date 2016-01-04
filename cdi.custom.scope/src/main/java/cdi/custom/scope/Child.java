package cdi.custom.scope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Child {

	@Id
	@SequenceGenerator(sequenceName = "woman_seq", allocationSize = 1, name = "woman_generator")
	@GeneratedValue(generator = "woman_generator", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Integer id;
	
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}

