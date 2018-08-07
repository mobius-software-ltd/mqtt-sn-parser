package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.ResponseMessage;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;

public class WillTopicResp extends ResponseMessage
{
	public WillTopicResp()
	{
		super();
	}

	public WillTopicResp(ReturnCode code)
	{
		super(code);
	}

	public WillTopicResp reInit(ReturnCode code)
	{
		setCode(code);
		return this;
	}

	@Override
	public SNType getType()
	{
		return SNType.WILL_TOPIC_RESP;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processWillTopicResponse();
	}
}
