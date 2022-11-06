package edu.stevens.cs548.clinic.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.stevens.cs548.clinic.util.DateUtils;

// TODO
@Entity
public class PhysiotherapyTreatment extends Treatment {

	private static final long serialVersionUID = 5602950140629148756L;
	
	// TODO (including order by date)
	@ElementCollection
	@Temporal(TemporalType.DATE)
	@OrderBy
	protected List<Date> treatmentDates;

	public void addTreatmentDate(LocalDate date) {
		treatmentDates.add(DateUtils.toDatabaseDate(date));
	}

	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		// TODO
		List<LocalDate> dates = new ArrayList<>();
		for(Date date: treatmentDates){
			dates.add(DateUtils.fromDatabaseDate(date));
		}
		return visitor.exportPhysiotherapy( treatmentId, 
											patient.getPatientId(),
											patient.getName(),
											provider.getProviderId(),
											provider.getName(),
											diagnosis, 
											dates, 
											() -> exportFollowupTreatments(visitor));
	}
	
	public PhysiotherapyTreatment() {
		treatmentDates = new ArrayList<>();
	}

}
