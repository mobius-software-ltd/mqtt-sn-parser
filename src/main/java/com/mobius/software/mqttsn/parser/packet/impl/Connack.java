package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.ResponseMessage;

public class Connack extends ResponseMessage
{
	public Connack()
	{
		super();
	}

	public Connack(ReturnCode code)
	{
		super(code);
	}

	public Connack reInit(ReturnCode code)
	{
		setCode(code);
		return this;
	}

	@Override
	public SNType getType()
	{
		return SNType.CONNACK;
	}
}
