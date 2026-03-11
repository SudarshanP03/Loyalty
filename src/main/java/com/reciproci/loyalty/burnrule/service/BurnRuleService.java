package com.reciproci.loyalty.burnrule.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reciproci.loyalty.burnrule.entity.BurnRule;
import com.reciproci.loyalty.burnrule.entity.BurnRuleLocale;
import com.reciproci.loyalty.burnrule.entity.RewardValue;
import com.reciproci.loyalty.burnrule.exception.BurnRuleAlreadyExistsException;
import com.reciproci.loyalty.burnrule.model.CreateBurnRuleBean;
import com.reciproci.loyalty.burnrule.model.RewardValueBean;
import com.reciproci.loyalty.burnrule.model.RuleLocalBean;
import com.reciproci.loyalty.burnrule.model.ViewBean;
import com.reciproci.loyalty.burnrule.model.ViewProgramBean;
import com.reciproci.loyalty.burnrule.repository.BurnRuleRepository;
import com.reciproci.loyalty.earnrule.entity.Language;
import com.reciproci.loyalty.earnrule.entity.Store;
import com.reciproci.loyalty.earnrule.repository.LanguageRepo;
import com.reciproci.loyalty.earnrule.repository.StoreRepo;
import com.reciproci.loyalty.program.entity.Program;
import com.reciproci.loyalty.program.repository.ProgramRepo;

@Service
public class BurnRuleService {

	private static final Logger logger = LogManager.getLogger(BurnRuleService.class);

	@Autowired
	private ProgramRepo programRepo;

	@Autowired
	private BurnRuleRepository burnRuleRepository;

	@Autowired
	private StoreRepo storeRepo;

	@Autowired
	private LanguageRepo laguageRepo;

	public void saveBurnRule(CreateBurnRuleBean bean) {

		logger.info("Attempting to save Burn Rule for program Id ;" + bean.getProgramId());

		if (burnRuleRepository.existsByProgramId(bean.getProgramId())) {
			logger.info("For "+bean.getProgramId()+" program Burn Rule already exist");
			throw new BurnRuleAlreadyExistsException("Validation Failed: A Burn Rule already exists for Program ID "
					+ bean.getProgramId() + ". You can only create one rule per program.");
		}

		Program program = programRepo.findById(bean.getProgramId())
				.orElseThrow(() -> new RuntimeException("Program not found with ID: " + bean.getProgramId()));

		BurnRule burnRule = new BurnRule();

		burnRule.setProgram(program);
		burnRule.setProgramId(bean.getProgramId());

		burnRule.setRewardType(bean.getRewardType());
		burnRule.setRewardQty(bean.getRewardQty());
		burnRule.setRewardImagePath(bean.getRewardImagePath());
		burnRule.setRuleImagepath(bean.getRuleImagepath());
		burnRule.setStartDate(bean.getStartDate());
		burnRule.setEndDate(bean.getEndDate());
		burnRule.setDisplayHomeScreen(bean.isDisplayHomeScreen());
		burnRule.setExclusive(bean.isExclusive());
		burnRule.setSkuFilePath(bean.getSkuFilePath());
		burnRule.setMinimumPoints(bean.getMinimumPoints());
		burnRule.setExcludeStore(bean.isExcludeStore());
		burnRule.setExcludeSku(bean.isExcludeSku());
		burnRule.setFreeProductType(bean.getFreeProductType());

		List<RewardValue> rewardValues = bean.getRewardValues();
		List<RewardValue> list = new ArrayList<>();
		for (RewardValue reward : rewardValues) {
			reward.setBurnRule(burnRule);
			list.add(reward);
		}
		burnRule.setRewardValues(rewardValues);

		try {
			if (bean.getStore() != null && !bean.getStore().isEmpty()) {
				List<Store> allById = storeRepo.findAllById(bean.getStore());
				burnRule.setStore(allById);
			}
		} catch (Exception e) {
			logger.error("Error in BurnRuleService at getStore", e);
		}

		Language lang = laguageRepo.findById(bean.getLanguageId()).orElseThrow(() -> {
			logger.error("Exception in BurnRuleService at finding Language by id");
			return new RuntimeException("Language not found with ID: " + bean.getLanguageId());

		});

		if (burnRule.getLanguage() == null) {
			burnRule.setLanguage(new ArrayList<>());
		}
		burnRule.getLanguage().add(lang);

		if (bean.getRuleLocales() != null) {
			List<BurnRuleLocale> locales = bean.getRuleLocales().stream().map(localeEntity -> {
				localeEntity.setBurnRule(burnRule);
				return localeEntity;
			}).collect(Collectors.toList());
			burnRule.setRuleLocaleList(locales);
		}
		try {
			burnRuleRepository.save(burnRule);
			logger.info("Successfully saved Burn Rule for program Id :" + bean.getProgramId());
		} catch (Exception e) {
			logger.error("Failed to save Burn Rule for Program Id :" + bean.getProgramId());
		}
	}

