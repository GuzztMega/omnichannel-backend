package br.com.guzzmega.omnichannel.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CHANNEL")
public class Channel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING) @NotNull
	private ChannelType channelType;
	@ManyToMany
	@JoinTable(
		name= "CHANNEL_CUSTOMER",
		joinColumns = @JoinColumn(name = "channel_id"),
		inverseJoinColumns = @JoinColumn(name = "customer_id")
	)
	private List<Customer> customerList = new ArrayList<>();

	public Channel(){
	}

	public Channel(String name, String email, String phoneNumber, ChannelType channelType) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.channelType = channelType;
	}

	@NotNull
	private String name;
	private String email;
	private String phoneNumber;

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (!(o instanceof Channel channel)) return false;
		return channelType == channel.channelType
				&& Objects.equals(name, channel.name)
				&& Objects.equals(email, channel.email)
				&& Objects.equals(phoneNumber, channel.phoneNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(channelType, name, email, phoneNumber);
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

	public ChannelType getChannelType() {
		return channelType;
	}

	public void setChannelType(ChannelType channelType) {
		this.channelType = channelType;
	}

	public List<Customer> getCustomerList() {
		return this.customerList;
	}

	public void setCustomerList(final List<Customer> customerList) {
		this.customerList = customerList;
	}
}