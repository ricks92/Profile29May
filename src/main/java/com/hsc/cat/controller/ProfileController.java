package com.hsc.cat.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hsc.cat.TO.ProfileTO;
import com.hsc.cat.VO.CreateProfileVO;
import com.hsc.cat.service.EmployeeSkillService;
import com.hsc.cat.service.ProfileService;
import com.hsc.cat.utilities.JSONOutputEnum;
import com.hsc.cat.utilities.JSONOutputModel;
import com.hsc.cat.utilities.RESTURLConstants;
import com.hsc.cat.utilities.StatusCode;

@RestController
public class ProfileController {

	private static final Logger LOGGER = (Logger) LogManager.getLogger(EmployeeSkillService.class);
	@Autowired
	private ProfileService profileService;
	
	@RequestMapping(value=RESTURLConstants.CREATE_PROFILE,method=RequestMethod.POST)
	@CrossOrigin
	@ResponseBody
	public JSONOutputModel createProfile(@RequestBody CreateProfileVO profileVO) {
		JSONOutputModel output= new JSONOutputModel();
		LOGGER.info("Request came to create profile for:"+profileVO.getUserName());
	
//		if(profileVO.getUsername()==null) {
//			output.setMessage("Username cannot be null");
//			output.setStatus(JSONOutputEnum.FAILURE.getValue());
//			return output;
//		}
		ProfileTO createProfileTO= profileService.createProfile(profileVO);
		output.setData(createProfileTO);
		if(createProfileTO!=null) {
			output.setMessage("Profile updated successfully");
			output.setStatus(JSONOutputEnum.SUCCESS.getValue());
		}
		else {
			output.setMessage("Profile could not be  updated");
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
		}
		
		
		return output;
	}
	
	
	
	@RequestMapping(value=RESTURLConstants.VIEW_PROFILE,method=RequestMethod.GET)
	@CrossOrigin
	@ResponseBody
	public JSONOutputModel viewProfile(@PathVariable("empId")String empId) {
		JSONOutputModel output= new JSONOutputModel();
		ProfileTO createProfileTO= profileService.viewProfile(empId);
		output.setData(createProfileTO);
		
		
		if(createProfileTO!=null && createProfileTO.getProjectRole()!=null && !createProfileTO.getSelectedSkills().isEmpty()) {
			output.setMessage("Profile complete..Fetched successfully");
			output.setStatus(StatusCode.PROFILE_COMPLETE);
		}
		else if(createProfileTO!=null && createProfileTO.getProjectRole()!=null || !createProfileTO.getSelectedSkills().isEmpty()) {
			output.setMessage("Profile incomplete..Fetched successfully");
			output.setStatus(StatusCode.PROFILE_INCOMPLETE);
		}
		
		else {
			output.setMessage("You are giving invalid username");
			output.setStatus(JSONOutputEnum.FAILURE.getValue());
		}
		return output;
	}
}
