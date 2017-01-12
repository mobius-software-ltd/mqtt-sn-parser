package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.QoS;
import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;

public class Suback extends CountableMessage
{
	private int topicID;
	private ReturnCode code;
	private QoS allowedQos;

	public Suback()
	{
		super();
	}

	public Suback(int topicID, int messageID, ReturnCode code, QoS allowedQos)
	{
		super(messageID);
		this.topicID = topicID;
		this.code = code;
		this.allowedQos = allowedQos;
	}

	public Suback reInit(int topicID, int messageID, ReturnCode code, QoS allowedQos)
	{
		setMessageID(messageID);
		this.topicID = topicID;
		this.code = code;
		this.allowedQos = allowedQos;
		return this;
	}

	@Override
	public int getLength()
	{
		return 8;
	}

	@Override
	public SNType getType()
	{
		return SNType.SUBACK;
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

	public QoS getAllowedQos()
	{
		return allowedQos;
	}

	public void setAllowedQos(QoS allowedQos)
	{
		this.allowedQos = allowedQos;
	}
}