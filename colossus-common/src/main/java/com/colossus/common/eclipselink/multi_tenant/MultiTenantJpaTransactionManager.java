package com.colossus.common.eclipselink.multi_tenant;

import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class MultiTenantJpaTransactionManager  extends JpaTransactionManager{

	private static final long serialVersionUID = 540073346739578316L;

	@Override
	protected void doBegin(final Object transaction, final TransactionDefinition definition) {
		super.doBegin(transaction, definition);
		final EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager
				.getResource(getEntityManagerFactory());
		final EntityManager em = emHolder.getEntityManager();
		final Serializable tenantId = TenantHolder.getTenant();
		if (tenantId != null) {
			em.setProperty(TenantHolder.TENANT_ID, tenantId);
		} 
		
	}
	
}
