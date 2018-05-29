package com.hsc.cat.controller;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hsc.cat.TO.SearchSkillTOList;
import com.hsc.cat.TO.SkillTO;
import com.hsc.cat.VO.AddSkillVO;
import com.hsc.cat.service.SkillService;
import com.hsc.cat.utilities.JSONOutputEnum;
import com.hsc.cat.utilities.JSONOutputModel;
import com.hsc.cat.utilities.RESTURLConstants;

@RestController
@CrossOrigin
public class SkillController {


	private static final Logger LOGGER = (Logger) LogManager.getLogger(SkillController.class);
	
	@Autowired
	private SkillService skillService;
	@ResponseBody
	@RequestMapping(value=RESTURLConstants.SKILLS,method=RequestMethod.POST)
	@CrossOrigin
	public JSONOutputModel addSkill(@RequestBody AddSkillVO svo) {
		LOGGER.debug("Inside add skill controller method");
		JSONOutputModel output = new JSONOutputModel();
		SkillTO skillTO=skillService.addSkill(svo);
		if(skillTO!=null) {
			
			if(skillTO.getIssue()!=null) {
				output.setData(new String(skillTO.getIssue()));
				output.setStatus(JSONOutputEnum.FAILURE.getValue());
				output.setMessage(skillTO.getIssue());
				LOGGER.error(skillTO.getIssue());
			}
			else {
			output.setData(skillTO);
			output.setStatus(HttpStatus.CREATED.value());
			output.setMessage("Skills saved successfully");
			LOGGER.debug("Skills saved successfully");
			}
			
		}
		else {
			output.setData(skillTO);
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
			output.setMessage("Skills could not be saved");
			LOGGER.error("Skills could not be saved");
		}
		
		return output;
	}
	
	
	@ResponseBody
	@RequestMapping(value=RESTURLConstants.SKILLS,method=RequestMethod.GET)
	@CrossOrigin
	public JSONOutputModel fetchAllSkills() {
		LOGGER.debug("Inside fetch all skills controller method");
		JSONOutputModel output = new JSONOutputModel();
		
		List<SkillTO> skillTOList=skillService.fetchAllSkills();
		if(!skillTOList.isEmpty() && skillTOList.size()!=0) {
			output.setData(skillTOList);
			output.setStatus(JSONOutputEnum.SUCCESS.getValue());
			output.setMessage("Skills fetched successfully");
			LOGGER.debug("Skills fetched successfully");
		}else {
			output.setData(skillTOList);
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
			output.setMessage("No skill found");
			LOGGER.error("No skill found");
		}
		
		return output;
	}
	
	
	@ResponseBody
	@RequestMapping(value="searchSkill/{skillName}",method=RequestMethod.GET)
	@CrossOrigin
	public JSONOutputModel searchSkill(@PathVariable("skillName") String skillName) {
		JSONOutputModel output = new JSONOutputModel();
		LOGGER.debug("Inside search skill controller method");
		SearchSkillTOList searchSkillTO=skillService.skillExists(skillName);
		
		if(searchSkillTO.isExists()) {
			output.setData(searchSkillTO.getSkills());
			output.setStatus(JSONOutputEnum.SUCCESS.getValue());
			output.setMessage("Skill already exists!!!");
			LOGGER.debug("Skill already exists!!!");
		}
		else {
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
			output.setMessage("Skill does not exist!!!");
			LOGGER.error("Skill does not exist!!!");
		}
		
		return output;
	}
	
	
	
	@ResponseBody
	@RequestMapping(value=RESTURLConstants.FETCH_SKILLS_IN_A_CATEGORY,method=RequestMethod.GET)
	@CrossOrigin
	public JSONOutputModel fetchSkillsInACategory(@PathVariable("skillCategory")String skillCategory) {
		JSONOutputModel output = new JSONOutputModel();
		List<SkillTO> skillTOList=skillService.fetchSkillsInACategory(skillCategory);
		output.setData(skillTOList);
		if(skillTOList!=null && !skillTOList.isEmpty()) {
			output.setMessage("Skills fetched in skill category:"+skillCategory);
			output.setStatus(JSONOutputEnum.SUCCESS.getValue());
		}
		
		else {
			output.setMessage("No skill in skill category:"+skillCategory);
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
		}
		
		return output;
	}
	
	
	@ResponseBody
	@RequestMapping(value=RESTURLConstants.FETCH_SKILLS_IN_A_CATEGORY_SUBCATEGORY,method=RequestMethod.GET)
	@CrossOrigin
	public JSONOutputModel fetchSkillsInACategorySubcategory(@RequestParam("skillCategory")String skillCategory,@RequestParam("skillSubCategory")String skillSubCategory) {
		JSONOutputModel output = new JSONOutputModel();
		List<SkillTO> skillTOList=skillService.fetchSkillsInACategorySubcategory(skillCategory, skillSubCategory);
		output.setData(skillTOList);
		if(skillTOList!=null && !skillTOList.isEmpty()) {
			output.setMessage("Skills fetched in skill category:"+skillCategory +" and subCategory: "+skillSubCategory);
			output.setStatus(JSONOutputEnum.SUCCESS.getValue());
		}
		
		else {
			output.setMessage("No skill found in skill category:"+skillCategory+" and subCategory: "+skillSubCategory);
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
		}
		
		return output;
	}
}
