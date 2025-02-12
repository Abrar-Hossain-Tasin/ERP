package com.fsiberp.frms.services;

import java.sql.Timestamp;

import com.fsiberp.frms.model.CreateDomain;

import com.fsiberp.frms.model.StatusUpdateRequest;

public interface CreateDomainService {
	CreateDomain createForm(CreateDomain createDomain);
	CreateDomain saveForm(CreateDomain form);
	CreateDomain updateStatus(Long id, String userid, StatusUpdateRequest request, Timestamp currentTimestamp);

}
