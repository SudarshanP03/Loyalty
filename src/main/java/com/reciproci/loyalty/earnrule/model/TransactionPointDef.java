package com.reciproci.loyalty.earnrule.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"markType","noOfRewards"})
public class TransactionPointDef {
	private String markupType;
	private Integer noOfRewards;
	public String getMarkupType() {
		return markupType;
	}
	public void setMarkupType(String markupType) {
		this.markupType = markupType;
	}
	public Integer getNoOfRewards() {
		return noOfRewards;
	}
	public void setNoOfRewards(Integer noOfRewards) {
		this.noOfRewards = noOfRewards;
	}
	
}