	public void update(CreateBurnRuleBean bean) {
		logger.info("Attempting to update BurnRule for Program ID: {}", bean.getProgramId());
		BurnRule existingRule = burnRuleRepository.findById(bean.getProgramId())
				.orElseThrow(() -> {
					logger.error("Update failed: No Burn Rule found for Program ID: {}", bean.getProgramId());
					return new RuntimeException("No Burn Rule found for Program ID: " + bean.getProgramId());
				});
					

		existingRule.setRewardType(bean.getRewardType());
		existingRule.setRewardQty(bean.getRewardQty());
		existingRule.setRewardImagePath(bean.getRewardImagePath());
		existingRule.setRuleImagepath(bean.getRuleImagepath());
		existingRule.setStartDate(bean.getStartDate());
		existingRule.setEndDate(bean.getEndDate());
		existingRule.setDisplayHomeScreen(bean.isDisplayHomeScreen());
		existingRule.setExclusive(bean.isExclusive());
		existingRule.setSkuFilePath(bean.getSkuFilePath());
		existingRule.setMinimumPoints(bean.getMinimumPoints());
		existingRule.setExcludeStore(bean.isExcludeStore());
		existingRule.setExcludeSku(bean.isExcludeSku());
		existingRule.setFreeProductType(bean.getFreeProductType());

		if (bean.getRewardValues() != null) {
			logger.debug("Updating RewardValues for Program ID: {}. Count: {}", bean.getProgramId(), bean.getRewardValues().size());
			Map<Long, RewardValue> reward = existingRule.getRewardValues().stream()
					.collect(Collectors.toMap(RewardValue::getTierId, t -> t));

			List<RewardValue> updatedRewardValues = new ArrayList<>();
			existingRule.getRewardValues().clear();

			for (RewardValue rewardValue : bean.getRewardValues()) {
				RewardValue foundValue = reward.get(rewardValue.getTierId());
				if (foundValue == null) {
					logger.debug("Adding new RewardValue for Tier ID: {}", rewardValue.getTierId());
					foundValue = new RewardValue();
					foundValue.setBurnRule(existingRule);
				}
				foundValue.setRegionId(rewardValue.getRegionId());
				foundValue.setTierId(rewardValue.getTierId());
				foundValue.setRewardValue(rewardValue.getRewardValue());
				updatedRewardValues.add(foundValue);
			}

			existingRule.getRewardValues().clear();
			existingRule.getRewardValues().addAll(updatedRewardValues);
		}

		if (bean.getStore() != null) {
			logger.debug("Updating Stores for Program ID: {}. Store IDs: {}", bean.getProgramId(), bean.getStore());
			List<Store> updatedStores = storeRepo.findAllById(bean.getStore());
			existingRule.setStore(updatedStores);
		}

		Language lang = laguageRepo.findById(bean.getLanguageId())
				.orElseThrow(() ->{
					logger.error("Update failed: Language ID {} not found", bean.getLanguageId());
					return new RuntimeException("Language not found");
				});

		existingRule.getLanguage().clear();
		existingRule.getLanguage().add(lang);

		if (bean.getRuleLocales() != null) {
			logger.debug("Updating Locales for Program ID: {}. Count: {}", bean.getProgramId(), bean.getRuleLocales().size());
			Map<Long, BurnRuleLocale> existingMap = existingRule.getRuleLocaleList().stream()
					.collect(Collectors.toMap(BurnRuleLocale::getId, t -> t));

			List<BurnRuleLocale> listOfLocales = new ArrayList<>();
			existingRule.getRuleLocaleList().clear();
			for (BurnRuleLocale local : bean.getRuleLocales()) {
				BurnRuleLocale locale = existingMap.get(local.getId());
				if (locale == null) {
					logger.debug("Adding new Locale entry for Program ID: {}", bean.getProgramId());
					locale = new BurnRuleLocale();
					locale.setBurnRule(existingRule);
				}
				locale.setDescription(local.getDescription());
				locale.setRuleName(local.getRuleName());
				locale.setTermsAndConditions(local.getTermsAndConditions());
				listOfLocales.add(locale);
			}

			existingRule.getRuleLocaleList().clear();
			existingRule.getRuleLocaleList().addAll(listOfLocales);
		}

		burnRuleRepository.save(existingRule);
		logger.info("Successfully updated BurnRule for Program ID: {}", bean.getProgramId());
	}

