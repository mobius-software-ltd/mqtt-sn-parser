package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;

public class Register extends CountableMessage
{
	private int topicID;
	private String topicName;

	public Register()
	{
		super();
	}

	public Register(int topicID, int messageID, String topicName)
	{
		super(messageID);
		this.topicID = topicID;
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

	public String getTopicName()
	{
		return topicName;
	}

	public void setTopicName(String topicName)
	{
		this.topicName = topicName;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processRegister(messageID, topicID, topicName);
	}
}