package edu.stevens.cs548.clinic.service.init;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import edu.stevens.cs548.clinic.service.IPatientService;
import edu.stevens.cs548.clinic.service.IProviderService;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.PhysiotherapyTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.RadiologyTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.SurgeryTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDtoFactory;

@Singleton
@LocalBean
@Startup
// @ApplicationScoped
// @Transactional
public class InitBean {

	private static final Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());

	private static final ZoneId ZONE_ID = ZoneOffset.UTC;

	private PatientDtoFactory patientFactory = new PatientDtoFactory();

	private ProviderDtoFactory providerFactory = new ProviderDtoFactory();

	private TreatmentDtoFactory treatmentFactory = new TreatmentDtoFactory();

	// TODO
	@Inject
	private IPatientService patientService;

	// TODO
	@Inject
	private IProviderService providerService;

	/*
	 * Initialize using EJB logic
	 */
	@PostConstruct
	/*
	 * This should work to initialize with CDI bean, but there is a bug in
	 * Payara.....
	 */
	// public void init(@Observes @Initialized(ApplicationScoped.class) ServletContext init) {
	public void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the
		 * server logs.
		 */
		logger.info("Your name here: ");
		System.err.println("Your name here!");

		try {

			/*
			 * Clear the database and populate with fresh data.
			 * 
			 * Note that the service generates the external ids, when adding the entities.
			 */

			providerService.removeAll();
			patientService.removeAll();

			PatientDto john = patientFactory.createPatientDto();
			john.setName("John Doe");
			john.setDob(LocalDate.parse("1995-08-15"));
			john.setId(patientService.addPatient(john));

			ProviderDto jane = providerFactory.createProviderDto();
			jane.setName("Jane Doe");
			jane.setNpi("1234");
			jane.setId(providerService.addProvider(jane));

			DrugTreatmentDto drug01 = treatmentFactory.createDrugTreatmentDto();
			drug01.setPatientId(john.getId());
			drug01.setPatientName(john.getName());
			drug01.setProviderId(jane.getId());
			drug01.setProviderName(jane.getName());
			drug01.setDiagnosis("Headache");
			drug01.setDrug("Aspirin");
			drug01.setDosage(20);
			drug01.setFrequency(7);
			drug01.setStartDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			drug01.setEndDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));

			drug01.setId(providerService.addTreatment(drug01));

			// TODO add more testing, including treatments and providers

			//================patients ====================
			PatientDto russel = patientFactory.createPatientDto();
			russel.setName("Andrew Russel");
			russel.setDob(LocalDate.parse("1997-05-15"));
			russel.setId(patientService.addPatient(russel));

			PatientDto stokes = patientFactory.createPatientDto();
			stokes.setName("Ben Stokes");
			stokes.setDob(LocalDate.parse("2003-04-23"));
			stokes.setId(patientService.addPatient(stokes));

			// ================== providers =================
			ProviderDto provider2 = providerFactory.createProviderDto();
			provider2.setName("Provider2 LLC");
			provider2.setNpi("7789");
			provider2.setId(providerService.addProvider(provider2));

			ProviderDto provider3 = providerFactory.createProviderDto();
			provider3.setName("Provider3 Company");
			provider3.setNpi("5634");
			provider3.setId(providerService.addProvider(provider3));

			// ============= treatments ==================
			PhysiotherapyTreatmentDto therapy01 = treatmentFactory.createPhysiotherapyTreatmentDto();
			therapy01.setPatientId(john.getId());
			therapy01.setPatientName(john.getName());
			therapy01.setProviderId(jane.getId());
			therapy01.setProviderName(jane.getName());
			therapy01.setDiagnosis("Neck Pain");
			List<LocalDate> treatmentDates = new ArrayList<>();
			treatmentDates.add(LocalDate.parse("2022-11-07"));
			treatmentDates.add(LocalDate.parse("2022-11-17"));
			treatmentDates.add(LocalDate.parse("2022-11-27"));
			therapy01.setTreatmentDates(treatmentDates);
			therapy01.setId(providerService.addTreatment(therapy01));

			RadiologyTreatmentDto rad01 = treatmentFactory.createRadiologyTreatmentDto();
			rad01.setPatientId(russel.getId());
			rad01.setPatientName(russel.getName());
			rad01.setProviderId(provider2.getId());
			rad01.setProviderName(provider2.getName());
			rad01.setDiagnosis("Skin Cancer");
			List<LocalDate> treatmentDates2 = new ArrayList<>();
			treatmentDates.add(LocalDate.parse("2022-12-07"));
			treatmentDates.add(LocalDate.parse("2022-12-17"));
			treatmentDates.add(LocalDate.parse("2022-12-27"));
			rad01.setTreatmentDates(treatmentDates2);
			Collection<TreatmentDto> followupTreatments = new ArrayList<>();
			followupTreatments.add(drug01);
			followupTreatments.add(therapy01);
			rad01.setFollowupTreatments(followupTreatments);
			rad01.setId(providerService.addTreatment(rad01));

			SurgeryTreatmentDto sur01 = treatmentFactory.createSurgeryTreatmentDto();
			sur01.setPatientId(russel.getId());
			sur01.setPatientName(russel.getName());
			sur01.setProviderId(provider2.getId());
			sur01.setProviderName(provider2.getName());
			sur01.setDiagnosis("Broken Feet");
			sur01.setSurgeryDate(LocalDate.parse("2022-12-19"));
			sur01.setDischargeInstructions("Take 2 weeks rest");
			sur01.setId(providerService.addTreatment(sur01));

			SurgeryTreatmentDto sur02 = treatmentFactory.createSurgeryTreatmentDto();
			sur02.setPatientId(stokes.getId());
			sur02.setPatientName(stokes.getName());
			sur02.setProviderId(provider3.getId());
			sur02.setProviderName(provider3.getName());
			sur02.setDiagnosis("Knee Fracture");
			sur02.setSurgeryDate(LocalDate.parse("2022-11-20"));
			sur02.setDischargeInstructions("Take 4 weeks rest");
			Collection<TreatmentDto> followupTreatments2 = new ArrayList<>();
			followupTreatments2.add(therapy01);
			followupTreatments2.add(sur01);
			rad01.setFollowupTreatments(followupTreatments2);
			sur02.setId(providerService.addTreatment(sur02));

			SurgeryTreatmentDto sur03 = treatmentFactory.createSurgeryTreatmentDto();
			sur03.setPatientId(stokes.getId());
			sur03.setPatientName(stokes.getName());
			sur03.setProviderId(provider3.getId());
			sur03.setProviderName(provider3.getName());
			sur03.setDiagnosis("Dislocated joint");
			sur03.setSurgeryDate(LocalDate.parse("2022-12-05"));
			sur03.setDischargeInstructions("Take 5 weeks rest");
			Collection<TreatmentDto> followupTreatments3 = new ArrayList<>();
			followupTreatments3.add(drug01);
			followupTreatments3.add(sur01);
			rad01.setFollowupTreatments(followupTreatments3);
			sur03.setId(providerService.addTreatment(sur03));

			

			// Now show in the logs what has been added

			Collection<PatientDto> patients = patientService.getPatients();
			for (PatientDto p : patients) {
				logger.info(String.format("Patient %s, ID %s, DOB %s", p.getName(), p.getId().toString(),
						p.getDob().toString()));
				logTreatments(p.getTreatments());
			}

			Collection<ProviderDto> providers = providerService.getProviders();
			for (ProviderDto p : providers) {
				logger.info(String.format("Provider %s, ID %s, NPI %s", p.getName(), p.getId().toString(), p.getNpi()));
				logTreatments(p.getTreatments());
			}

		} catch (Exception e) {
			;
			throw new IllegalStateException("Failed to add record.", e);

		}
		
	}

	public void shutdown(@Observes @Destroyed(ApplicationScoped.class) ServletContext init) {
		logger.info("App shutting down....");
	}

	private void logTreatments(Collection<TreatmentDto> treatments) {
		for (TreatmentDto treatment : treatments) {
			if (treatment instanceof DrugTreatmentDto) {
				logTreatment((DrugTreatmentDto) treatment);
			} else if (treatment instanceof SurgeryTreatmentDto) {
				logTreatment((SurgeryTreatmentDto) treatment);
			} else if (treatment instanceof RadiologyTreatmentDto) {
				logTreatment((RadiologyTreatmentDto) treatment);
			} else if (treatment instanceof PhysiotherapyTreatmentDto) {
				logTreatment((PhysiotherapyTreatmentDto) treatment);
			}
			if (!treatment.getFollowupTreatments().isEmpty()) {
				logger.info("============= Follow-up Treatments");
				logTreatments(treatment.getFollowupTreatments());
				logger.info("============= End Follow-up Treatments");
			}
		}
	}

	private void logTreatment(DrugTreatmentDto t) {
		logger.info(String.format("...Drug treatment %s, drug %s", t.getId().toString(), t.getDrug()));
	}

	private void logTreatment(RadiologyTreatmentDto t) {
		logger.info(String.format("...Radiology treatment %s", t.getId().toString()));
	}

	private void logTreatment(SurgeryTreatmentDto t) {
		logger.info(String.format("...Surgery treatment %s", t.getId().toString()));
	}

	private void logTreatment(PhysiotherapyTreatmentDto t) {
		logger.info(String.format("...Physiotherapy treatment %s", t.getId().toString()));
	}

}
