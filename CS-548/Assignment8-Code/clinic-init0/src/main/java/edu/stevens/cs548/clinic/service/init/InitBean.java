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
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import edu.stevens.cs548.clinic.domain.DrugTreatment;
import edu.stevens.cs548.clinic.domain.IPatientDao;
import edu.stevens.cs548.clinic.domain.IProviderDao;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.PhysiotherapyTreatment;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.RadiologyTreatment;
import edu.stevens.cs548.clinic.domain.SurgeryTreatment;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;

 @Singleton
 @LocalBean
 @Startup
// @ApplicationScoped
// @Transactional
public class InitBean implements ITreatmentExporter<Void>{

	private static final Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());
	
	private static final ZoneId ZONE_ID = ZoneOffset.UTC;
	
	private PatientFactory patientFactory = new PatientFactory();
	
	private ProviderFactory providerFactory = new ProviderFactory();
	
	private TreatmentFactory treatmentFactory = new TreatmentFactory();

	// TODO 
	@Inject
	private IPatientDao patientDao;
	
	// TODO
	@Inject
	private IProviderDao providerDao;
	
	/*
	 * Initialize using EJB logic
	 */
	@PostConstruct
	/*
	 * This should work to initialize with CDI bean, but there is a bug in Payara.....
	 */
	// public void init(@Observes @Initialized(ApplicationScoped.class) ServletContext init) {
	public void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		logger.info("Your name here: ");
		System.err.println("Your name here!");

		try {
			
			/*   
			 * Clear the database and populate with fresh data.
			 * 
			 * If we ensure that deletion of patients cascades deletes of treatments,
			 * then we only need to delete patients.
			 */
			
			providerDao.deleteProviders();
			patientDao.deletePatients();
			
			
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
			drug01.setTreatmentId(UUID.randomUUID());
			drug01.setDiagnosis("Headache");
			drug01.setDrug("Aspirin");
			drug01.setStartDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			drug01.setEndDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			drug01.setProvider(john, jane);
			jane.addTreatment(john, drug01);
			
			
			// TODO add more testing, including treatments and providers

			Patient patient1 = patientFactory.createPatient();
			patient1.setPatientId(UUID.randomUUID());
			patient1.setName("Patient No.1");
			patient1.setDob(LocalDate.parse("1998-05-15"));
			patientDao.addPatient(patient1);

			Patient patient2 = patientFactory.createPatient();
			patient2.setPatientId(UUID.randomUUID());
			patient2.setName("Patient No.2");
			patient2.setDob(LocalDate.parse("2003-04-27"));
			patientDao.addPatient(patient2);

			Patient patient3 = patientFactory.createPatient();
			patient3.setPatientId(UUID.randomUUID());
			patient3.setName("Patient No.3");
			patient3.setDob(LocalDate.parse("2005-10-12"));
			patientDao.addPatient(patient3);

			Provider provider1 = providerFactory.createProvider();
			provider1.setProviderId(UUID.randomUUID());
			provider1.setName("Provider No.1");
			provider1.setNpi("1234");
			providerDao.addProvider(provider1);

			Provider provider2 = providerFactory.createProvider();
			provider2.setProviderId(UUID.randomUUID());
			provider2.setName("Provider No.2");
			provider2.setNpi("2345");
			providerDao.addProvider(provider2);

			Provider provider3 = providerFactory.createProvider();
			provider3.setProviderId(UUID.randomUUID());
			provider3.setName("Provider No.3");
			provider3.setNpi("3456");
			providerDao.addProvider(provider3);

			DrugTreatment drug02 = treatmentFactory.createDrugTreatment();
			drug02.setTreatmentId(UUID.randomUUID());
			drug02.setDiagnosis("Common Cold");
			drug02.setDrug("Tenoryl");
			drug02.setStartDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			drug02.setEndDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			drug02.setProvider(patient1, jane);
			jane.addTreatment(patient1, drug02);

			RadiologyTreatment drug03 = treatmentFactory.createRadiologyTreatment();
			drug03.setTreatmentId(UUID.randomUUID());
			drug03.setDiagnosis("Skin Cancer");
			drug03.setProvider(patient1, jane);
			jane.addTreatment(patient1, drug03);

			SurgeryTreatment drug04 = treatmentFactory.createSurgeryTreatment();
			drug04.setTreatmentId(UUID.randomUUID());
			drug04.setDiagnosis("Knee Fracture");
			drug04.setSurgeryDate(LocalDate.parse("2022-06-21"));
			drug04.setDischargeInstructions("Minimal movement for 15 days.");
			drug04.setProvider(patient2, provider1);
			provider1.addTreatment(patient2, drug04);

			PhysiotherapyTreatment drug05 = treatmentFactory.createPhysiotherapyTreatment();
			drug05.setTreatmentId(UUID.randomUUID());
			drug05.setDiagnosis("Neck pain");
			drug05.setProvider(patient3, provider2);
			provider2.addTreatment(patient3, drug05);

			
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

			logger.info("====testing a single treatment for a patient (john)===");
			john.exportTreatment(drug01.getTreatmentId(), this);
			
			logger.info("======testing a single treatment for a provider (provider 1)========");
			provider1.exportTreatment(drug04.getTreatmentId(), this);


			
		} catch (Exception e) {
;
			throw new IllegalStateException("Failed to add record.", e);
			
		} 
			
	}
	
//	public void shutdown(@Observes @Destroyed(ApplicationScoped.class) ServletContext init) {
//		logger.info("App shutting down....");
//	}
	
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
