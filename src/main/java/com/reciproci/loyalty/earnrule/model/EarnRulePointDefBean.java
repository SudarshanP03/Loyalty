package com.reciproci.loyalty.earnrule.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder({"tierId","tierName"})
public class EarnRulePointDefBean {
	private Long tierId;
	private String tierName;
	private List<TransactionPointDef> transactionPointDefs;
	public Long getTierId() {
		return tierId;
	}
	public void setTierId(Long tierId) {
		this.tierId = tierId;
	}
	public String getTierName() {
		return tierName;
	}
	public void setTierName(String tierName) {
		this.tierName = tierName;
	}
	public List<TransactionPointDef> getTransactionPointDefs() {
		return transactionPointDefs;
	}
	public void setTransactionPointDefs(List<TransactionPointDef> transactionPointDefs) {
		this.transactionPointDefs = transactionPointDefs;
	}
	
}
