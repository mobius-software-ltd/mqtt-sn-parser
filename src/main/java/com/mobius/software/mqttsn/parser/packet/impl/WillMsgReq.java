package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class WillMsgReq extends SNMessage
{
	@Override
	public int getLength()
	{
		return 2;
	}

	@Override
	public SNType getType()
	{
		return SNType.WILL_MSG_REQ;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processWillMessageRequest();
	}
}
