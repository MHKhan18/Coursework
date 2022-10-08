package edu.stevens.cs548.clinic.service.init;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.inject.Singleton;

import edu.stevens.cs548.clinic.domain.DrugTreatment;
import edu.stevens.cs548.clinic.domain.IPatientDao;
import edu.stevens.cs548.clinic.domain.IProviderDao;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;

@Singleton
@LocalBean
@Startup
public class InitBean implements ITreatmentExporter<Void>{

	private static final Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());
	
	private static final ZoneId ZONE_ID = ZoneOffset.UTC;
	
	private PatientFactory patientFactory = new PatientFactory();
	
	private ProviderFactory providerFactory = new ProviderFactory();
	
	private TreatmentFactory treatmentFactory = new TreatmentFactory();

	// TODO 
	private IPatientDao patientDao;
	
	// TODO
	private IProviderDao providerDao;
	
        // TODO
	private void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		logger.info("Your name here: ");

		try {
			
			/*   
			 * Clear the database and populate with fresh data.
			 * 
			 * If we ensure that deletion of patients cascades deletes of treatments,
			 * then we only need to delete patients.
			 */
			
			patientDao.deletePatients();
			providerDao.deleteProviders();
			
			Patient john = patientFactory.createPatient();
			john.setPatientId(UUID.randomUUID());
			john.setName("John Doe");
			john.setDob(LocalDate.parse("1995-08-15"));
			patientDao.addPatient(john);
			
			Provider jane = providerFactory.createProvider();
			jane.setProviderId(UUID.randomUUID());
			jane.setName("Jane Doe");
			jane.setNpi("1234");
			providerDao.addProvider(jane);
			
			DrugTreatment drug01 = treatmentFactory.createDrugTreatment();
			drug01.setDiagnosis("Headache");
			drug01.setDrug("Aspirin");
			drug01.setStartDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			drug01.setEndDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			
			jane.addTreatment(john, drug01);
			
			
			// TODO add more testing, including treatments and providers
			
			
			// Now show in the logs what has been added
			
			Collection<Patient> patients = patientDao.getPatients();
			for (Patient p : patients) {
				String dob = p.getDob().toString();
				logger.info(String.format("Patient %s, ID %s, DOB %s", p.getName(), p.getPatientId().toString(), dob));
				p.exportTreatments(this);
			}
			
			Collection<Provider> providers = providerDao.getProviders();
			for (Provider p : providers) {
				logger.info(String.format("Provider %s, ID %s", p.getName(), p.getProviderId().toString()));
				p.exportTreatments(this);
			}
			
		} catch (Exception e) {
;
			throw new IllegalStateException("Failed to add record.", e);
			
		} 
			
	}
	
	@Override
	public Void exportDrugTreatment(UUID tid, UUID patientId, UUID providerId, String diagnosis, String drug,
			float dosage, LocalDate start, LocalDate end, int frequency, Collection<Void> followups) {
		logger.info(String.format("...Drug treatment %s, drug %s", tid.toString(), drug));
		return null;
	}

	@Override
	public Void exportRadiology(UUID tid, UUID patientId, UUID providerId, String diagnosis, List<LocalDate> dates,
			Collection<Void> followups) {
		logger.info(String.format("...Radiology treatment %s", tid.toString()));
		return null;
	}

	@Override
	public Void exportSurgery(UUID tid, UUID patientId, UUID providerId, String diagnosis, LocalDate date,
			Collection<Void> followups) {
		logger.info(String.format("...Surgery treatment %s", tid.toString()));
		return null;
	}

	@Override
	public Void exportPhysiotherapy(UUID tid, UUID patientId, UUID providerId, String diagnosis, List<LocalDate> dates, Collection<Void> followups) {
		logger.info(String.format("...Physiotherapy treatment %s", tid.toString()));
		return null;
	}


}
