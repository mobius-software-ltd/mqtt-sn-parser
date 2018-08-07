package com.mobius.software.mqttsn.parser.packet.api;

import com.mobius.software.mqttsn.parser.avps.FullTopic;
import com.mobius.software.mqttsn.parser.avps.Radius;
import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNQoS;
import com.mobius.software.mqttsn.parser.avps.SNTopic;

import io.netty.buffer.ByteBuf;

public interface SNDevice
{
	void processConnect(boolean cleanSession, int keepalive);

	void processConnack(ReturnCode code);

	void processSubscribe(int messageID, SNTopic topic);

	void processSuback(int messageID, int topicID, ReturnCode returnCode, SNQoS allowedQos);

	void processUnsubscribe(int messageID, SNTopic topic);

	void processUnsuback(int messageID);

	void processPublish(int messageID, SNTopic topic, ByteBuf content, Boolean retain, Boolean isDup);

	void processPuback(int messageID);

	void processPubrec(int messageID);

	void processPubrel(int messageID);

	void processPubcomp(int messageID);

	void processPingreq();

	void processPingresp();

	void processDisconnect();

	void processWillTopicRequest();

	void processWillMessageRequest();

	void processWillTopic(FullTopic topic);

	void processWillMessage(ByteBuf content);

	void processWillTopicUpdate(FullTopic willTopic);

	void processWillMessageUpdate(ByteBuf content);

	void processWillTopicResponse();

	void processWillMessageResponse();

	void processAdvertise(int gatewayID, int duration);

	void processGwInfo(int gatewayID, String gatewayAddress);

	void processSearchGw(Radius radius);

	void processRegister(int messageID, int topicID, String topicName);

	void processRegack(int messageID, int topicID, ReturnCode returnCode);
}
