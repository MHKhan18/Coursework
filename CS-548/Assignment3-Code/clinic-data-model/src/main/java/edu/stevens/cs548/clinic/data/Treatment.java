package edu.stevens.cs548.clinic.data;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;

/**
 * Entity implementation class for Entity: Treatment
 *
 */
@NamedQueries({
	@NamedQuery(
		name="SearchTreatmentByTreatmentId",
		query="select t from Treatment t where t.treatmentId = :treatmentId"),
	@NamedQuery(
			name="CountTreatmentByTreatmentId",
			query="select count(t) from Treatment t where t.treatmentId = :treatmentId"),
	@NamedQuery(
		name = "RemoveAllTreatments", 
		query = "delete from Treatment t")
})

// TODO
@Entity
@Table(
		name = "Treatment",
		indexes = @Index(columnList="treatmentId")
		)
@Converter(name="uuidConverter", converterClass=UUIDConverter.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// TODO PK
	@Id
	@GeneratedValue
	protected long id;
	
	@Convert("uuidConverter")
	@Column(nullable=false,unique=true)
	protected UUID treatmentId;
	
	protected String diagnosis;
	
	
	private String type;
	
	public String getType() {
		return type;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public UUID getTreatmentId() {
		return treatmentId;
	}

	public void setTreatmentId(UUID treatmentId) {
		this.treatmentId = treatmentId;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	/*
	 * TODO
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PATIENT_FK", nullable = false)
	private Patient patient;

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
		// More logic in the domain model.
	}

	/*
	 * TODO
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROVIDER_FK", nullable = false)
	private Provider provider;

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
		// More logic in the domain model.
	}	
	public Treatment(String type) {
		super();
		this.type = type;
		
	}
   
}
