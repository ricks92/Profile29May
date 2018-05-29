package com.hsc.cat.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hsc.cat.TO.ProfileTO;
import com.hsc.cat.VO.CreateProfileVO;
import com.hsc.cat.entity.EmployeeDetails;
import com.hsc.cat.entity.ProfileEntity;
import com.hsc.cat.repository.EmployeeDetailRepository;
import com.hsc.cat.repository.ProfileRepository;
import com.hsc.cat.repository.SkillProfileRepository;

@Service
@Transactional
public class ProfileService {

	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
private SkillProfileService skillProfileService;
	
	@Autowired
	private SkillProfileRepository skillProfileRepository;
	
	@Autowired
	private EmployeeDetailRepository employeeDetailRepository;
	
	@PersistenceContext
	EntityManager entityManager;
	
	public ProfileTO createProfile(CreateProfileVO profileVO) {
		
		ProfileEntity exists=profileRepository.findByEmpId(profileVO.getUserName());
		
		
		
		ProfileTO createProfileTO=new ProfileTO();
		
		if(exists==null) {
		ProfileEntity profileEntity=new ProfileEntity();
		profileEntity.setEmpId(profileVO.getUserName());
		profileEntity.setProjectRole(profileVO.getProjectRole());
		Date date=new Date();
		profileEntity.setCreationDate(date);
		profileRepository.save(profileEntity);
		
		ProfileEntity saved=profileRepository.findByEmpId(profileVO.getUserName());
		
		createProfileTO.setFirstName(profileVO.getFirstName());
		createProfileTO.setLastName(profileVO.getLastName());
		createProfileTO.setDepartment(profileVO.getDepartment());
		createProfileTO.setEmailId(profileVO.getEmailId());
		createProfileTO.setProjectRole(profileEntity.getProjectRole());
		createProfileTO.setUserName(profileVO.getUserName());
		createProfileTO.setSelectedSkills(profileVO.getSelectedSkills());
		
		System.out.println("\n\n\n#######################"+saved.getId());
		
		for(String skill:profileVO.getSelectedSkills()) {
			skillProfileService.saveSkillProfile(skill, saved.getId());
		}
		
		}
		
		
		else {
		//We need to update the profile
			
			profileRepository.updateProfile(profileVO.getUserName(), profileVO.getProjectRole());
			ProfileEntity saved=profileRepository.findByEmpId(profileVO.getUserName());
			System.out.println("\n\n\n#######################"+saved.getId());
			createProfileTO.setFirstName(profileVO.getFirstName());
			createProfileTO.setLastName(profileVO.getLastName());
			createProfileTO.setDepartment(profileVO.getDepartment());
			createProfileTO.setEmailId(profileVO.getEmailId());
			createProfileTO.setProjectRole(saved.getProjectRole());
			createProfileTO.setUserName(profileVO.getUserName());
			createProfileTO.setSelectedSkills(profileVO.getSelectedSkills());
//			if(profileVO.getSelectedSkills()!=null && !profileVO.getSelectedSkills().isEmpty())
//			{
//				//delete previous skills
////				//skillProfileRepository.deleteByProfileId(saved.getId());
////				String query="Delete from SkillProfileEntity e WHERE e.profileId=:profileId";
////				Query q=entityManager.createQuery(query);
////				q.setParameter("profileId",saved.getId());
////				List<Long> list=q.getResultList();
////				System.out.println("Skills deleted from profile: "+list.size());
//				
//				skillProfileRepository.deleteByProfileId(saved.getId());
//			}
			
			if(profileVO.getSelectedSkills()!=null && !profileVO.getSelectedSkills().isEmpty())
			{
				skillProfileRepository.deleteByProfileId(saved.getId());
			}
			for(String skill:profileVO.getSelectedSkills()) {
				skillProfileService.saveSkillProfile(skill, saved.getId());
			}
			
		}
		return createProfileTO;
	}
	
	
	
	
	public ProfileTO viewProfile(String empId) {
		ProfileTO createProfileTO=new ProfileTO();
		ProfileEntity profileEntity=profileRepository.findByEmpId(empId);
		
		EmployeeDetails emp=employeeDetailRepository.findOne(empId);
		
		createProfileTO.setFirstName(emp.getFirstName());
		createProfileTO.setLastName(emp.getLastName());
		createProfileTO.setEmailId(emp.getEmail());
		createProfileTO.setProjectRole(profileEntity.getProjectRole());
		createProfileTO.setUserName(empId);
		createProfileTO.setDepartment(emp.getDepartment());
		createProfileTO.setSelectedSkills(skillProfileService.viewSkillsInProfile(profileEntity.getId()));
		return createProfileTO;
	}
}
