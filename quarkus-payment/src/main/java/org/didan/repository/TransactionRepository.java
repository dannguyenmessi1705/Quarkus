package org.didan.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.didan.entity.TransactionEntity;

@ApplicationScoped
public class TransactionRepository implements PanacheRepositoryBase<TransactionEntity, Integer> {

}
