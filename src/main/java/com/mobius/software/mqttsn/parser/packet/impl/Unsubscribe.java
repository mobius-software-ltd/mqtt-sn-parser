package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.avps.Topic;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class Unsubscribe implements SNMessage
{
	private int messageID;
	private Topic topic;

	public Unsubscribe()
	{
		super();
	}

	public Unsubscribe(int messageID, Topic topic)
	{
		this.messageID = messageID;
		this.topic = topic;
	}

	public Unsubscribe reInit(int messageID, Topic topic)
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

	public int getMessageID()
	{
		return messageID;
	}

	public void setMessageID(int messageID)
	{
		this.messageID = messageID;
	}

	public Topic getTopic()
	{
		return topic;
	}

	public void setTopic(Topic topic)
	{
		this.topic = topic;
	}
}