package com.hsc.cat.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hsc.cat.entity.SkillProfileEntity;
@Repository
@Transactional
public interface SkillProfileRepository extends JpaRepository<SkillProfileEntity, Integer>{

	
	SkillProfileEntity findByProfileIdAndSkillId(int profileId,int skillId);
	
	    @Modifying
	    @Transactional
	    @Query("Delete from SkillProfileEntity e WHERE e.profileId=:profileId")
	    void deleteByProfileId(@Param("profileId")int profileId);
	    
	    
	    List<SkillProfileEntity> findByProfileId(int profileId);
	
}
