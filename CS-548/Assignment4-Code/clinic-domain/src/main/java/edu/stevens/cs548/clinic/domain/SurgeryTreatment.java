package edu.stevens.cs548.clinic.domain;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.stevens.cs548.clinic.util.DateUtils;

//TODO JPA annotations
@Entity
public class SurgeryTreatment extends Treatment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4173146640306267418L;
	
	//TODO annotations
	@Temporal(TemporalType.DATE)
	private Date surgeryDate;
	
	private String dischargeInstructions;
	
	// TODO
	@OneToMany(cascade = CascadeType.ALL, 
			   fetch = FetchType.EAGER)
	private Collection<Treatment> followupTreatments;

	public LocalDate getSurgeryDate() {
		return DateUtils.fromDatabaseDate(surgeryDate);
	}

	public void setSurgeryDate(LocalDate surgeryDate) {
		this.surgeryDate = DateUtils.toDatabaseDate(surgeryDate);
	}

	public String getDischargeInstructions() {
		return dischargeInstructions;
	}

	public void setDischargeInstructions(String dischargeInstructions) {
		this.dischargeInstructions = dischargeInstructions;
	}
	
	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		// TODO
		return visitor.exportSurgery(
						treatmentId, 
						patient.getPatientId(), 
						provider.getProviderId(), 
						diagnosis, 
						getSurgeryDate(), 
						exportFollowupTreatments(visitor)
					);
	}
	
	public SurgeryTreatment() {
		super();
	}

}
