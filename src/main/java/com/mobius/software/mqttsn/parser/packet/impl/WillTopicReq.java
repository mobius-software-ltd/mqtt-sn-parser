package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class WillTopicReq implements SNMessage
{
	public WillTopicReq()
	{

	}

	@Override
	public int getLength()
	{
		return 2;
	}

	@Override
	public SNType getType()
	{
		return SNType.WILL_TOPIC_REQ;
	}
}
