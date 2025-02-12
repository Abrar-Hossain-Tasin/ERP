package com.fsiberp.frms.services.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.fsiberp.frms.model.CreateDomain;
import com.fsiberp.frms.model.FunctionalRole;
import com.fsiberp.frms.model.IctDepartment;
import com.fsiberp.frms.model.StatusUpdateRequest;
import com.fsiberp.frms.model.User;
import com.fsiberp.frms.repository.AuthRepository;
import com.fsiberp.frms.repository.CreateDomainRepository;
import com.fsiberp.frms.repository.FunctionalRoleRepository;
import com.fsiberp.frms.repository.IctDepartmentRepository;
import com.fsiberp.frms.repository.ProfileRepository;

import com.fsiberp.frms.services.CreateDomainService;
import com.fsiberp.frms.services.EmailService;
import com.fsiberp.frms.services.NotificationService;
import com.fsiberp.frms.services.ProfileService;

import jakarta.transaction.Transactional;

@Service
public class CreateDomainServiceImpl implements CreateDomainService {

	private CreateDomainRepository createDomainRepository;
	private final FunctionalRoleRepository functionalRoleRepository;
	private final NotificationService notificationService;
	private final ProfileRepository profileRepository;
	private final ProfileService profileService;
	private final AuthRepository authRepository;
	private final IctDepartmentRepository ictDepartmentRepository;
	private EmailService emailService;

	public CreateDomainServiceImpl(CreateDomainRepository createDomainRepository,
			FunctionalRoleRepository functionalRoleRepository, NotificationService notificationService,
			ProfileRepository profileRepository, ProfileService profileService, AuthRepository authRepository,
			IctDepartmentRepository ictDepartmentRepository,EmailService emailService) {
		this.createDomainRepository = createDomainRepository;
		this.functionalRoleRepository = functionalRoleRepository;
		this.notificationService = notificationService;
		this.profileRepository = profileRepository;
		this.profileService = profileService;
		this.authRepository = authRepository;
		this.ictDepartmentRepository = ictDepartmentRepository;
		this.emailService = emailService;
	}

	@Override
	public CreateDomain createForm(CreateDomain createDomain) {
		CreateDomain savedForm = createDomainRepository.save(createDomain);
		if (createDomain.getUnitheaduserid() != null) {
			User user = profileService.getUserByUserid(createDomain.getUserid());
			String username = user != null ? user.getUsername() : "Unknown User";

			notificationService.createNotification(createDomain.getUnitheaduserid(),
					"A new Domain request has been submitted by " + username + " (" + createDomain.getUserid() + ").",
					createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false);
		}
		return savedForm;
	}

	@Transactional
	public CreateDomain saveForm(CreateDomain form) {
		// Save the form temporarily to generate the ID
		CreateDomain savedForm = createDomainRepository.save(form);

		// Generate the reference value using the generated ID
		String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
		String paddedSubmissionId = String.format("%05d", savedForm.getId()); // Padded with zeros
		String referenceValue = currentYear + "-1010/" + paddedSubmissionId;

		// Set the reference value
		savedForm.setReferencevalue(referenceValue);

		// Save the form again with the reference value
		savedForm = createDomainRepository.save(savedForm);

		// Trigger notification for the unit head if the userid is set
		if (savedForm.getUnitheaduserid() != null) {
			User user = profileService.getUserByUserid(savedForm.getUserid());
			String username = user != null ? user.getUsername() : "Unknown User";
			notificationService.createNotification(savedForm.getUnitheaduserid(),
					"A new Domain request has been submitted by " + username + " (" + savedForm.getUserid() + ").",
					savedForm.getUserid(), savedForm.getFormid(), savedForm.getId(), false);

//       unit head email
			emailService.sendNotificationEmail(user,savedForm.getUnitheaduserid(),1);
		}

		return savedForm;
	}

