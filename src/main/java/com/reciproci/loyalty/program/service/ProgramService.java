package com.reciproci.loyalty.program.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.reciproci.loyalty.program.entity.Brand;
import com.reciproci.loyalty.program.entity.Program;
import com.reciproci.loyalty.program.model.CreateProgramBean;
import com.reciproci.loyalty.program.model.SearchResponse;
import com.reciproci.loyalty.program.model.ViewBean;
import com.reciproci.loyalty.program.repository.BrandRepo;
import com.reciproci.loyalty.program.repository.ProgramRepo;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ProgramService {
	
	@Autowired
	private ProgramRepo programRepo;
	
	@Autowired
	private BrandRepo brandRepo;

	public void createProgram(CreateProgramBean createProgramBean) {
		Program program = new Program();
		program.setStatus(createProgramBean.getStatus());
		program.setProgramType(createProgramBean.getProgramType());
		program.setProgramName(createProgramBean.getProgramName());
		program.setProgramDescription(createProgramBean.getProgramDescription());
		program.setProgramImgPath(createProgramBean.getProgramImgPath());
		program.setRewardType(createProgramBean.getRewardType());
		program.setStartDate(createProgramBean.getStartDate());
		program.setEndDate(createProgramBean.getEndDate());
		program.setStartTime(createProgramBean.getStartTime());
		program.setEndTime(createProgramBean.getEndTime());
		program.setPointExpiryIn(createProgramBean.getPointExpiryIn());
		program.setExpireDate(createProgramBean.getExpireDate());
		program.setExpiryDays(createProgramBean.getExpiryDays());
		program.setSkuFilePath(createProgramBean.getSkuFilePath());
		program.setPerpetual(createProgramBean.isPerpetual());
		program.setCreatedTime(LocalDateTime.now());
		program.setModifiedTime(LocalDateTime.now());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate date = LocalDate.parse(createProgramBean.getStartDate(), formatter);
		LocalDate today = LocalDate.now();
		
		if(date.isAfter(today)) {
			program.setApproval_Status("NEW");
			program.setPublish("UPCOMING");
		} else if(date.isEqual(today)) {
			program.setApproval_Status("APPROVED");
			program.setPublish("LIVE");
		}
		Optional<Brand> byId = brandRepo.findById(createProgramBean.getBrandId());
		Brand brand = byId.get();
		brand.setProgram(program);
		program.setBrand(brand);
		
		programRepo.save(program);
	}

	public ViewBean view(Program program) {
		Optional<Program> byId = programRepo.findById(program.getId());
		Program pro = byId.get();
		ViewBean bean = new ViewBean();
		bean.setProgramId(pro.getId());
		bean.setStatus(pro.getStatus());
		bean.setProgramType(pro.getProgramType());
		bean.setProgramName(pro.getProgramName());
		bean.setProgramDescription(pro.getProgramDescription());
		bean.setProgramImgPath(pro.getProgramImgPath());
		bean.setRewardType(pro.getRewardType());
		bean.setStartDate(pro.getStartDate());
		bean.setEndDate(pro.getEndDate());
		bean.setStartTime(pro.getStartTime());
		bean.setEndTime(pro.getEndTime());
		bean.setPointExpiryIn(pro.getPointExpiryIn());
		bean.setExpireDate(pro.getExpireDate());
		bean.setExpiryDays(pro.getExpiryDays());
		bean.setSkuFilePath(pro.getSkuFilePath());
		bean.setPerpetual(pro.isPerpetual());
		bean.setCreatedTime(pro.getCreatedTime());
		bean.setModifiedTime(pro.getModifiedTime());
		bean.setBrand(pro.getBrand());
		bean.setApproval_Status(pro.getApproval_Status());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate date = LocalDate.parse(pro.getEndDate(), formatter);
		LocalDate today = LocalDate.now();
		if(date.isEqual(today)) {
			bean.setPublish("EXPIRED");
		}else {
			bean.setPublish(pro.getPublish());
		}
		return bean;
	}


	
	@Transactional
	public void update(ViewBean pro) {
	    // 1. Use ifPresent or orElseThrow to handle missing data gracefully
	    programRepo.findById(pro.getProgramId()).ifPresentOrElse(existing -> {
	        
	        // 2. Map fields from Bean to Entity
	        existing.setStatus(pro.getStatus());
	        existing.setProgramType(pro.getProgramType());
	        existing.setProgramName(pro.getProgramName());
	        existing.setProgramDescription(pro.getProgramDescription());
	        existing.setProgramImgPath(pro.getProgramImgPath());
	        existing.setRewardType(pro.getRewardType());
	        existing.setStartDate(pro.getStartDate());
	        existing.setEndDate(pro.getEndDate());
	        existing.setStartTime(pro.getStartTime());
	        existing.setEndTime(pro.getEndTime());
	        existing.setPointExpiryIn(pro.getPointExpiryIn());
	        existing.setExpireDate(pro.getExpireDate());
	        existing.setExpiryDays(pro.getExpiryDays());
	        existing.setSkuFilePath(pro.getSkuFilePath());
	        existing.setPerpetual(pro.isPerpetual());
	        Optional<Brand> byId = brandRepo.findById(pro.getBrand().getId());
	        existing.setBrand(byId.get());
	        byId.get().setProgram(existing);
	        existing.setModifiedTime(LocalDateTime.now());

	        // 4. Save is optional if @Transactional is used, but good for clarity
	        programRepo.save(existing);
	        
	    }, () -> {
	        // Handle the "Not Found" case
	        throw new EntityNotFoundException("Program not found with ID: " + pro.getProgramId());
	    });
	}
	
	public SearchResponse search(SearchResponse response) {
		
		Sort.Direction direction = Sort.Direction.ASC;
		String sortColumn = "id";
		
		if(response.getOrder() != null) {
			if("DEC".equalsIgnoreCase(response.getOrder().getDir())) {
				direction = Sort.Direction.DESC;
			}
			
			if(response.getOrder().getColumn() != null && !response.getOrder().getColumn().isEmpty()) {
				sortColumn = response.getOrder().getColumn();
			}
		}
		
		Pageable pageable = PageRequest.of(response.getPage(), response.getPageSize(), Sort.by(direction, sortColumn));
		
		Page<Program> all = programRepo.findAll(pageable);
		List<SearchResponse.SearchItem> arrayList = new ArrayList<>();
		
		for(Program program : all) {
			SearchResponse.SearchItem searchItem = response.new SearchItem();
			searchItem.setProgramId(program.getId());
			if (program.getBrand() != null) {
		        searchItem.setBrandId(program.getBrand().getId());
		        searchItem.setBrandName(program.getBrand().getBrandName());
		    } else {
		        // Optional: Set default values if brand is missing
		        searchItem.setBrandId(null);
		        searchItem.setBrandName("No Brand Assigned");
		    }
			searchItem.setProgramName(program.getProgramName());
			searchItem.setReward(program.getRewardType());
			searchItem.setEndDate(program.getEndDate());
			searchItem.setModifiedTime(program.getModifiedTime());
			searchItem.setStatus(program.getStatus());
			arrayList.add(searchItem);
		}
		response.setTotalCoant(arrayList.size());
		response.setSearchItems(arrayList);
		return response;
	}
	
}
