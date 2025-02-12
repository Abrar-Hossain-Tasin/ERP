package com.fsiberp.frms.model;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "form_create_domain")
public class CreateDomain {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "form_id")
    private String formid;
    
    
    @Size(max = 50)
    @Column(name = "user_id")
    private String userid;
    
    @Size(max = 50)
    @Column(name = "ref")
	private String referenceValue;
    
    @Size(max = 50)
    @Column(name = "action")
	private String action;
    
    @Column(name = "new_branch_name")
    private String newBranchName;

    @Column(name = "new_branch_code")
    private String newBranchCode;

    @Column(name = "previous_branch_name")
    private String previousBranchName;

    @Column(name = "previous_branch_code")
    private String previousBranchCode;

    @Column(name = "transfer_date")
    private Date transferDate;
    
    @Column(name = "submit_date")
	private Date submitdate;
    
    @Column(name = "submit_time")
	private Timestamp submittime;
    
    @Size(max = 50)
	@Column(name = "unit_head_userid")
	private String unitheaduserid;
	
	@Size(max = 50)
	@Column(name = "unit_head_username")
	private String unitheadusername;
	
	@Size(max = 50)
	@Column(name = "unit_head_status")
	private String unitheadstatus;
	
	@Column(name = "unit_head_sub_date")
	private Timestamp unitheadsubdate;
	
	@Size(max = 255)
	@Column(name = "unit_head_cmnt")
	private String unitheadcmnt;
	
	@Size(max = 50)
	@Column(name = "sa_head_userid")
	private String saheaduserid;
	
	@Size(max = 50)
	@Column(name = "sa_head_username")
	private String saheadusername;
	
	@Size(max = 50)
	@Column(name = "sa_head_status")
	private String saheadstatus;
	
	@Column(name = "sa_head_sub_date")
	private Timestamp saheadsubdate;
	
	@Size(max = 255)
	@Column(name = "sa_head_cmnt")
	private String saheadcmnt;
	
	
	@Size(max = 50)
	@Column(name = "isrm_head_userid")
	private String isrmheaduserid;
	
	@Size(max = 50)
	@Column(name = "isrm_head_username")
	private String isrmheadusername;
	
	@Size(max = 50)
	@Column(name = "isrm_head_status")
	private String isrmheadstatus;
	
	@Column(name = "isrm_head_sub_date")
	private Timestamp isrmheadsubdate;
	
	@Size(max = 255)
	@Column(name = "isrm_head_cmnt")
	private String isrmheadcmnt;
	
	@Size(max = 50)
	@Column(name = "cito_userid")
	private String citouserid;
	
	@Size(max = 50)
	@Column(name = "cito_username")
	private String citousername;
	
	@Size(max = 50)
	@Column(name = "cito_status")
	private String citostatus;
	
	@Column(name = "cito_sub_date")
	private Timestamp citosubdate;
	
	@Size(max = 255)
	@Column(name = "cito_cmnt")
	private String citocmnt;
	
	
	@Size(max = 50)
	@Column(name = "implemented_by_userid")
	private String implementedbyuserid;
	
	@Size(max = 50)
	@Column(name = "implemented_by_username")
	private String implementedbyusername;
	
	@Size(max = 50)
	@Column(name = "implemented_by_status")
	private String implementedbystatus;
	
    @Size(max = 255)
	@Column(name = "implemented_by_cmnt")
	private String implementedbycmnt;
	
	@Column(name = "implemented_by_sub_date")
	private Timestamp implementedbysubdate;
	
	@Column(name = "implemented_by_dept_id")
	private Integer implementedbydeptid;
	
	@Column(name = "joining_letter_path")
    private String joiningLetterPath;
	
	@Column(name = "branch_code")
	private String branchCode;
	
	@Column(name = "department")
	private String department;
 
    @Transient
    private String joiningLetterDownloadUrl;
    

	public CreateDomain() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public CreateDomain(Long id, String formid, @NotBlank @Size(max = 50) String userid, String department,
			@Size(max = 50) String referenceValue, @Size(max = 50) String action, String newBranchName,
			String newBranchCode, String previousBranchName, String previousBranchCode, Date transferDate,
			Date submitdate, Timestamp submittime, @Size(max = 50) String unitheaduserid,
			@Size(max = 50) String unitheadusername, @Size(max = 50) String unitheadstatus, Timestamp unitheadsubdate,
			@Size(max = 255) String unitheadcmnt, @Size(max = 50) String saheaduserid,
			@Size(max = 50) String saheadusername, @Size(max = 50) String saheadstatus, Timestamp saheadsubdate,
			@Size(max = 255) String saheadcmnt, @Size(max = 50) String isrmheaduserid,
			@Size(max = 50) String isrmheadusername, @Size(max = 50) String isrmheadstatus, Timestamp isrmheadsubdate,
			@Size(max = 255) String isrmheadcmnt, @Size(max = 50) String citouserid,
			@Size(max = 50) String citousername, @Size(max = 50) String citostatus, Timestamp citosubdate,
			@Size(max = 255) String citocmnt, @Size(max = 50) String implementedbyuserid,
			@Size(max = 50) String implementedbyusername, @Size(max = 50) String implementedbystatus,
			@Size(max = 255) String implementedbycmnt, Timestamp implementedbysubdate, Integer implementedbydeptid,
			String joiningLetterPath, String joiningLetterDownloadUrl, String branchCode) {
		super();
		this.id = id;
		this.formid = formid;
		this.userid = userid;
		this.referenceValue = referenceValue;
		this.action = action;
		this.newBranchName = newBranchName;
		this.newBranchCode = newBranchCode;
		this.previousBranchName = previousBranchName;
		this.previousBranchCode = previousBranchCode;
		this.transferDate = transferDate;
		this.submitdate = submitdate;
		this.submittime = submittime;
		this.unitheaduserid = unitheaduserid;
		this.unitheadusername = unitheadusername;
		this.unitheadstatus = unitheadstatus;
		this.unitheadsubdate = unitheadsubdate;
		this.unitheadcmnt = unitheadcmnt;
		this.saheaduserid = saheaduserid;
		this.saheadusername = saheadusername;
		this.saheadstatus = saheadstatus;
		this.saheadsubdate = saheadsubdate;
		this.saheadcmnt = saheadcmnt;
		this.isrmheaduserid = isrmheaduserid;
		this.isrmheadusername = isrmheadusername;
		this.isrmheadstatus = isrmheadstatus;
		this.isrmheadsubdate = isrmheadsubdate;
		this.isrmheadcmnt = isrmheadcmnt;
		this.citouserid = citouserid;
		this.citousername = citousername;
		this.citostatus = citostatus;
		this.citosubdate = citosubdate;
		this.citocmnt = citocmnt;
		this.implementedbyuserid = implementedbyuserid;
		this.implementedbyusername = implementedbyusername;
		this.implementedbystatus = implementedbystatus;
		this.implementedbycmnt = implementedbycmnt;
		this.implementedbysubdate = implementedbysubdate;
		this.implementedbydeptid = implementedbydeptid;
		this.joiningLetterPath = joiningLetterPath;
		this.joiningLetterDownloadUrl = joiningLetterDownloadUrl;
		this.branchCode = branchCode;
		this.department = department;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFormid() {
		return formid;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getReferencevalue() {
		return referenceValue;
	}

	public void setReferencevalue(String referencevalue) {
		this.referenceValue = referencevalue;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getNewBranchName() {
		return newBranchName;
	}

	public void setNewBranchName(String newBranchName) {
		this.newBranchName = newBranchName;
	}

	public String getNewBranchCode() {
		return newBranchCode;
	}

	public void setNewBranchCode(String newBranchCode) {
		this.newBranchCode = newBranchCode;
	}

	public String getPreviousBranchName() {
		return previousBranchName;
	}

	public void setPreviousBranchName(String previousBranchName) {
		this.previousBranchName = previousBranchName;
	}

	public String getPreviousBranchCode() {
		return previousBranchCode;
	}

	public void setPreviousBranchCode(String previousBranchCode) {
		this.previousBranchCode = previousBranchCode;
	}

	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public Date getSubmitdate() {
		return submitdate;
	}

	public void setSubmitdate(Date submitdate) {
		this.submitdate = submitdate;
	}

	public Timestamp getSubmittime() {
		return submittime;
	}

	public void setSubmittime(Timestamp submittime) {
		this.submittime = submittime;
	}

	public String getUnitheaduserid() {
		return unitheaduserid;
	}

	public void setUnitheaduserid(String unitheaduserid) {
		this.unitheaduserid = unitheaduserid;
	}

	public String getUnitheadusername() {
		return unitheadusername;
	}

	public void setUnitheadusername(String unitheadusername) {
		this.unitheadusername = unitheadusername;
	}

	public String getUnitheadstatus() {
		return unitheadstatus;
	}

	public void setUnitheadstatus(String unitheadstatus) {
		this.unitheadstatus = unitheadstatus;
	}

	public Timestamp getUnitheadsubdate() {
		return unitheadsubdate;
	}

	public void setUnitheadsubdate(Timestamp unitheadsubdate) {
		this.unitheadsubdate = unitheadsubdate;
	}

	public String getUnitheadcmnt() {
		return unitheadcmnt;
	}

	public void setUnitheadcmnt(String unitheadcmnt) {
		this.unitheadcmnt = unitheadcmnt;
	}

	public String getSaheaduserid() {
		return saheaduserid;
	}

	public void setSaheaduserid(String saheaduserid) {
		this.saheaduserid = saheaduserid;
	}

	public String getSaheadusername() {
		return saheadusername;
	}

	public void setSaheadusername(String saheadusername) {
		this.saheadusername = saheadusername;
	}

	public String getSaheadstatus() {
		return saheadstatus;
	}

	public void setSaheadstatus(String saheadstatus) {
		this.saheadstatus = saheadstatus;
	}

	public Timestamp getSaheadsubdate() {
		return saheadsubdate;
	}

	public void setSaheadsubdate(Timestamp saheadsubdate) {
		this.saheadsubdate = saheadsubdate;
	}

	public String getSaheadcmnt() {
		return saheadcmnt;
	}

	public void setSaheadcmnt(String saheadcmnt) {
		this.saheadcmnt = saheadcmnt;
	}

	public String getIsrmheaduserid() {
		return isrmheaduserid;
	}

	public void setIsrmheaduserid(String isrmheaduserid) {
		this.isrmheaduserid = isrmheaduserid;
	}

	public String getIsrmheadusername() {
		return isrmheadusername;
	}

	public void setIsrmheadusername(String isrmheadusername) {
		this.isrmheadusername = isrmheadusername;
	}

	public String getIsrmheadstatus() {
		return isrmheadstatus;
	}

	public void setIsrmheadstatus(String isrmheadstatus) {
		this.isrmheadstatus = isrmheadstatus;
	}

	public Timestamp getIsrmheadsubdate() {
		return isrmheadsubdate;
	}

	public void setIsrmheadsubdate(Timestamp isrmheadsubdate) {
		this.isrmheadsubdate = isrmheadsubdate;
	}

	public String getIsrmheadcmnt() {
		return isrmheadcmnt;
	}

	public void setIsrmheadcmnt(String isrmheadcmnt) {
		this.isrmheadcmnt = isrmheadcmnt;
	}

	public String getCitouserid() {
		return citouserid;
	}

	public void setCitouserid(String citouserid) {
		this.citouserid = citouserid;
	}

	public String getCitousername() {
		return citousername;
	}

	public void setCitousername(String citousername) {
		this.citousername = citousername;
	}

	public String getCitostatus() {
		return citostatus;
	}

	public void setCitostatus(String citostatus) {
		this.citostatus = citostatus;
	}

	public Timestamp getCitosubdate() {
		return citosubdate;
	}

	public void setCitosubdate(Timestamp citosubdate) {
		this.citosubdate = citosubdate;
	}

	public String getCitocmnt() {
		return citocmnt;
	}

	public void setCitocmnt(String citocmnt) {
		this.citocmnt = citocmnt;
	}

	public String getImplementedbyuserid() {
		return implementedbyuserid;
	}

	public void setImplementedbyuserid(String implementedbyuserid) {
		this.implementedbyuserid = implementedbyuserid;
	}

	public String getImplementedbyusername() {
		return implementedbyusername;
	}

	public void setImplementedbyusername(String implementedbyusername) {
		this.implementedbyusername = implementedbyusername;
	}

	public String getImplementedbystatus() {
		return implementedbystatus;
	}

	public void setImplementedbystatus(String implementedbystatus) {
		this.implementedbystatus = implementedbystatus;
	}

	public String getImplementedbycmnt() {
		return implementedbycmnt;
	}

	public void setImplementedbycmnt(String implementedbycmnt) {
		this.implementedbycmnt = implementedbycmnt;
	}

	public Timestamp getImplementedbysubdate() {
		return implementedbysubdate;
	}

	public void setImplementedbysubdate(Timestamp implementedbysubdate) {
		this.implementedbysubdate = implementedbysubdate;
	}

	public Integer getImplementedbydeptid() {
		return implementedbydeptid;
	}

	public void setImplementedbydeptid(Integer implementedbydeptid) {
		this.implementedbydeptid = implementedbydeptid;
	}


	public String getJoiningLetterPath() {
		return joiningLetterPath;
	}


	public void setJoiningLetterPath(String joiningLetterPath) {
		this.joiningLetterPath = joiningLetterPath;
	}


	public String getJoiningLetterDownloadUrl() {
		return joiningLetterDownloadUrl;
	}


	public void setJoiningLetterDownloadUrl(String joiningLetterDownloadUrl) {
		this.joiningLetterDownloadUrl = joiningLetterDownloadUrl;
	}
	
	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
}