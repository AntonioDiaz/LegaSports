package com.adiaz.munisports.entities;

/**
 * Created by toni on 27/04/2017.
 */

public class CompetitionEntity {

	private String name;
	private String sportName;
	private String categoryName;
	// TODO: 31/08/2017 serverId should be Long.
	private String serverId;
	private Long lastUpdateServer;
	private Long lastUpdateApp;
	private Long lastNotification;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getLastUpdateServer() {
		return lastUpdateServer;
	}

	public void setLastUpdateServer(Long lastUpdateServer) {
		this.lastUpdateServer = lastUpdateServer;
	}

	public Long getLastUpdateApp() {
		return lastUpdateApp;
	}

	public void setLastUpdateApp(Long lastUpdateApp) {
		this.lastUpdateApp = lastUpdateApp;
	}

	public Long getLastNotification() {
		return lastNotification;
	}

	public void setLastNotification(Long lastNotification) {
		this.lastNotification = lastNotification;
	}
}