	@Override
	public CreateDomain updateStatus(Long id, String userid, StatusUpdateRequest request, Timestamp currentTimestamp) {
		// Find the form by ID
		CreateDomain createDomain = createDomainRepository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Form not found"));

		// Check and handle Unit Head status update
		if ("Pending".equals(createDomain.getUnitheadstatus()) || "Rejected".equals(createDomain.getUnitheadstatus())
				|| createDomain.getUnitheadstatus() == null) {

			// Fetch the user's information
			User user = profileService.getUserByUserid(userid);
//			String username = user != null ? user.getUsername() : "Unknown User";
			
			createDomain.setUnitheadstatus(request.getStatus());
			createDomain.setUnitheadcmnt(request.getComment());
			createDomain.setUnitheadsubdate(currentTimestamp);

			notificationService.createNotification(createDomain.getUserid(),

					"Your Domain request form has been " + request.getStatus().toLowerCase() + " by Unit Head "
							+ createDomain.getUnitheadusername(),
					createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false // Not viewed by
																									// default
			);

			if ("Accepted".equalsIgnoreCase(request.getStatus())) {

				User formSubmitter = profileService.getUserByUserid(createDomain.getUserid());
				String deptName = formSubmitter != null ? formSubmitter.getDepartment() : "Unknown Branch";
				String branchCode = formSubmitter != null ? formSubmitter.getBranchcode() : "Unknown Code";

				// Notify the SA Head when Unit Head accepts
				notificationService.createNotification(createDomain.getSaheaduserid(),
						"A new Domain request from " + formSubmitter.getUsername() + "(" + formSubmitter.getUserid()  + "), " + deptName + " (" + branchCode + ") requires your approval.",
						createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false);

//           sa head email
				emailService.sendNotificationEmail(user,createDomain.getSaheaduserid(),1);
			}

		} else if ("Accepted".equalsIgnoreCase(createDomain.getUnitheadstatus())
				&& "Pending".equalsIgnoreCase(createDomain.getSaheadstatus())) {

			User formSubmitter = profileService.getUserByUserid(createDomain.getUserid());
			String submitterName = formSubmitter != null ? formSubmitter.getUsername() : "Unknown User";
			String deptName = formSubmitter != null ? formSubmitter.getDepartment() : "Unknown Branch";
			String branchCode = formSubmitter != null ? formSubmitter.getBranchcode() : "Unknown Code";

			// SA status can be updated only if Unit Head status is Accepted
			FunctionalRole functionalRole = functionalRoleRepository.findByUserid(userid)
					.orElseThrow(() -> new NoSuchElementException("No such user role found"));

			if ("sa".equalsIgnoreCase(functionalRole.getFunctionalrole())) {
				createDomain.setSaheadstatus(request.getStatus());
				createDomain.setSaheadcmnt(request.getComment());
				createDomain.setSaheadsubdate(currentTimestamp);

				notificationService.createNotification(createDomain.getUserid(),
						"Your Domain request was " + request.getStatus().toLowerCase() + " by the SA Head "
								+ createDomain.getSaheadusername() + " (" + createDomain.getSaheaduserid() + ").",
						createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false // Not viewed by
																										// default
				);

				if ("Accepted".equalsIgnoreCase(request.getStatus())) {
					User user = profileService.getUserByUserid(createDomain.getUserid());
//					String username = user != null ? user.getUsername() : "Unknown User";

					notificationService.createNotification(createDomain.getIsrmheaduserid(),

							"A new Domain request from " + submitterName + "(" + formSubmitter.getUserid()  + "), " + deptName + " (" + branchCode + ") requires your approval.",

							createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false);

//               isrm head email
					emailService.sendNotificationEmail(user,createDomain.getIsrmheaduserid(),1);
				}
			}

		} else if ("Accepted".equalsIgnoreCase(createDomain.getSaheadstatus())
				&& "Pending".equalsIgnoreCase(createDomain.getIsrmheadstatus())) {
			// ISRM status can be updated only if SA Head status is Accepted
			FunctionalRole functionalRole = functionalRoleRepository.findByUserid(userid)
					.orElseThrow(() -> new NoSuchElementException("No such user role found"));

			if ("isrm".equalsIgnoreCase(functionalRole.getFunctionalrole())) {
				createDomain.setIsrmheadstatus(request.getStatus());
				createDomain.setIsrmheadcmnt(request.getComment());
				createDomain.setIsrmheadsubdate(currentTimestamp);

				notificationService.createNotification(createDomain.getUserid(),
						"Your Domain request was " + request.getStatus().toLowerCase() + " by ISRM Head "
								+ createDomain.getIsrmheadusername() + " (" + createDomain.getIsrmheaduserid() + ").",
						createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false // Not viewed by
																										// default
				);

				if ("Accepted".equalsIgnoreCase(request.getStatus())) {
					User user = profileService.getUserByUserid(createDomain.getUserid());
//					String username = user != null ? user.getUsername() : "Unknown User";

					User formSubmitter = profileService.getUserByUserid(createDomain.getUserid());
					String submitterName = formSubmitter != null ? formSubmitter.getUsername() : "Unknown User";
					String deptName = formSubmitter != null ? formSubmitter.getDepartment() : "Unknown Branch";
					String branchCode = formSubmitter != null ? formSubmitter.getBranchcode() : "Unknown Code";

					notificationService.createNotification(createDomain.getCitouserid(),
							"A new Domain request from " + submitterName + "(" + formSubmitter.getUserid()  + "), " + deptName + " (" + branchCode + ") requires your approval.",

							createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false);

//               cito email
					emailService.sendNotificationEmail(user,createDomain.getCitouserid(),1);
				}
			}
		}

		// Handle CITO status updates and prevent duplicate notifications
		else if ("Accepted".equalsIgnoreCase(createDomain.getIsrmheadstatus())
				&& "Pending".equalsIgnoreCase(createDomain.getCitostatus())) {

			// CITO status can be updated only if ISRM status is Accepted
			FunctionalRole functionalRole = functionalRoleRepository.findByUserid(userid)
					.orElseThrow(() -> new NoSuchElementException("No such user role found"));

			if ("cito".equalsIgnoreCase(functionalRole.getFunctionalrole())) {
				createDomain.setCitostatus(request.getStatus());
				createDomain.setCitocmnt(request.getComment());
				createDomain.setCitosubdate(currentTimestamp);

				notificationService.createNotification(createDomain.getUserid(),
						"Your Domain request was " + request.getStatus().toLowerCase() + " by CITO "
								+ createDomain.getCitousername() + " (" + createDomain.getCitouserid() + ").",
						createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false // Not viewed by
																										// default
				);

				// Mark notifications as viewed for the System Administration Unit members if
				// CITO status is 'Accepted'
				if ("Accepted".equalsIgnoreCase(request.getStatus())) {
//					User user = profileService.getUserByUserid(createDomain.getUserid());
//					String username = user != null ? user.getUsername() : "Unknown User";

					User formSubmitter = profileService.getUserByUserid(createDomain.getUserid());
					String submitterName = formSubmitter != null ? formSubmitter.getUsername() : "Unknown User";
					String deptName = formSubmitter != null ? formSubmitter.getDepartment() : "Unknown Branch";
					String branchCode = formSubmitter != null ? formSubmitter.getBranchcode() : "Unknown Code";

					// Notify System Administration Unit members
					List<User> systemAdmins = profileRepository.findByUnitAndRoleid("System & Hardware Unit", 3);
					for (User sysAdmin : systemAdmins) {
						notificationService.createNotification(sysAdmin.getUserid(),
								"A new Domain request from " + submitterName + "(" + formSubmitter.getUserid()  + "), " + deptName + " (" + branchCode + ") requires your approval.",

								createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false);
					}

//               dept email

					User userinfo = authRepository.findByUserid(createDomain.getUserid()).orElse(null);
					IctDepartment existingUser = ictDepartmentRepository.findById(createDomain.getImplementedbydeptid())
							.orElse(null);
					emailService.sendNotificationEmailForDept(userinfo, existingUser.getDeptmail(),1);
				}
			}
		}

		// Handle the final implementation status ("Done")
		if ("Done".equalsIgnoreCase(request.getStatus())) {
			User user = profileRepository.findByUserid(userid)
					.orElseThrow(() -> new NoSuchElementException("No such user found"));

			// Set the implementation details
			createDomain.setImplementedbystatus(request.getStatus());
			createDomain.setImplementedbyuserid(userid);
			createDomain.setImplementedbyusername(user.getUsername());
			createDomain.setImplementedbysubdate(currentTimestamp);
			createDomain.setImplementedbycmnt(request.getComment());

			// Notify the original user who submitted the form
			notificationService.createNotification(createDomain.getUserid(),
					"Your domain request has been fully granted by " + createDomain.getImplementedbyusername() + " ("
							+ createDomain.getImplementedbyuserid() + ").",
					createDomain.getUserid(), createDomain.getFormid(), createDomain.getId(), false // Not viewed by
																									// default
			);

			// If the CITO status is 'Accepted', mark related notifications as viewed for
			// this form and submission only
			if ("Accepted".equalsIgnoreCase(createDomain.getCitostatus())) {
				// Find System Administration Unit members with role_id = 3
				List<User> systemAdmins = profileRepository.findByUnitAndRoleid("System & Hardware Unit", 3);
				for (User sysAdmin : systemAdmins) {
					// Mark notifications as viewed only for this form and submission
					notificationService.markNotificationsAsViewedForUserAndForm(sysAdmin.getUserid(),
							createDomain.getFormid(), createDomain.getId() // This ensures only notifications for the
																			// current submission are marked
					);
				}

//           user email
				emailService.sendNotificationEmailForUser(user, createDomain.getUserid(),1);
			}
		}
		// Save updates to the createDomain object
		return createDomainRepository.save(createDomain);
	}
}