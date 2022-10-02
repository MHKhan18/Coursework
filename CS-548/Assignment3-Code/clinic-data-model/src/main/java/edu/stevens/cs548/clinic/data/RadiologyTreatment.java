package edu.stevens.cs548.clinic.data;

import java.util.Collection;
import java.util.Date;

// TODO
public class RadiologyTreatment extends Treatment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3656673416179492428L;

	// TODO 
	protected Collection<Date> treatmentDates;
	
	// TODO
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
	}
	
}
