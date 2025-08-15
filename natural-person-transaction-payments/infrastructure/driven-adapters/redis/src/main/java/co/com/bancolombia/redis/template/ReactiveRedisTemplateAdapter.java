package co.com.bancolombia.redis.template;

import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionCacheRepository;
import co.com.bancolombia.redis.template.helper.ReactiveTemplateAdapterOperations;
import co.com.bancolombia.redis.template.model.TransactionDTO;
import co.com.bancolombia.redis.template.model.TransactionListDTO;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ReactiveRedisTemplateAdapter extends ReactiveTemplateAdapterOperations<TransactionListDTO, String, TransactionListDTO> implements TransactionCacheRepository {
    public ReactiveRedisTemplateAdapter(ReactiveRedisConnectionFactory connectionFactory, ObjectMapper mapper) {
        super(connectionFactory, mapper, d -> d);
    }

    @Override
    public Mono<List<Transaction>> saveTransactionsCache(List<Transaction> transactions, String accountId) {
        var dto = TransactionListDTO.builder()
                .transactions(transactions.stream().map(t->mapper.map(t, TransactionDTO.class)).toList())
                .build();
        return this.save(accountId, dto, 10000)
                .map(this::toModel);
    }

    @Override
    public Mono<List<Transaction>> getTransactionsCacheByAccount(String accountId) {
        return this.findById(accountId)
                .map(this::toModel);
    }

    private List<Transaction> toModel(TransactionListDTO transactionListDTO){
        return transactionListDTO.getTransactions()
                .stream()
                .map(t-> mapper.map(t, Transaction.class))
                .toList();
    }
}
