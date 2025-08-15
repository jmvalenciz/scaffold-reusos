package co.com.bancolombia.mq.reqreply;

import co.com.bancolombia.commons.jms.api.MQRequestReply;
import co.com.bancolombia.commons.jms.mq.ReqReply;

import static co.com.bancolombia.commons.jms.internal.models.MQListenerConfig.QueueType.FIXED;

@ReqReply(requestQueue = "${commons.jms.output-queue}",replyQueue = "${commons.jms.input-queue}", queueType = FIXED)
public interface ReqReplyGateway extends MQRequestReply {
}
