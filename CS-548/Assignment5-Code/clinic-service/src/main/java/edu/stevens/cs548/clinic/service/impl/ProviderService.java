package edu.stevens.cs548.clinic.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import edu.stevens.cs548.clinic.domain.DrugTreatment;
import edu.stevens.cs548.clinic.domain.IPatientDao;
import edu.stevens.cs548.clinic.domain.IPatientDao.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDao;
import edu.stevens.cs548.clinic.domain.IProviderDao.ProviderExn;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDao.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentFactory;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PhysiotherapyTreatment;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.RadiologyTreatment;
import edu.stevens.cs548.clinic.domain.SurgeryTreatment;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.service.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.IProviderService;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.PhysiotherapyTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.RadiologyTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.SurgeryTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;

/**
 * CDI Bean implementation class ProviderService
 */
// TODO
@RequestScoped
public class ProviderService implements IProviderService {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderService.class.getCanonicalName());

	private IProviderFactory providerFactory;

	private ITreatmentFactory treatmentFactory;

	private ProviderDtoFactory providerDtoFactory;
	

	public ProviderService() {
		// Initialize factories
		providerFactory = new ProviderFactory();
		providerDtoFactory = new ProviderDtoFactory();
		treatmentFactory = new TreatmentFactory();
	}
	
	// TODO
	@Inject
	private IProviderDao providerDao;

	// TODO
	@Inject
	private IPatientDao patientDao;


	/**
	 * @see IProviderService#addProvider(ProviderDto dto)
	 */
	@Override
	public UUID addProvider(ProviderDto dto) throws ProviderServiceExn {
		// Use factory to create Provider entity, and persist with DAO
		try {
			Provider provider = providerFactory.createProvider();
			provider.setProviderId(UUID.randomUUID());
			provider.setName(dto.getName());
			provider.setNpi(dto.getNpi());
			providerDao.addProvider(provider);
			return provider.getProviderId();
		} catch (ProviderExn e) {
			throw new ProviderServiceExn("Failed to add provider", e);
		}
	}
	
	public List<ProviderDto> getProviders() throws ProviderServiceExn {
		List<ProviderDto> dtos = new ArrayList<ProviderDto>();
		Collection<Provider> providers = providerDao.getProviders();
		for (Provider provider : providers) {
			try {
				dtos.add(providerToDto(provider, false));
			} catch (TreatmentExn e) {
				throw new ProviderServiceExn("Failed to export treatment", e);
			}
		}
		return dtos;
	}

	/**
	 * @see IProviderService#getProvider(UUID)
	 */
	@Override
	public ProviderDto getProvider(UUID id) throws ProviderServiceExn {
		// TODO use DAO to get Provider by external key
		Provider provider;
		try{
			provider = providerDao.getProvider(id);
		}catch(ProviderExn e){
			throw new ProviderServiceExn("Failed to retrieve provider with id: " + id, e);
		}
		ProviderDto dto;
		try{
			dto = providerToDto(provider, true);
		}catch (TreatmentExn e) {
			throw new ProviderServiceExn("Failed to export treatment", e);
		}

		return dto;

	}
	
	private ProviderDto providerToDto(Provider provider, boolean includeTreatments) throws TreatmentExn {
		ProviderDto dto = providerDtoFactory.createProviderDto();
		dto.setId(provider.getProviderId());
		dto.setName(provider.getName());
		dto.setNpi(provider.getNpi());
		if (includeTreatments) {
			dto.setTreatments(provider.exportTreatments(TreatmentExporter.exportWithoutFollowups()));
		}
		return dto;
	}

	@Override
	public UUID addTreatment(TreatmentDto dto) throws PatientServiceExn, ProviderServiceExn {
		return addTreatmentImpl(dto).getTreatmentId();
	}

	private void addFollowupTreatments(Collection<TreatmentDto> treatmentDtos, Treatment treatment) throws PatientServiceExn, ProviderServiceExn{
		for (TreatmentDto treatmentDto: treatmentDtos){
			treatment.addFollowupTreatment(addTreatmentImpl(treatmentDto));
		}
	}

	private Treatment addTreatmentImpl(TreatmentDto dto) throws PatientServiceExn, ProviderServiceExn {
		try {
			Provider provider = providerDao.getProvider(dto.getProviderId());
			Patient patient = patientDao.getPatient(dto.getPatientId());
			if (dto instanceof DrugTreatmentDto) {
				DrugTreatmentDto drugTreatmentDto = (DrugTreatmentDto) dto;
				DrugTreatment drugTreatment = treatmentFactory.createDrugTreatment();
				drugTreatment.setTreatmentId(UUID.randomUUID());
				drugTreatment.setDiagnosis(drugTreatmentDto.getDiagnosis());
				// TODO fill in the rest of the fields
				drugTreatment.setDrug(drugTreatmentDto.getDrug());
				drugTreatment.setDosage(drugTreatmentDto.getDosage());
				drugTreatment.setStartDate(drugTreatmentDto.getStartDate());
				drugTreatment.setEndDate(drugTreatmentDto.getEndDate());
				drugTreatment.setFrequency(drugTreatmentDto.getFrequency());
				
				addFollowupTreatments(dto.getFollowupTreatments(), drugTreatment);
				drugTreatment.setProvider(patient, provider);
				return drugTreatment ;
			} else {
				/*
				 * TODO Handle the other cases
				 */
				if (dto instanceof PhysiotherapyTreatmentDto) {
					PhysiotherapyTreatmentDto physiotherapyTreatmentDto = (PhysiotherapyTreatmentDto)dto;
					PhysiotherapyTreatment physiotherapyTreatment = treatmentFactory.createPhysiotherapyTreatment();
					physiotherapyTreatment.setTreatmentId(UUID.randomUUID());
					physiotherapyTreatment.setDiagnosis(physiotherapyTreatmentDto.getDiagnosis());

					
					for (LocalDate date: physiotherapyTreatmentDto.getTreatmentDates()){
						physiotherapyTreatment.addTreatmentDate(date);
					}

					addFollowupTreatments(dto.getFollowupTreatments(), physiotherapyTreatment);
					physiotherapyTreatment.setProvider(patient, provider);
					return physiotherapyTreatment ;
				}

				else if (dto instanceof RadiologyTreatmentDto){
					RadiologyTreatmentDto radiologyTreatmentDto = (RadiologyTreatmentDto)dto;
					RadiologyTreatment radiologyTreatment = treatmentFactory.createRadiologyTreatment();
					radiologyTreatment.setTreatmentId(UUID.randomUUID());
					radiologyTreatment.setDiagnosis(radiologyTreatmentDto.getDiagnosis());
					
					for (LocalDate date: radiologyTreatmentDto.getTreatmentDates()){
						radiologyTreatment.addTreatmentDate(date);
					}

					addFollowupTreatments(dto.getFollowupTreatments(), radiologyTreatment);
					radiologyTreatment.setProvider(patient, provider);
					return radiologyTreatment ;
				}

				else if (dto instanceof SurgeryTreatmentDto){
					SurgeryTreatmentDto surgeryTreatmentDto = (SurgeryTreatmentDto)dto;
					SurgeryTreatment surgeryTreatment = treatmentFactory.createSurgeryTreatment();
					surgeryTreatment.setTreatmentId(UUID.randomUUID());
					surgeryTreatment.setDiagnosis(surgeryTreatmentDto.getDiagnosis());

					surgeryTreatment.setSurgeryDate(surgeryTreatmentDto.getSurgeryDate());
					surgeryTreatment.setDischargeInstructions(surgeryTreatmentDto.getDischargeInstructions());
					
					addFollowupTreatments(dto.getFollowupTreatments(), surgeryTreatment);
					surgeryTreatment.setProvider(patient, provider);
					return surgeryTreatment ;
				}

				throw new IllegalArgumentException("No treatment-specific info provided.");
			}

		} catch (PatientExn e) {
			throw new PatientNotFoundExn("Could not find patient for "+dto.getPatientId(), e);
		
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn("Could not find provider for "+dto.getProviderId(), e);
		}
	}

	@Override
	public TreatmentDto getTreatment(UUID providerId, UUID tid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn {
		// Export treatment DTO from Provider aggregate
		try {
			Provider provider = providerDao.getProvider(providerId);
			TreatmentDto treatment = provider.exportTreatment(tid, TreatmentExporter.exportWithoutFollowups());
			return treatment;

		} catch (TreatmentExn e) {
			throw new TreatmentNotFoundExn("Could not find treatment for "+tid, e);
		
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn("Could not find provider for "+providerId, e);
		}
	}


	@Override
	public void removeAll() throws ProviderServiceExn {
		providerDao.deleteProviders();
	}

}
