package com.didan.spring_account_transaction.service;

import com.didan.spring_account_transaction.dto.request.AccountBalanceRequestDTO;
import com.didan.spring_account_transaction.dto.request.AccountRequestDTO;
import com.didan.spring_account_transaction.dto.request.TransactionRequestDTO;
import com.didan.spring_account_transaction.dto.request.TransferRequestDTO;
import com.didan.spring_account_transaction.dto.response.AccountResponseDTO;
import com.didan.spring_account_transaction.entity.AccountEntity;
import com.didan.spring_account_transaction.exception.ResourceNotFound;
import com.didan.spring_account_transaction.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;

  public boolean verifyAccount(TransferRequestDTO requestDTO) {
    if (requestDTO.getType() == 1 || requestDTO.getType() == 2) {
      log.info("Verify account with number: {}", requestDTO.getSenderAccount());
      AccountEntity account = accountRepository.findAccountEntityByAccountNumber(requestDTO.getSenderAccount())
          .orElse(null);
      if (account == null) {
        log.info("Verify account failed");
        return false;
      } else return true;
    } else if (requestDTO.getType() == 2) {
      log.info("Verify account with number: {}", requestDTO.getReceiverAccount());
      AccountEntity account = accountRepository.findAccountEntityByAccountNumber(requestDTO.getReceiverAccount())
          .orElse(null);
      if (account == null) {
        log.info("Verify account failed");
        return false;
      } else return true;
    }
    return false;
  }

  public AccountResponseDTO createAccount(AccountRequestDTO requestDTO) {
    log.info("Create account with number: {}", requestDTO.getAccountNumber());
    AccountEntity account = new AccountEntity();
    account.setAccountNumber(requestDTO.getAccountNumber());
    account.setBalance(requestDTO.getBalance());
    account.setPinCode(requestDTO.getPinCode());
    account.setName(requestDTO.getName());
    accountRepository.save(account);
    return new AccountResponseDTO(account.getId(), account.getAccountNumber(), account.getName(), account.getBalance());
  }

  public boolean transfer(TransferRequestDTO requestDTO) {
    if (requestDTO.getType() == 1) {
      log.info("Deduct account sender: {}", requestDTO.getSenderAccount());
      AccountEntity account = accountRepository.findAccountEntityByAccountNumber(requestDTO.getSenderAccount())
          .orElse(null);
      if (account == null) {
        log.info("Deduct account failed");
        return false;
      } else {
        account.setBalance(account.getBalance() - Double.parseDouble(requestDTO.getAmount().toString()));
        accountRepository.save(account);
        return true;
      }
    } else if (requestDTO.getType() == 2) {
      log.info("Add balance account receiver: {}", requestDTO.getReceiverAccount());
      AccountEntity account = accountRepository.findAccountEntityByAccountNumber(requestDTO.getReceiverAccount())
          .orElse(null);
      if (account == null) {
        log.info("Add balance account failed");
        return false;
      } else {
        account.setBalance(account.getBalance() + Double.parseDouble(requestDTO.getAmount().toString()));
        accountRepository.save(account);
        return true;
      }
    } else {
      log.info("Transfer failed");
      throw new ResourceNotFound("Type transaction not found");
    }
  }

  public void revertSenderBalance(TransactionRequestDTO requestDTO) {
    log.info("Revert sender balance: {}", requestDTO.getSenderAccount());
    AccountEntity account = accountRepository.findAccountEntityByAccountNumber(requestDTO.getSenderAccount())
        .orElse(null);
    if (account == null) {
      log.info("Revert sender balance failed");
      throw new ResourceNotFound("Revert sender balance failed");
    }
    account.setBalance(account.getBalance() + Double.parseDouble(requestDTO.getAmount().toString()));
    accountRepository.save(account);
  }

  public AccountResponseDTO getBalance(AccountBalanceRequestDTO requestDTO) {
    log.info("Get balance account with number: {}", requestDTO.getAccountNumber());
    AccountEntity account = accountRepository.findAccountEntityByAccountNumber(requestDTO.getAccountNumber())
        .orElse(null);
    if (account == null) {
      log.info("Get balance failed");
      throw new ResourceNotFound("Get balance failed");
    }
    return new AccountResponseDTO(account.getId(), account.getAccountNumber(), account.getName(), account.getBalance());
  }
}
