package com.mobius.software.mqttsn.parser.avps;

public interface SNTopic
{
	TopicType getType();

	SNQoS getQos();

	byte[] encode();

	int length();
}
