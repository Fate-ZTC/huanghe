package com.parkbobo.model;

public class PatrolUserRegionShow extends PatrolUserRegion{

	private static final long serialVersionUID = 43927960441801177L;
	
	private String exceptionName;
	
	private String regionName;
	
	private Long patrolTime;

	public PatrolUserRegionShow(String username, String jobNum) {
		super(username, jobNum);
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Long getPatrolTime() {
		return patrolTime;
	}

	public void setPatrolTime(Long patrolTime) {
		this.patrolTime = patrolTime;
	}
	
	
}
