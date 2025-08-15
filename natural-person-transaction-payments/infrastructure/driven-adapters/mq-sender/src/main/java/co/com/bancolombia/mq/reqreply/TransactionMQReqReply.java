package co.com.bancolombia.mq.reqreply;

import co.com.bancolombia.commons.jms.mq.EnableMQGateway;
import co.com.bancolombia.model.transaction.NewTransaction;
import co.com.bancolombia.model.transaction.Transaction;
import co.com.bancolombia.model.transaction.gateways.TransactionService;
import co.com.bancolombia.mq.model.*;
import jakarta.jms.JMSException;
import lombok.RequiredArgsConstructor;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.StreamBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.jms.TextMessage;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableMQGateway(scanBasePackages = "co.com.bancolombia")
public class TransactionMQReqReply implements TransactionService {
    private final ReqReplyGateway sender;

    @Override
    public Mono<Transaction> createTransaction(NewTransaction newTransaction, String consumerAcronym) {
        String operation = "newTransaction";
        var dto = NewTransactionRequest.builder()
                .operation(operation)
                .newTransactionDTO(NewTransactionDTO.fromModel(newTransaction, consumerAcronym))
                .build();
        String messageContent = serielizeMessage(dto, NewTransactionRequest.class);
        return sender.requestReply(messageContent)
                .name("mq_req_reply")
                .tag("operation", operation)
                .metrics()
                .map(message -> {
                    try {
                        var messageText = ((TextMessage) message).getText();
                        var response = deserializeMessage(messageText, TransactionDTO.class).toModel();
                        return response;
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public Mono<List<Transaction>> getTransactionsByAccount(String accountId) {
        String operation = "getTransactions";
        var requestMessage = GetTransactionsRequest.builder()
                .operation(operation)
                .accountId(accountId)
                .build();
        return sender.requestReply(serielizeMessage(requestMessage, GetTransactionsRequest.class))
                .name("mq_req_reply")
                .tag("operation", operation)
                .metrics()
                .map(message -> {
                    try {
                        var messageText = ((TextMessage) message).getText();
                        messageText = messageText.substring(0, messageText.length()-1);
                        return Arrays.stream(messageText
                                        .split("\n"))
                                .map(i->deserializeMessage(i, TransactionDTO.class).toModel())
                                .toList();
                    } catch (ClassCastException e){
                        return new ArrayList<>();
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private <T> T deserializeMessage(String data, Class<T> clazz){
        String streamName = clazz.getName()+"Stream";
        StreamFactory factory = StreamFactory.newInstance();
        StreamBuilder builder = new StreamBuilder(streamName)
                .format("fixedlength")
                .addRecord(clazz);
        factory.define(builder);

        StringReader in = new StringReader(data);
        BeanReader reader = factory.createReader(streamName, in);
        T dto = (T) reader.read();
        reader.close();

        return dto;
    }

    private <T> String serielizeMessage(T obj, Class<T> clazz){
        String streamName = clazz.getName()+"Stream";
        StreamFactory factory = StreamFactory.newInstance();
        StreamBuilder builder = new StreamBuilder(streamName)
                .format("fixedlength")
                .addRecord(clazz);
        factory.define(builder);

        StringWriter out = new StringWriter();
        BeanWriter writer = factory.createWriter(streamName, out);

        writer.write(obj);
        writer.flush();
        writer.close();

        return out.toString();
    }
}
