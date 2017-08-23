package com.adiaz.munisports.entities;

/**
 * Created by toni on 23/08/2017.
 */

public class CourtEntity {
	private String courtName;
	private String centerName;
	private String courtFullName;
	private String centerAddress;

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public String getCenterName() {
		return centerName;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public String getCenterAddress() {
		return centerAddress;
	}

	public void setCenterAddress(String centerAddress) {
		this.centerAddress = centerAddress;
	}

	public String getCourtFullName() {
		return courtFullName;
	}

	public void setCourtFullName(String courtFullName) {
		this.courtFullName = courtFullName;
	}
}
