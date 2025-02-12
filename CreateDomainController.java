package com.fsiberp.frms.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fsiberp.frms.model.BranchInfo;
import com.fsiberp.frms.model.CreateDomain;
import com.fsiberp.frms.model.FunctionalRole;
import com.fsiberp.frms.model.Notification;
import com.fsiberp.frms.model.User;
import com.fsiberp.frms.repository.CreateDomainRepository;
import com.fsiberp.frms.repository.FunctionalRoleRepository;
import com.fsiberp.frms.repository.NotificationRepository;
import com.fsiberp.frms.services.BranchInfoService;
import com.fsiberp.frms.services.CreateDomainService;
import com.fsiberp.frms.services.EmailService;
import com.fsiberp.frms.services.ProfileService;
import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/domain/")
public class CreateDomainController {
	
	private ProfileService profileService;
	private FunctionalRoleRepository functionalRoleRepository;
	private CreateDomainService createDomainService;
	private CreateDomainRepository createDomainRepository;
	private final BranchInfoService branchInfoService;
	private NotificationRepository notificationRepository;
    private EmailService emailService;

	
	   @Value("${file.upload-dir}")
	    private String uploadDir;
	
	public CreateDomainController(ProfileService profileService, NotificationRepository notificationRepository,
			FunctionalRoleRepository functionalRoleRepository, CreateDomainService createDomainService,
			CreateDomainRepository createDomainRepository, BranchInfoService branchInfoService, EmailService emailService ) {
			
			this.profileService = profileService;
	        this.functionalRoleRepository = functionalRoleRepository;
	        this.createDomainService = createDomainService;
	        this.createDomainRepository = createDomainRepository;
	        this.branchInfoService = branchInfoService;
	        this.notificationRepository = notificationRepository;
	        this.emailService = emailService;
	    }
	
