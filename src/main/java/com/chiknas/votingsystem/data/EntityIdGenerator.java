package com.chiknas.votingsystem.data;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

/**
 * com.chiknas.votingsystem.data.IdGenerator, created on 06/11/2019 12:25 <p>
 * @author NikolaosK
 */
public class EntityIdGenerator implements IdentifierGenerator {

  public static final String generatorName = "IdGenerator";

  @Override
  public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object object) throws HibernateException {
    return (long) (100000 + new Random().nextInt(900000));
  }
}
