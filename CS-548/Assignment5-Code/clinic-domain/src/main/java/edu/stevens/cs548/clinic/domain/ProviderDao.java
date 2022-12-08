package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import edu.stevens.cs548.clinic.domain.ClinicDomainProducer.ClinicDomain;

// TODO
@RequestScoped
public class ProviderDao implements IProviderDao {

	// TODO
	@Inject @ClinicDomain
	private EntityManager em;
	
	// TODO
	@Inject
	private ITreatmentDao treatmentDao;

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderDao.class.getCanonicalName());

	@Override
	public void addProvider(Provider provider) throws ProviderExn {
		/*
		 * TODO add to database (and sync with database to generate primary key).
		 * Don't forget to initialize the Provider aggregate with a treatment DAO.
		 */
		UUID pid = provider.getProviderId();
		Query query = em.createNamedQuery("CountProviderByProviderId").setParameter("providerId", pid);
		Long numExisting = (Long) query.getSingleResult();

		if (numExisting < 1){
			em.persist(provider);
			provider.setTreatmentDao(treatmentDao);
		}
		else{
			throw new ProviderExn("Insertion: Provider with provider id (" + pid + ") already exists.");
		}
	}

	@Override
	public Provider getProvider(UUID id) throws ProviderExn {
		/*
		 * TODO retrieve Provider using external key
		 */
		TypedQuery<Provider> query = em.createNamedQuery("SearchProviderByProviderId", Provider.class).setParameter("providerId", id);
		List<Provider> providers = query.getResultList();

		if (providers.size() > 1){
			throw new ProviderExn("Duplicate provider records: provider id = " + id);
		}
		else if (providers.size() < 1){
			throw new ProviderExn("Provider not found: provider id = " + id);
		}
		else{
			Provider p = providers.get(0);
			p.setTreatmentDao(treatmentDao);
			return p;
		}
	}
	
	@Override
	public List<Provider> getProviders() {
		/*
		 * TODO Return a list of all providers (remember to set treatmentDAO)
		 */
		TypedQuery<Provider> query = em.createNamedQuery("SearchAllProviders", Provider.class);
		List<Provider> providers = query.getResultList();

		for(Provider p: providers){
			p.setTreatmentDao(treatmentDao);
		}
		return providers;
	}
	
	@Override
	public void deleteProviders() {
		Query update = em.createNamedQuery("RemoveAllTreatments");
		update.executeUpdate();
		update = em.createNamedQuery("RemoveAllProviders");
		update.executeUpdate();
	}

}
