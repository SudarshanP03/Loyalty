package com.reciproci.loyalty.program.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.reciproci.loyalty.earnrule.entity.EarnRule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PROGRAM")
public class Program {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String status;
	
	private String programType;
	
	private String programName;
	
	private String programDescription;
	
	private String programImgPath;
	
	private String rewardType;
	
	private String startDate;
	
	private String endDate;
	
	private String startTime;
	
	private String endTime;
	
	private String pointExpiryIn;
	
	private Integer expiryDays;	
	
	private String expireDate;
	
	private String skuFilePath;
	
	private boolean isPerpetual;
	
	private LocalDateTime createdTime;
	
	private LocalDateTime modifiedTime;
	
	@OneToOne(mappedBy = "program", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Brand brand;
	
	@OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<EarnRule> earnRules;
	
	private String Approval_Status;
	
	private String publish;
	
	
	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getApproval_Status() {
		return Approval_Status;
	}

	public void setApproval_Status(String approval_Status) {
		Approval_Status = approval_Status;
	}
	
	public List<EarnRule> getEarnRules() {
		return earnRules;
	}

	public void setEarnRules(List<EarnRule> earnRules) {
		this.earnRules = earnRules;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProgramType() {
		return programType;
	}

	public void setProgramType(String programType) {
		this.programType = programType;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramDescription() {
		return programDescription;
	}

	public void setProgramDescription(String programDescription) {
		this.programDescription = programDescription;
	}

	public String getProgramImgPath() {
		return programImgPath;
	}

	public void setProgramImgPath(String programImgPath) {
		this.programImgPath = programImgPath;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPointExpiryIn() {
		return pointExpiryIn;
	}

	public void setPointExpiryIn(String pointExpiryIn) {
		this.pointExpiryIn = pointExpiryIn;
	}

	public Integer getExpiryDays() {
		return expiryDays;
	}

	public void setExpiryDays(Integer expiryDays) {
		this.expiryDays = expiryDays;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public String getSkuFilePath() {
		return skuFilePath;
	}

	public void setSkuFilePath(String skuFilePath) {
		this.skuFilePath = skuFilePath;
	}

	public boolean isPerpetual() {
		return isPerpetual;
	}

	public void setPerpetual(boolean isPerpetual) {
		this.isPerpetual = isPerpetual;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(LocalDateTime modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
