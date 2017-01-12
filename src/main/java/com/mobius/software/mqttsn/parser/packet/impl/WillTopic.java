package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.NamedTopic;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class WillTopic implements SNMessage
{
	private boolean retain;
	private NamedTopic topic;

	public WillTopic()
	{
		super();
	}

	public WillTopic(boolean retain, NamedTopic topic)
	{
		this.retain = retain;
		this.topic = topic;
	}

	public WillTopic reInit(boolean retain, NamedTopic topic)
	{
		this.retain = retain;
		this.topic = topic;
		return this;
	}

	@Override
	public int getLength()
	{
		int length = 2;
		if (topic != null)
		{
			length += topic.length() + 1;
			if (topic.length() > 252)
				length += 2;
		}
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.WILL_TOPIC;
	}

	public boolean isRetain()
	{
		return retain;
	}

	public void setRetain(boolean retain)
	{
		this.retain = retain;
	}

	public NamedTopic getTopic()
	{
		return topic;
	}

	public void setTopic(NamedTopic topic)
	{
		this.topic = topic;
	}
}
