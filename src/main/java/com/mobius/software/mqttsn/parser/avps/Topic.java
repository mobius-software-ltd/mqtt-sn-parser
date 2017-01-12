package com.mobius.software.mqttsn.parser.avps;

public interface Topic
{
	TopicType getType();

	QoS getQos();

	byte[] encode();

	int length();
}
