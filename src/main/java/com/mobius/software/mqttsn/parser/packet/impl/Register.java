package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class Register extends SNMessage
{
	private int topicID;
	private int messageID;
	private String topicName;

	public Register()
	{
		super();
	}

	public Register(int topicID, int messageID, String topicName)
	{
		this.topicID = topicID;
		this.messageID = messageID;
		this.topicName = topicName;
	}

	public Register reInit(int topicID, int messageID, String topicName)
	{
		this.topicID = topicID;
		this.messageID = messageID;
		this.topicName = topicName;
		return this;
	}

	@Override
	public int getLength()
	{
		if (this.topicName == null)
			throw new MalformedMessageException(this.getClass().getSimpleName() + " must contain a valid topic name");
		int length = 6;
		length += topicName.length();
		if (topicName.length() > 249)
			length += 2;
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.REGISTER;
	}

	public int getTopicID()
	{
		return topicID;
	}

	public void setTopicID(int topicID)
	{
		this.topicID = topicID;
	}

	public int getMessageID()
	{
		return messageID;
	}

	public void setMessageID(int messageID)
	{
		this.messageID = messageID;
	}

	public String getTopicName()
	{
		return topicName;
	}

	public void setTopicName(String topicName)
	{
		this.topicName = topicName;
	}
}