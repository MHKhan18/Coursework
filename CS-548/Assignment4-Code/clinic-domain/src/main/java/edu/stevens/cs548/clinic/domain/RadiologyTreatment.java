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

//TODO JPA annotations
@Entity
public class RadiologyTreatment extends Treatment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3656673416179492428L;

	// TODO  including order by date
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
		return visitor.exportRadiology(
							treatmentId, 
							patient.getPatientId(), 
							provider.getProviderId(), 
							diagnosis, 
							DateUtils.fromDatabaseDates(treatmentDates), 
							exportFollowupTreatments(visitor)
							);
	}
	
	public RadiologyTreatment() {
		super();
		treatmentDates = new ArrayList<>();
	}
	
}
