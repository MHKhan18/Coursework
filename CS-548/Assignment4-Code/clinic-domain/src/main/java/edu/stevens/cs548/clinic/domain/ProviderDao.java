package edu.stevens.cs548.clinic.domain;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

// TODO
public class ProviderDao implements IProviderDao {

	// TODO
	private EntityManager em;
	
	// TODO
	private ITreatmentDao treatmentDao;

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderDao.class.getCanonicalName());

	@Override
	public void addProvider(Provider provider) throws ProviderExn {
		/*
		 * TODO add to database (and sync with database to generate primary key).
		 * Don't forget to initialize the Provider aggregate with a treatment DAO.
		 */

	}

	@Override
	public Provider getProvider(UUID id) throws ProviderExn {
		/*
		 * TODO retrieve Provider using external key
		 */
		return null;
	}
	
	@Override
	public List<Provider> getProviders() {
		/*
		 * TODO Return a list of all providers (remember to set treatmentDAO)
		 */

		return null;
	}
	
	@Override
	public void deleteProviders() {
		Query update = em.createNamedQuery("RemoveAllTreatments");
		update.executeUpdate();
		update = em.createNamedQuery("RemoveAllProviders");
		update.executeUpdate();
	}

}
