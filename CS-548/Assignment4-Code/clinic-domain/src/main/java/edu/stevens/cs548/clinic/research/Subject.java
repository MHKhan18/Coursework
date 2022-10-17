package edu.stevens.cs548.clinic.research;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

import edu.stevens.cs548.clinic.domain.UUIDConverter;

/**
 * Entity implementation class for Entity: Subject
 */
// TODO
@Entity
@Table(indexes = @Index(columnList="patientId"))
@Converter(name="uuidConverter", converterClass=UUIDConverter.class)
public class Subject implements Serializable {

	
	private static final long serialVersionUID = 1L;

	// TODO
	@Id
	@GeneratedValue
	private long id;
	
	/*
	 * This will be same as patient id in Clinic database
	 */
	// TODO
	@Convert("uuidConverter")
	@Column(nullable=false,unique=true)
	private UUID patientId;
		
	/*
	 * Anonymize patient (randomly generated when assigned)
	 */
	private long subjectId;
	
	// TODO
	@OneToMany(cascade = CascadeType.ALL, 
			mappedBy = "subject",
			fetch = FetchType.EAGER)
	private Collection<DrugTreatmentRecord> treatments;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UUID getPatientId() {
		return patientId;
	}

	public void setPatientId(UUID patientId) {
		this.patientId = patientId;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public Collection<DrugTreatmentRecord> getTreatments() {
		return treatments;
	}
	
	public void addTreatment(DrugTreatmentRecord treatment) {
		this.treatments.add(treatment);
	}
	
	public Subject() {
		treatments = new ArrayList<>();
	}
	
   
}
