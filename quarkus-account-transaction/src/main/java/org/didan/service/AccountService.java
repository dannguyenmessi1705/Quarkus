package org.didan.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.didan.dto.request.AccountRequestDTO;
import org.didan.dto.request.TransactionRequestDto;
import org.didan.dto.request.TransferRequestDTO;
import org.didan.dto.response.AccountResponseDto;
import org.didan.entity.AccountEntity;
import org.didan.exception.ResourceNotFound;
import org.didan.repository.AccountRepository;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AccountService {
  @Inject
  AccountRepository accountRepository;

  public boolean verifyAccount(TransferRequestDTO requestDTO) {
    if (requestDTO.getType() == 1 || requestDTO.getType() == 3) {
      log.info("Verify account with account number: {}", requestDTO.getSenderAccount());
      AccountEntity account = accountRepository.find("accountNumber = ?1 and pinCode = ?2", requestDTO.getSenderAccount(), requestDTO.getPinCode()).firstResult();
      if (account == null) {
        log.info("Verify account failed");
        return false;
      } else return true;
    } else if (requestDTO.getType() == 2) {
      log.info("Verify account with account number: {}", requestDTO.getReceiverAccount());
      AccountEntity account = accountRepository.find("accountNumber = ?1", requestDTO.getReceiverAccount()).firstResult();
      if (account == null) {
        log.info("Verify account failed");
        return false;
      } else return true;
    }
    return false;
  }

  @Transactional
  public AccountResponseDto createAccount(AccountRequestDTO requestDto) {
    log.info("Create account with account number: {}", requestDto.getAccountNumber());
    AccountEntity account = new AccountEntity();
    account.setAccountNumber(requestDto.getAccountNumber());
    account.setPinCode(requestDto.getPinCode());
    account.setName(requestDto.getName());
    account.setBalance(requestDto.getBalance());
    accountRepository.persist(account);
    return new AccountResponseDto(account.getId(), account.getAccountNumber(), account.getName(), account.getBalance());
  }

  @Transactional
  public boolean transfer(TransferRequestDTO requestDTO) {
    if (requestDTO.getType() == 1) {
      log.info("Deduct account sender: {}", requestDTO.getSenderAccount());
      AccountEntity account = accountRepository.find("accountNumber = ?1", requestDTO.getSenderAccount()).firstResult();
      if (account == null) {
        log.info("Deduct account failed");
        return false;
      }
      account.setBalance(account.getBalance() - Double.parseDouble(requestDTO.getAmount().toString()));
      accountRepository.persist(account);
      return true;
    } else if (requestDTO.getType() == 2) {
      log.info("Add balance account receiver: {}", requestDTO.getReceiverAccount());
      AccountEntity account = accountRepository.find("accountNumber = ?1", requestDTO.getReceiverAccount()).firstResult();
      if (account == null) {
        log.info("Add balance account failed");
        return false;
      }
      account.setBalance(account.getBalance() + Double.parseDouble(requestDTO.getAmount().toString()));
      accountRepository.persist(account);
      return true;
    } else {
      log.info("Transfer failed");
      throw new ResourceNotFound("Type transaction not found");
    }
  }

  @Transactional
  public void revertSenderBalance(TransactionRequestDto requestDTO) {
    log.info("Revert sender balance: {}", requestDTO.getSenderAccount());
    AccountEntity account = accountRepository.find("accountNumber = ?1", requestDTO.getSenderAccount()).firstResult();
    if (account == null) {
      log.info("Revert sender balance failed");
      throw new ResourceNotFound("Revert sender balance failed");
    }
    account.setBalance(account.getBalance() + Double.parseDouble(requestDTO.getAmount().toString()));
    accountRepository.persist(account);
  }

  public AccountResponseDto getBalance(AccountRequestDTO requestDto) {
    log.info("Get balance with account number: {}", requestDto.getAccountNumber());
    AccountEntity account = accountRepository.find("accountNumber = ?1 and pinCode = ?2", requestDto.getAccountNumber(), requestDto.getPinCode()).firstResult();
    if (account == null) {
      log.info("Get balance failed");
      throw new ResourceNotFound("Get balance failed");
    }
    return new AccountResponseDto(account.getId(), account.getAccountNumber(), account.getName(), account.getBalance());
  }
}
