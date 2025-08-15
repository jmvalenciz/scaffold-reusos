package co.com.bancolombia.usecase.createnewtransaction;

import co.com.bancolombia.model.account.Account;
import co.com.bancolombia.model.account.gateways.AccountService;
import co.com.bancolombia.model.naturalpersondata.NaturalPersonData;
import co.com.bancolombia.model.naturalpersondata.gateways.NaturalPersonDataService;
import co.com.bancolombia.model.transaction.NewTransaction;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionService;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.model.exceptions.BusinessExceptionMessages;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RequiredArgsConstructor
public class CreateNewTransactionUseCase {
    private final AccountService accountService;
    private final NaturalPersonDataService naturalPersonDataService;
    private final TransactionService transactionService;

    public Mono<Transaction> execute(NewTransaction newTransaction, String consumerAcronym, String messageId){
        return this.fetchAccounts(newTransaction, messageId)
                .flatMap(accounts->
                        this.validateBalance(accounts.getT1(), newTransaction.getAmount())
                                .thenReturn(accounts)
                )
                .flatMap(accounts->Mono.zip(
                        naturalPersonDataService.getNaturalPersonData(accounts.getT1().getOwner(), messageId),
                        naturalPersonDataService.getNaturalPersonData(accounts.getT2().getOwner(), messageId)
                ).flatMap(this::validateNaturalPersonData))
                .flatMap(v-> transactionService.createTransaction(newTransaction, consumerAcronym))
                .flatMap(transaction -> Mono.zip(
                        accountService.updateAccountBalance(newTransaction.getOrigin(), -newTransaction.getAmount(), messageId),
                        accountService.updateAccountBalance(newTransaction.getDestination(), newTransaction.getAmount(), messageId)
                ).thenReturn(transaction));
    }

    Mono<Tuple2<Account, Account>> fetchAccounts(NewTransaction newTransaction, String messageId){
        var originAccount = accountService.getAccount(newTransaction.getOrigin(), messageId);
        var destinationAccount = accountService.getAccount(newTransaction.getDestination(), messageId);
        return originAccount.zipWith(destinationAccount);
    }

    Mono<Boolean> validateNaturalPersonData(Tuple2<NaturalPersonData, NaturalPersonData> data){
        var origin = data.getT1();
        var destination = data.getT2();

        if(!origin.getStatus().equals("ACTIVE") || !destination.getStatus().equals("ACTIVE")){
            return Mono.error(()->new BusinessException(BusinessExceptionMessages.NATURAL_PERSON_NO_ACTIVE, null));
        }
        return Mono.just(true);
    }

    Mono<Account> validateBalance(Account account, long transactionValue){
        if(account.getNumber() < transactionValue){
            return Mono.error(new BusinessException(BusinessExceptionMessages.NOT_ENOUGH_FOUNDS, null));
        }
        return Mono.just(account);
    }
}