	public ViewBean viewBurnRule(Map<String, Long> request) {

		Long burnRuleId = request.get("burnRuleId");

		logger.info("Fetching BurnRule for Id :" + burnRuleId);

		try {
			BurnRule burnRule = burnRuleRepository.findById(burnRuleId)
					.orElseThrow(() -> new RuntimeException("Burn Rule not found for ID: " + burnRuleId));
			if (burnRule != null) {
				ViewBean viewBean = new ViewBean();
				viewBean.setBurnRuleId(burnRule.getId());
				viewBean.setProgramId(burnRule.getProgramId());
				viewBean.setRuleImagePath(burnRule.getRuleImagepath());
				viewBean.setRewardType(burnRule.getRewardType());
				viewBean.setRewardQty(burnRule.getRewardQty());
				viewBean.setRewardImagePath(burnRule.getRewardImagePath());
				viewBean.setSkuFilePath(burnRule.getSkuFilePath());
				viewBean.setMinimumPoints(burnRule.getMinimumPoints());
				viewBean.setExcludeSku(burnRule.isExcludeSku());
				viewBean.setExcludeStore(burnRule.isExcludeStore());
				viewBean.setDisplayHomeScreen(burnRule.isDisplayHomeScreen());
				viewBean.setExclusive(burnRule.isExclusive());
				List<RewardValueBean> arrayList = new ArrayList<>();
				for (RewardValue reward : burnRule.getRewardValues()) {
					RewardValueBean rewardValueBean = new RewardValueBean();
					rewardValueBean.setRegionId(reward.getRegionId());
					rewardValueBean.setTierid(reward.getTierId());
					rewardValueBean.setRewardValue(reward.getRewardValue());
					arrayList.add(rewardValueBean);
				}
				viewBean.setRewardValues(arrayList);

				if (burnRule.getStore() != null) {
					viewBean.setStoreList(burnRule.getStore());
				}

				if (burnRule.getRuleLocaleList() != null) {
					List<RuleLocalBean> localeBeans = burnRule.getRuleLocaleList().stream().map(locale -> {
						RuleLocalBean localBean = new RuleLocalBean();
						localBean.setRuleName(locale.getRuleName());
						localBean.setDescription(locale.getDescription());
						localBean.setTermsAndConditions(locale.getTermsAndConditions());
						return localBean;
					}).collect(Collectors.toList());
					viewBean.setRuleLocalList(localeBeans);
					logger.info("Fetching data for Id :" + burnRuleId + " is done");
					return viewBean;
				}
			} else {
				logger.error("Error in Burn Rule Service");
			}
		} catch (Exception e) {
			logger.error("Error in BurnRuleService at BurnRule empty/BurnRule Not found");
		}
		return null;
	}

	public ViewProgramBean getProgramDetails(Map<String, Object> request) {
		Object idObj = request.get("burnRuleId");
		Long burnRuleId = Long.valueOf(idObj.toString());
		logger.info("Fetching program details on burnRule Id :"+burnRuleId);
		try {
			BurnRule burnRule = burnRuleRepository.findById(burnRuleId)
					.orElseThrow(() ->  new RuntimeException("Burn Rule not found for ID: " + burnRuleId));
			Program program = programRepo.findById(burnRule.getProgramId()).orElseThrow();
			ViewProgramBean bean = new ViewProgramBean();
			bean.setBurnRuleId(burnRule.getId());
			bean.setRuleName(program.getProgramName());
			bean.setRewardType(burnRule.getRewardType());
			bean.setRedeemQty(burnRule.getRewardQty());
			bean.setStartDate(burnRule.getStartDate());
			bean.setEndDate(burnRule.getEndDate());

			bean.setRedeemType(burnRule.getRewardType());

			if (burnRule.getRuleLocaleList() != null && !burnRule.getRuleLocaleList().isEmpty()) {
				bean.setRuleName(burnRule.getRuleLocaleList().get(0).getRuleName());
			} else {
				bean.setRuleName("N/A");
			}
			logger.info("Fetching program details is done from BurnRuleService");
			return bean;
		} catch (Exception e) {
			logger.error("Error in BurnRule Service at get program details");
		}
		return null;
	}
}