	@GetMapping("view/{id}")
    public ResponseEntity<User> showForms(@PathVariable("id") String userid){
        User user = profileService.getUserByUserid(userid);
        
        FunctionalRole citoRole = functionalRoleRepository.findByFunctionalroleAndStatus("cito" , "Active")
                .orElseThrow(NoSuchElementException::new);
        User citoinfo = profileService.getUserByUserid(citoRole.getUserid());
        
        FunctionalRole isrmRole = functionalRoleRepository.findByFunctionalroleAndStatus("isrm" , "Active")
                .orElseThrow(NoSuchElementException::new);
        User isrminfo = profileService.getUserByUserid(isrmRole.getUserid());
        
        FunctionalRole saRole = functionalRoleRepository.findByFunctionalroleAndStatus("sa" , "Active")
                .orElseThrow(NoSuchElementException::new);
        User sainfo = profileService.getUserByUserid(saRole.getUserid());
		
		user.setCreatedby(citoinfo.getUsername());
		user.setUsergrp(isrminfo.getUsername());
		user.setChngpassword(sainfo.getUsername());
		user.setPassword("Pending");
	
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

	@PostMapping("save/{id}")
	public ResponseEntity<?> createForm(@PathVariable("id") String userid,
	                                    @ModelAttribute @Valid CreateDomain createDomain,
	                                    BindingResult bindingResult,
	                                    @RequestParam(value = "joiningLetter", required = false) MultipartFile joiningLetter) {

	    if (bindingResult.hasErrors()) {
	        // Handle validation errors
	        return ResponseEntity.badRequest().body("Validation failed: " + bindingResult.getAllErrors());
	    }

	    User user = profileService.getUserByUserid(userid);

	    FunctionalRole citoRole = functionalRoleRepository.findByFunctionalroleAndStatus("cito", "Active")
	            .orElseThrow(NoSuchElementException::new);
	    User citoinfo = profileService.getUserByUserid(citoRole.getUserid());

	    FunctionalRole isrmRole = functionalRoleRepository.findByFunctionalroleAndStatus("isrm", "Active")
	            .orElseThrow(NoSuchElementException::new);
	    User isrminfo = profileService.getUserByUserid(isrmRole.getUserid());

	    FunctionalRole saRole = functionalRoleRepository.findByFunctionalroleAndStatus("sa", "Active")
	            .orElseThrow(NoSuchElementException::new);
	    User sainfo = profileService.getUserByUserid(saRole.getUserid());

	    createDomain.setFormid("1010");
	    createDomain.setUserid(user.getUserid());
	    createDomain.setCitouserid(citoinfo.getUserid());
	    createDomain.setCitousername(citoinfo.getUsername());
	    createDomain.setCitostatus("Pending");
	    createDomain.setIsrmheaduserid(isrminfo.getUserid());
	    createDomain.setIsrmheadusername(isrminfo.getUsername());
	    createDomain.setIsrmheadstatus("Pending");
	    createDomain.setSaheaduserid(sainfo.getUserid());
	    createDomain.setSaheadusername(sainfo.getUsername());
	    createDomain.setSaheadstatus("Pending");
	    createDomain.setBranchCode(user.getBranchcode());

	    if (user.getDepartment().equals("I C T Div., Head Office, Dhaka")) {
	        createDomain.setDepartment(user.getUnit());
	    } else {
	        createDomain.setDepartment(user.getDepartment());
	    }

	    createDomain.setImplementedbystatus("Pending");

	    createDomain.setUnitheadstatus("Pending");
	    createDomain.setImplementedbydeptid(10);

	    createDomain.setSubmitdate(new Date(System.currentTimeMillis()));
	    createDomain.setSubmittime(new Timestamp(System.currentTimeMillis()));

	    // Handle the joiningLetter file update
	    try {
	        if (joiningLetter != null && !joiningLetter.isEmpty()) {
	            if (joiningLetter.getSize() > 250 * 1024) { // File size limit: 250 KB

	                Map<String, String> errorResponse = new HashMap<>();
	                errorResponse.put("error", String.format("%s file size exceeds the limit of 250 KB.", joiningLetter.getOriginalFilename()));
	                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	            }

	            // Use Apache Tika for file type detection
	            String fileType = getFileTypeFromTika(joiningLetter);

	            // Validate allowed file types (PDF, JPEG, PNG)
	            if (!"application/pdf".equals(fileType) && !"image/jpeg".equals(fileType) && !"image/png".equals(fileType)) {
	                Map<String, String> errorResponse = new HashMap<>();
	                errorResponse.put("error", "Invalid file type. Only PDF, JPEG, and PNG files are allowed.");
	                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	            }

	            // Check file extension (allow only PDF, JPEG, and PNG)
	            String fileExtension = getFileExtension(joiningLetter.getOriginalFilename());
	            if (!"pdf".equalsIgnoreCase(fileExtension) && !"jpg".equalsIgnoreCase(fileExtension) &&
	                !"jpeg".equalsIgnoreCase(fileExtension) && !"png".equalsIgnoreCase(fileExtension)) {
	                Map<String, String> errorResponse = new HashMap<>();
	                errorResponse.put("error", "Invalid file extension. Only PDF, JPG, JPEG, and PNG files are allowed.");
	                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	            }

	            String joiningLetterPath = saveFile(userid, joiningLetter);
	            createDomain.setJoiningLetterPath(joiningLetterPath);
	        }
	    } catch (IOException | TikaException e) {
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Error while processing file: " + e.getMessage());
	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    CreateDomain savedForm = createDomainService.saveForm(createDomain);
	    return new ResponseEntity<>(savedForm, HttpStatus.CREATED);
	}

	// Save file to disk
	private String saveFile(String userid, MultipartFile file) throws IOException {
	    String originalFilename = file.getOriginalFilename();
	    String sanitizedFilename = sanitizeFileName(originalFilename);
	    String filename = userid + "_" + 1010 + "_" + System.currentTimeMillis() + "~" + sanitizedFilename;

	    Path filePath = Paths.get(uploadDir, filename);

	    try (InputStream inputStream = file.getInputStream()) {
	        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
	    }

	    return filePath.toString();
	}

	// Sanitize file name
	private String sanitizeFileName(String originalFilename) {
	    return originalFilename.replaceAll("[^a-zA-Z0-9.-]", "_");
	}

	// Extract file extension
	private String getFileExtension(String filename) {
	    if (filename == null || filename.isEmpty()) {
	        return "";
	    }
	    int dotIndex = filename.lastIndexOf(".");
	    if (dotIndex == -1) {
	        return "";
	    }
	    return filename.substring(dotIndex + 1);
	}

	// Use Tika to detect file type
	private String getFileTypeFromTika(MultipartFile file) throws IOException, TikaException {
	    Tika tika = new Tika();
	    try (InputStream inputStream = file.getInputStream()) {
	        return tika.detect(inputStream);
	    }
	}
	
	  
	  @GetMapping("viewform/{id}")
	    public ResponseEntity<CreateDomain> viewForms(@PathVariable("id") String userid) {
		  CreateDomain createDomain = createDomainRepository.findTopByUseridOrderByIdDesc(userid);
	        if (createDomain == null) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        // Set download URLs if paths are not null
	       
	        if (createDomain.getJoiningLetterPath() != null) {
	            String joiningLetterDownloadUrl = "/api/domain/download/joiningLetter/" + createDomain.getId();
	            createDomain.setJoiningLetterDownloadUrl(joiningLetterDownloadUrl);
	        }

	        return new ResponseEntity<>(createDomain, HttpStatus.OK);
	    }
	  
	  @GetMapping("download/joiningLetter/{id}")
	    public ResponseEntity<Resource> downloadJoiningLetter(@PathVariable("id") Long id) {
		  CreateDomain createDomain = createDomainRepository.findById(id).orElse(null);
	        if (createDomain == null || createDomain.getJoiningLetterPath() == null) {
	            return ResponseEntity.notFound().build();
	        }
	        try {
	            Path filePath = Paths.get(createDomain.getJoiningLetterPath());
	            Resource resource = new UrlResource(filePath.toUri());

	            String originalFilename = filePath.getFileName().toString();
	            String contentType = Files.probeContentType(filePath);

	            return ResponseEntity.ok()
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + originalFilename + "\"")
	                    .contentType(MediaType.parseMediaType(contentType))
	                    .body(resource);
	        } catch (IOException e) {
	            return ResponseEntity.badRequest().build();
	        }
	    }
	  
	  @GetMapping("branches")
	    public ResponseEntity<List<BranchInfo>> getAllBranches() {
	        List<BranchInfo> branches = branchInfoService.getAllBranches();
	        
	        if (branches != null && !branches.isEmpty()) {
	            return ResponseEntity.ok(branches);
	        } else {
	            return ResponseEntity.noContent().build(); // Return HTTP 204 No Content if no branches are found
	        }
	    }
	
	  @PutMapping("update/{id}")
	  public ResponseEntity<?> updateCreateDomain(
	          @PathVariable("id") Long id,
	          @ModelAttribute @Valid CreateDomain updatedCreateDomain,
	          @RequestParam(value = "joiningLetter", required = false) MultipartFile joiningLetter) {

	      CreateDomain createDomain = createDomainRepository.findById(id)
	              .orElseThrow(NoSuchElementException::new);

	      // Update fields based on the incoming data
	      createDomain.setAction(updatedCreateDomain.getAction());
	      createDomain.setPreviousBranchName(updatedCreateDomain.getPreviousBranchName());
	      createDomain.setPreviousBranchCode(updatedCreateDomain.getPreviousBranchCode());
	      createDomain.setNewBranchName(updatedCreateDomain.getNewBranchName());
	      createDomain.setNewBranchCode(updatedCreateDomain.getNewBranchCode());
	      createDomain.setTransferDate(updatedCreateDomain.getTransferDate());

	      createDomain.setUnitheadstatus("Pending");
	      createDomain.setSubmittime(new Timestamp(System.currentTimeMillis()));
	      createDomain.setSubmitdate(new Date(System.currentTimeMillis()));

	      // Handle the joiningLetter file update
	      try {
	          if (joiningLetter != null && !joiningLetter.isEmpty()) {
	              // Check file size (250 KB limit)
	              if (joiningLetter.getSize() > 250 * 1024) {
	                  Map<String, String> errorResponse = new HashMap<>();
	                  errorResponse.put("error", String.format("%s file size exceeds the limit of 250 KB.", joiningLetter.getOriginalFilename()));
	                  return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	              }

	              // Used Apache Tika to check the actual content type of the file
	              String fileType = getFileTypeFromTika(joiningLetter);
	              if (!"application/pdf".equals(fileType) && !"image/jpeg".equals(fileType) && !"image/png".equals(fileType)) {
	                  Map<String, String> errorResponse = new HashMap<>();
	                  errorResponse.put("error", "Invalid file type. Only PDF, JPEG, and PNG files are allowed.");
	                  return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	              }

	              // Check file extension (allow only PDF, JPEG, and PNG)
	              String fileExtension = getFileExtension(joiningLetter.getOriginalFilename());
	              if (!"pdf".equalsIgnoreCase(fileExtension) && !"jpg".equalsIgnoreCase(fileExtension) && !"jpeg".equalsIgnoreCase(fileExtension) && !"png".equalsIgnoreCase(fileExtension)) {
	                  Map<String, String> errorResponse = new HashMap<>();
	                  errorResponse.put("error", "Invalid file extension. Only PDF, JPG, JPEG, and PNG files are allowed.");
	                  return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	              }

	              // Delete the old file if it exists
	              String oldFilePath = createDomain.getJoiningLetterPath();
	              if (oldFilePath != null) {
	                  File oldFile = new File(oldFilePath);
	                  if (oldFile.exists() && !oldFile.delete()) {
	                      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                              .body("Failed to delete the old joining letter file.");
	                  }
	              }

	              // Save the new file and update the path
	              String joiningLetterPath = saveFile(createDomain.getUserid(), joiningLetter);
	              createDomain.setJoiningLetterPath(joiningLetterPath);
	          }
	      } catch (IOException | TikaException e) {
	          Map<String, String> errorResponse = new HashMap<>();
	          errorResponse.put("error", "Error while saving files: " + e.getMessage());
	          return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	      }

	      // Handle the unit head update logic as before (if needed)
	      if (!updatedCreateDomain.getUnitheaduserid().equals(createDomain.getUnitheaduserid())) {
	          String newUnitHeadUserId = updatedCreateDomain.getUnitheaduserid();
	          User newUnitHeadUser = profileService.getUserByUserid(newUnitHeadUserId);
	          String newUnitHeadUsername = newUnitHeadUser != null ? newUnitHeadUser.getUsername() : "Unknown User";

	          List<Notification> existingNotifications = notificationRepository.findByUseridAndFormidAndSubmissionId(
	                  createDomain.getUnitheaduserid(),
	                  createDomain.getFormid(),
	                  createDomain.getId()
	          );

	          User submittingUser = profileService.getUserByUserid(createDomain.getUserid());
	          String submittingUsername = submittingUser != null ? submittingUser.getUsername() : "Unknown User";

	          // Update existing notifications with the new unit head details
	          for (Notification notification : existingNotifications) {
	              notification.setUserid(newUnitHeadUserId);
	              notification.setViewed(false);
	              notification.setMessage(
	                      "A new Domain request has been submitted by " + submittingUsername +
	                              " (" + createDomain.getUserid() + ")."
	              );
	              notificationRepository.save(notification);
	          }

	          // Update create domain with the new unit head
	          createDomain.setUnitheaduserid(newUnitHeadUserId);
	          createDomain.setUnitheadusername(newUnitHeadUsername);

	          // Send notification email (if necessary)
	          emailService.sendNotificationEmail(newUnitHeadUser, updatedCreateDomain.getUserid(), 1);
	      }

	      // Save the updated CreateDomain object
	      CreateDomain updatedForm = createDomainRepository.save(createDomain);
	      return new ResponseEntity<>(updatedForm, HttpStatus.OK);
	  }


	  
	  @DeleteMapping("removefile/{id}")
	  public ResponseEntity<?> removeJoiningLetter(@PathVariable("id") Long id) {
	  
	      CreateDomain createDomain = createDomainRepository.findById(id)
	              .orElseThrow(() -> new NoSuchElementException("CreateDomain not found with id: " + id));

	
	      String joiningLetterPath = createDomain.getJoiningLetterPath();
	      if (joiningLetterPath == null || joiningLetterPath.isEmpty()) {
	          return new ResponseEntity<>("No file to remove", HttpStatus.BAD_REQUEST);
	      }

	      File file = new File(joiningLetterPath);
	      if (file.exists() && file.delete()) {
	   
	          createDomain.setJoiningLetterPath(null);
	          createDomainRepository.save(createDomain);

	          return new ResponseEntity<>("File removed successfully", HttpStatus.OK);
	      } else {
	          return new ResponseEntity<>("Failed to delete the file from the server", HttpStatus.INTERNAL_SERVER_ERROR);
	      }
	  }

}
