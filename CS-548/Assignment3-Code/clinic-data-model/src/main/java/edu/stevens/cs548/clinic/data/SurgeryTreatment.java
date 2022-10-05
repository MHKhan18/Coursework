package edu.stevens.cs548.clinic.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

// TODO
@Entity
@Table(name = "SurgeryTreatment")
@DiscriminatorValue("SURGERY")
public class SurgeryTreatment extends Treatment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4173146640306267418L;
	
	// TODO
	@Temporal(TemporalType.DATE)
	private Date surgeryDate;
	
	private String dischargeInstructions;
	
	// TODO
	@OneToMany(cascade = CascadeType.ALL, 
			   fetch = FetchType.EAGER)
	private Collection<Treatment> followupTreatments;

	public Date getSurgeryDate() {
		return surgeryDate;
	}

	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	public String getDischargeInstructions() {
		return dischargeInstructions;
	}

	public void setDischargeInstructions(String dischargeInstructions) {
		this.dischargeInstructions = dischargeInstructions;
	}

	public Collection<Treatment> getFollowupTreatments() {
		return followupTreatments;
	}

	public void setFollowupTreatments(Collection<Treatment> followupTreatments) {
		this.followupTreatments = followupTreatments;
	}
	
	public SurgeryTreatment() {
		// TODO
		super("SURGERY");
		this.followupTreatments = new ArrayList<>();
	}

}
