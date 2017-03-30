package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.FullTopic;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class WillTopicUpd implements SNMessage
{
	private boolean retain;
	private FullTopic topic;

	public WillTopicUpd()
	{
		super();
	}

	public WillTopicUpd(boolean retain, FullTopic topic)
	{
		this.topic = topic;
		this.retain = retain;
	}

	public WillTopicUpd reInit(boolean retain, FullTopic topic)
	{
		this.topic = topic;
		this.retain = retain;
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
		return SNType.WILL_TOPIC_UPD;
	}

	public FullTopic getTopic()
	{
		return topic;
	}

	public void setTopic(FullTopic topic)
	{
		this.topic = topic;
	}

	public boolean isRetain()
	{
		return retain;
	}

	public void setRetain(boolean retain)
	{
		this.retain = retain;
	}
}