package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNTopic;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;

public class SNUnsubscribe extends CountableMessage
{
	private SNTopic topic;

	public SNUnsubscribe()
	{
		super();
	}

	public SNUnsubscribe(int messageID, SNTopic topic)
	{
		super(messageID);
		this.topic = topic;
	}

	public SNUnsubscribe reInit(int messageID, SNTopic topic)
	{
		this.messageID = messageID;
		this.topic = topic;
		return this;
	}

	@Override
	public int getLength()
	{
		int length = 5;
		length += topic.length();
		if (topic.length() > 250)
			length += 2;
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.UNSUBSCRIBE;
	}

	public SNTopic getTopic()
	{
		return topic;
	}

	public void setTopic(SNTopic topic)
	{
		this.topic = topic;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processUnsubscribe(messageID, topic);
	}
}