package co.com.bancolombia.mq.listener.marshall;

import co.com.bancolombia.model.transaction.NewTransaction;
import co.com.bancolombia.mq.listener.model.NewTransactionDTO;
import org.beanio.Marshaller;
import org.beanio.StreamFactory;
import org.beanio.Unmarshaller;
import org.beanio.builder.StreamBuilder;

public class ObjectMarshal<T> {
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public ObjectMarshal(Class<T> clazz){
        var streamFactory = StreamFactory.newInstance();
        String dtoName = clazz.getName();
        streamFactory.define(new StreamBuilder(dtoName).format("fixedlength").addRecord(clazz));
        this.marshaller = streamFactory.createMarshaller(dtoName);
        this.unmarshaller = streamFactory.createUnmarshaller(dtoName);
    }

    public String marshal(T dto){
        return marshaller.marshal(dto).toString();
    }

    public T unmarshal(String data){
        return (T) unmarshaller.unmarshal(data);
    }
}
