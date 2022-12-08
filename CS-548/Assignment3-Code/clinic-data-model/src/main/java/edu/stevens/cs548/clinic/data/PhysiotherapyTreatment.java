package edu.stevens.cs548.clinic.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

// TODO
@Entity
@Table(name = "PhysiotherapyTreatment")
@DiscriminatorValue("PHYSIOTHERAPY")
public class PhysiotherapyTreatment extends Treatment {

	private static final long serialVersionUID = 5602950140629148756L;
	
	// TODO 
	@ElementCollection
	@Temporal(TemporalType.DATE)
	protected Collection<Date> treatmentDates;

	public Collection<Date> getTreatmentDates() {
		return treatmentDates;
	}

	public void setTreatmentDates(Collection<Date> treatmentDates) {
		this.treatmentDates = treatmentDates;
	}
	
	public PhysiotherapyTreatment() {
		// TODO
		super("PHYSIOTHERAPY");
		this.treatmentDates = new ArrayList<>();
	}

}
