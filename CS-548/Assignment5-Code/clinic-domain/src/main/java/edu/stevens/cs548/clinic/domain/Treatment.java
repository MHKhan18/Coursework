package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToMany;
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
			name="SearchTreatmentWithFollowupsByTreatmentId",
			query="select t from Treatment t left join fetch t.followupTreatments where t.treatmentId = :treatmentId"),
	@NamedQuery(
			name="CountTreatmentByTreatmentId",
			query="select count(t) from Treatment t where t.treatmentId = :treatmentId"),
	@NamedQuery(
		name = "RemoveAllTreatments", 
		query = "delete from Treatment t")
})

// TODO
@Entity
@Table(indexes = @Index(columnList="treatmentId"))
@Converter(name="uuidConverter", converterClass=UUIDConverter.class)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Treatment implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// TODO PK
	@Id
	@GeneratedValue
	protected long id;
	
	// TODO
	@Convert("uuidConverter")
	@Column(nullable=false,unique=true)
	protected UUID treatmentId;
	
	protected String diagnosis;
	
	
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
	protected Patient patient;

	public Patient getPatient() {
		return patient;
	}

	
	void setPatient(Patient patient) {
		this.patient = patient;
		// More logic in the domain model.
	}

	/*
	 * TODO
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROVIDER_FK", nullable = false)
	protected Provider provider;

	public Provider getProvider() {
		return provider;
	}	
	
	public void setProvider(Patient patient, Provider provider) {
		this.provider = provider;
		/*
		 * Make sure the provider also links back to this treatment.
		 */
		if (!provider.administers(this))
			provider.addTreatment(patient, this);
	}	
	
	/*
	 * TODO
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	protected Collection<Treatment> followupTreatments;
	
	public void addFollowupTreatment(Treatment t) {
		followupTreatments.add(t);
	}


	/*
	 * We use the visitor pattern to access a treatment.
	 */
	public abstract <T> T export(ITreatmentExporter<T> visitor);
	
	protected final <T> List<T> exportFollowupTreatments(ITreatmentExporter<T> visitor) {
		List<T> exports = new ArrayList<T>();
		for (Treatment t : followupTreatments) {
			exports.add(t.export(visitor));
		}
		return exports;
	}

	
	public Treatment() {
		super();
		/*
		 * TODO initialize lists
		 */
		followupTreatments = new ArrayList<>();
	}   
}
