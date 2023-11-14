package br.com.guzzmega.omnichannel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CUSTOMER")
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonIgnore
	@ManyToMany(mappedBy="customerList")
	private List<Channel> channelList = new ArrayList<>();
	@NotNull
	private String name;
	private String email;
	private String phoneNumber;

	public Customer(){
	}

	public Customer(String name, String email, String phoneNumber) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof Customer customer)) return false;
		return Objects.equals(name, customer.name)
				&& Objects.equals(email, customer.email)
				&& Objects.equals(phoneNumber, customer.phoneNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, email, phoneNumber);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<Channel> getChannelList() {
		return this.channelList;
	}

	public void setChannelList(final List<Channel> channelList) {
		this.channelList = channelList;
	}
}