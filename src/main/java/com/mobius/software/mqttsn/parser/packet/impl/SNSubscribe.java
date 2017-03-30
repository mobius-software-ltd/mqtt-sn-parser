package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.avps.SNTopic;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class SNSubscribe implements SNMessage
{
	private int messageID;
	private SNTopic topic;
	private boolean dup;

	public SNSubscribe()
	{
		super();
	}

	public SNSubscribe(int messageID, SNTopic topic, boolean dup)
	{
		this.messageID = messageID;
		this.topic = topic;
		this.dup = dup;
	}

	public SNSubscribe reInit(int messageID, SNTopic topic, boolean dup)
	{
		this.messageID = messageID;
		this.topic = topic;
		this.dup = dup;
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
		return SNType.SUBSCRIBE;
	}

	public int getMessageID()
	{
		return messageID;
	}

	public void setMessageID(int messageID)
	{
		this.messageID = messageID;
	}

	public SNTopic getTopic()
	{
		return topic;
	}

	public void setTopic(SNTopic topic)
	{
		this.topic = topic;
	}

	public boolean isDup()
	{
		return dup;
	}

	public void setDup(boolean dup)
	{
		this.dup = dup;
	}
}
