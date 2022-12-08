package edu.stevens.cs548.clinic.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

// TODO
@Entity
@Table(name = "RadiologyTreatment")
@DiscriminatorValue("RADIOLOGY")
public class RadiologyTreatment extends Treatment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3656673416179492428L;

	// TODO 
	@ElementCollection
	@Temporal(TemporalType.DATE)
	protected Collection<Date> treatmentDates;
	
	// TODO
	@OneToMany(cascade = CascadeType.ALL, 
			   fetch = FetchType.EAGER)
	protected Collection<Treatment> followupTreatments;

	public Collection<Date> getTreatmentDates() {
		return treatmentDates;
	}

	public void setTreatmentDates(Collection<Date> treatmentDates) {
		this.treatmentDates = treatmentDates;
	}

	public Collection<Treatment> getFollowupTreatments() {
		return followupTreatments;
	}

	public void setFollowupTreatments(Collection<Treatment> followupTreatments) {
		this.followupTreatments = followupTreatments;
	}
	
	public RadiologyTreatment() {
		// TODO
		super("RADIOLOGY");
		this.treatmentDates = new ArrayList<>();
		this.followupTreatments = new ArrayList<>();
		
	}
	
}
