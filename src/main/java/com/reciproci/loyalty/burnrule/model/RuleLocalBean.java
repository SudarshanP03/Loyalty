package com.reciproci.loyalty.burnrule.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"ruleName","description","termsAndConditions"})
public class RuleLocalBean {
	private String ruleName;
	private String description;
	private String termsAndConditions;

	public String getRuleName() {
		return ruleName;
	}

	public String getDescription() {
		return description;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}
}
