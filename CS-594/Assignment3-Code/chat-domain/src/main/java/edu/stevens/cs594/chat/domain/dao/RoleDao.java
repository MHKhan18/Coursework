package edu.stevens.cs594.chat.domain.dao;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;


import edu.stevens.cs594.chat.domain.Role;
import edu.stevens.cs594.chat.domain.dao.ChatUserDomainProducer.ChatUserDomain;

@RequestScoped
public class RoleDao implements IRoleDao {

	@Inject
	@ChatUserDomain
	private EntityManager em;

	@Override
	public List<Role> getRoles() {
		TypedQuery<Role> query = em.createNamedQuery("SearchRoles", Role.class);
		return query.getResultList();
	}

	@Override
	public Role getRole(String rolename) {
		Role r = em.find(Role.class, rolename);
		if (r == null) {
			throw new IllegalArgumentException("Missing role "+rolename);
		} else {
			return r;
		}
	}

	@Override
	public void addRole(Role role) {
		em.persist(role);
	}

	@Override
	public void deleteRoles() {
		Query q = em.createNamedQuery("DeleteRoles");
		q.executeUpdate();
	}	

}
