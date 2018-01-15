package com.adiaz.deportelocal.sync.retrofit.entities.town;

/**
 * Created by toni on 02/08/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TownRestEntity {

	@SerializedName("id")
	@Expose
	private Long id;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("active")
	@Expose
	private Boolean active;

	@SerializedName("fcmTopic")
	@Expose
	private String fcmTopic;

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getFcmTopic() {
		return fcmTopic;
	}

	public void setFcmTopic(String fcmTopic) {
		this.fcmTopic = fcmTopic;
	}
}