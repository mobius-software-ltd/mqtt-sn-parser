package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.ResponseMessage;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;

public class WillMsgResp extends ResponseMessage
{
	public WillMsgResp()
	{
		super();
	}

	public WillMsgResp(ReturnCode code)
	{
		super(code);
	}

	public WillMsgResp reInit(ReturnCode code)
	{
		setCode(code);
		return this;
	}

	@Override
	public SNType getType()
	{
		return SNType.WILL_MSG_RESP;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processWillMessageResponse();
	}
}
