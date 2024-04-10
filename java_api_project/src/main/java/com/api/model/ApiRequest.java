package com.api.model;

public class ApiRequest {

	private String id;
	private String name;
	private String nic;
	private String type;

	public ApiRequest(String id, String name, String nic, String type) {
		this.id = id;
		this.name = name;
		this.nic = nic;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNic() {
		return nic;
	}

	public void setNic(String nic) {
		this.nic = nic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
