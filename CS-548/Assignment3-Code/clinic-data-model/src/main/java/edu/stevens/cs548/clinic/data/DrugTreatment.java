package edu.stevens.cs548.clinic.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: DrugTreatment
 */
//TODO
@Entity
@Table(name = "DrugTreatment")
@DiscriminatorValue("DRUGTREATMENT")
public class DrugTreatment extends Treatment implements Serializable {

	private static final long serialVersionUID = 1L;

	private String drug;
	
	private float dosage;
	
	// TODO
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	// TODO
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	private int frequency;

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public float getDosage() {
		return dosage;
	}

	public void setDosage(float dosage) {
		this.dosage = dosage;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DrugTreatment() {
		super("DRUGTREATMENT");
	}

}
