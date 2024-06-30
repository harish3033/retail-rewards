package com.retail.rewards.util;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class PrimaryKeyGenerator implements IdentifierGenerator {

	/**
	 * Primary key generator while saving entity
	 */
	@Override
	public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
		try {
			return String.valueOf(Math.abs(System.nanoTime() + new Random().nextLong()));
		} catch (Exception e) {
			log.error("Exception while generating primary key", e);
			return String.valueOf(UUID.randomUUID().timestamp());
		}
	}

}
