package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNQoS;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;

public class SNSuback extends CountableMessage
{
	private int topicID;
	private ReturnCode code;
	private SNQoS allowedQos;

	public SNSuback()
	{
		super();
	}

	public SNSuback(int topicID, int messageID, ReturnCode code, SNQoS allowedQos)
	{
		super(messageID);
		this.topicID = topicID;
		this.code = code;
		this.allowedQos = allowedQos;
	}

	public SNSuback reInit(int topicID, int messageID, ReturnCode code, SNQoS allowedQos)
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

	public SNQoS getAllowedQos()
	{
		return allowedQos;
	}

	public void setAllowedQos(SNQoS allowedQos)
	{
		this.allowedQos = allowedQos;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processSuback(messageID, topicID, code, allowedQos);
	}
}