package org.didan.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.didan.entity.AccountEntity;

@ApplicationScoped
public class AccountRepository implements PanacheRepositoryBase<AccountEntity, String> {

}
