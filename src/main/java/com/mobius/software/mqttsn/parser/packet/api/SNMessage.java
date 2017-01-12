package com.mobius.software.mqttsn.parser.packet.api;

import com.mobius.software.mqttsn.parser.avps.SNType;

public interface SNMessage
{
	int getLength();

	SNType getType();
}
