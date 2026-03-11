package com.reciproci.loyalty.earnrule.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"tierId","spendAmount","markType"})
public class earnRuleTransactionAmountDefsBean {
	private Long tierId;
    private Double spendAmount;
    private String markupType;
    private Boolean isAllowMultiple;
	public Long getTierId() {
		return tierId;
	}
	public void setTierId(Long tierId) {
		this.tierId = tierId;
	}
	public Double getSpendAmount() {
		return spendAmount;
	}
	public void setSpendAmount(Double spendAmount) {
		this.spendAmount = spendAmount;
	}
	public String getMarkupType() {
		return markupType;
	}
	public void setMarkupType(String markupType) {
		this.markupType = markupType;
	}
	public Boolean getIsAllowMultiple() {
		return isAllowMultiple;
	}
	public void setIsAllowMultiple(Boolean isAllowMultiple) {
		this.isAllowMultiple = isAllowMultiple;
	}
    
    
}
