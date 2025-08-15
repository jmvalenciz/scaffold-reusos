package co.com.bancolombia.mq.listener;

import co.com.bancolombia.commons.jms.api.MQMessageSender;
import co.com.bancolombia.commons.jms.mq.MQListener;
import co.com.bancolombia.commons.jms.mq.EnableMQMessageSender;
import co.com.bancolombia.mq.listener.model.NewTransactionDTO;
import co.com.bancolombia.mq.listener.model.TransactionListDTO;
import co.com.bancolombia.mq.listener.model.TransactionResponseDTO;
import co.com.bancolombia.usecase.createnewtransaction.CreateNewTransactionUseCase;
import co.com.bancolombia.usecase.gettransactionsbyaccount.GetTransactionsByAccountUseCase;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Timer;
import jakarta.jms.Destination;
import lombok.RequiredArgsConstructor;
import org.beanio.BeanReader;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.StreamBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@EnableMQMessageSender
public class SampleMQMessageListener {
    private final MQMessageSender sender;
    private final Timer timer = Metrics.timer("mq_receive_message", "operation", "my-operation"); // TODO: Change operation name
    private final CreateNewTransactionUseCase createNewTransactionUseCase;
    private final GetTransactionsByAccountUseCase getTransactionsByAccountUseCase;

    @MQListener("${commons.jms.input-queue}")
    public void process(Message message) throws JMSException {
        timer.record(System.currentTimeMillis() - message.getJMSTimestamp(), TimeUnit.MILLISECONDS);
        TextMessage textMessage = (TextMessage) message;
        var operation = textMessage.getText().substring(0,15).trim();
        var payload = textMessage.getText().substring(15);
        Mono<String> res = switch(operation){
            case "getTransactions" -> handleGetTransactions(textMessage, payload);
            case "newTransaction" -> handleNewTransaction(textMessage, payload);
            default -> throw new IllegalStateException("Unexpected value: " + payload);
        };
        res.subscribe();
    }

    private Mono<String> handleNewTransaction(TextMessage message, String payload) throws JMSException {
        var replyTo = message.getJMSReplyTo();
        var correlationId = message.getJMSMessageID();
        NewTransactionDTO transactionMessage = stringToObject(payload, NewTransactionDTO.class);
        var newTransaction = transactionMessage.toModel();
        return createNewTransactionUseCase.newTransaction(newTransaction)
                .flatMap(transaction ->{
                    var transactionDto = TransactionResponseDTO.fromModel(transaction);
                    return sendResponse(objectToString(transactionDto, TransactionResponseDTO.class), correlationId, replyTo);
                });
    }

    private Mono<String> handleGetTransactions(TextMessage message, String payload) throws JMSException {
        var replyTo = message.getJMSReplyTo();
        var correlationId = message.getJMSMessageID();
        return getTransactionsByAccountUseCase.getTransactions(payload.substring(0,36))
                .collectList()
                .delayElement(Duration.ofSeconds(5))
                .flatMap(transactions -> {
                    TransactionListDTO transactionList = TransactionListDTO.builder()
                            .transactions(transactions.stream().map(TransactionResponseDTO::fromModel).toList())
                            .build();
                    var stringList = transactionList.getTransactions()
                            .stream()
                            .map(t -> objectToString(t, TransactionResponseDTO.class)).toList();
                    return sendResponse(String.join("", stringList), correlationId, replyTo);
                });
    }

    private Mono<String> sendResponse(String body, String correlationId, Destination destination){
        return sender.send(destination, context -> {
            Message textMessage = context.createTextMessage(body);
            textMessage.setJMSCorrelationID(correlationId);
            return textMessage;
        });
    }

    private <T> T stringToObject(String data, Class<T> clazz){
        String streamName = clazz.getName()+"Stream";
        StreamFactory factory = StreamFactory.newInstance();
        StreamBuilder builder = new StreamBuilder(streamName)
                .format("fixedlength")
                .addRecord(clazz);
        factory.define(builder);

        StringReader in = new StringReader(data);
        BeanReader reader = factory.createReader(streamName, in);
        T newTransactionDTO = (T) reader.read();
        reader.close();

        return newTransactionDTO;
    }

    private <T> String objectToString(T obj, Class<T> clazz){
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
