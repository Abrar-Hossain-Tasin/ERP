package com.fsiberp.frms.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fsiberp.frms.model.CreateDomain;

import org.springframework.stereotype.Repository;
@Repository
public interface CreateDomainRepository extends JpaRepository<CreateDomain, Long>{
	
	Optional<CreateDomain> findById(Long id);
	List<CreateDomain> findByUserid(String userid);
	CreateDomain findAllByUseridAndFormidAndId(String userid, String formid, Long id);
	List<CreateDomain> findByImplementedbydeptid(Integer implementedbydeptid);
	CreateDomain findTopByUseridOrderByIdDesc(String userid);
	List<CreateDomain> findByActionAndImplementedbystatusAndUserid(String action, String implementedbystatus, String userid);

	List<CreateDomain> findByImplementedbystatus(String implementedbystatus);
	List<CreateDomain> findByImplementedbystatusAndDepartment(String implementedbystatus, String department);
	List<CreateDomain> findByImplementedbystatusAndBranchCode(String implementedbystatus, String branchCode);
}
