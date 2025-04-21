package myapp.Model;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
//import javax.validation.constraints.NotNull;

//import lombok.Data;

@Entity
@Table(name="reverse")
//@Data
public class StringEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

//	@NotNull
	@Column
	private String data;

//	@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "posted_at")
    private Date postedAt = new Date();


	public void setData(String s) {
		data = s;
	}
	public String getData() {
		return data;
	}

	/* No-args constructor, setters, and getters are automatically generated thanks to @Data */
}
