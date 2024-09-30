package org.didan.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.didan.entity.PaymentEntity;

@ApplicationScoped
public class PaymentRepository implements PanacheRepositoryBase<PaymentEntity, Long> {

}
