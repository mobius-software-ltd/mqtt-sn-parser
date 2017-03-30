package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.ResponseMessage;

public class SNConnack extends ResponseMessage
{
	public SNConnack()
	{
		super();
	}

	public SNConnack(ReturnCode code)
	{
		super(code);
	}

	public SNConnack reInit(ReturnCode code)
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
