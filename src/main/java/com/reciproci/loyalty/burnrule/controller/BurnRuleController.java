package com.reciproci.loyalty.burnrule.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reciproci.loyalty.burnrule.model.CreateBurnRuleBean;
import com.reciproci.loyalty.burnrule.model.ViewBean;
import com.reciproci.loyalty.burnrule.model.ViewProgramBean;
import com.reciproci.loyalty.burnrule.service.BurnRuleService;
import com.reciproci.loyalty.program.model.CommonUtil;

@RestController
@RequestMapping("api/rpa/burn/rule")
public class BurnRuleController {

	private static final Logger logger=LogManager.getLogger(BurnRuleController.class);
	@Autowired
	private BurnRuleService burnRuleService;

	@PostMapping("/v1/save")
	public ResponseEntity<Object> create(@RequestBody CreateBurnRuleBean createBean, BindingResult bindingResult) {
		// Validate request body
		if (createBean == null) {
			logger.warn("create called with null request body");
			return new ResponseEntity<Object>(CommonUtil.getInternalServerError(), HttpStatus.BAD_REQUEST);
		}

		if (bindingResult != null && bindingResult.hasErrors()) {
			logger.warn("Validation failed for CreateBurnRuleBean: {}", bindingResult.getAllErrors());
			return new ResponseEntity<Object>(CommonUtil.getInternalServerError(), HttpStatus.BAD_REQUEST);
		}

		try{
			burnRuleService.saveBurnRule(createBean);
			return new ResponseEntity<Object>(CommonUtil.getSuccessMessage(), HttpStatus.CREATED);
		}catch(Exception e) {
			logger.error("Error while saving burn rule", e);
			return new ResponseEntity<Object>(CommonUtil.getInternalServerError(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/v1/update")
	public ResponseEntity<Object> update(@RequestBody CreateBurnRuleBean createBean) {
		burnRuleService.update(createBean);
		return new ResponseEntity<Object>(CommonUtil.getSuccessMessage(), HttpStatus.OK);
	}

	@PostMapping("/v1/view")
	public ResponseEntity<ViewBean> view(@RequestBody Map<String, Long> requestBody) {
		ViewBean viewBean = burnRuleService.viewBurnRule(requestBody);
		return new ResponseEntity<>(viewBean, HttpStatus.OK);
	}

	@PostMapping("/v1/program/view")
	public ResponseEntity<ViewProgramBean> viewProgram(@RequestBody Map<String, Object> requestBody) {
		ViewProgramBean details = burnRuleService.getProgramDetails(requestBody);
		return new ResponseEntity<>(details, HttpStatus.OK);
	}
}