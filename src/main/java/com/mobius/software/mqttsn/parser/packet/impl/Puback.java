package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;

public class Puback extends CountableMessage
{
	private int topicID;
	private ReturnCode code;

	public Puback()
	{
		super();
	}

	public Puback(int topicID, int messageID, ReturnCode code)
	{
		super(messageID);
		this.topicID = topicID;
		this.code = code;
	}

	public Puback reInit(int topicID, int messageID, ReturnCode code)
	{
		this.topicID = topicID;
		setMessageID(messageID);
		this.code = code;
		return this;
	}

	@Override
	public int getLength()
	{
		return 7;
	}

	@Override
	public SNType getType()
	{
		return SNType.PUBACK;
	}

	public int getTopicID()
	{
		return topicID;
	}

	public void setTopicID(int topicID)
	{
		this.topicID = topicID;
	}

	public ReturnCode getCode()
	{
		return code;
	}

	public void setCode(ReturnCode code)
	{
		this.code = code;
	}
}