package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNTopic;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

import io.netty.buffer.ByteBuf;

public class SNPublish extends SNMessage
{
	private Integer messageID;
	private SNTopic topic;
	private ByteBuf content;
	private boolean dup;
	private boolean retain;

	public SNPublish()
	{
		super();
	}

	public SNPublish(Integer messageID, SNTopic topic, ByteBuf content, boolean dup, boolean retain)
	{
		this.messageID = messageID;
		this.topic = topic;
		this.content = content;
		this.dup = dup;
		this.retain = retain;
	}

	public SNPublish reInit(Integer messageID, SNTopic topic, ByteBuf content, boolean dup, boolean retain)
	{
		this.messageID = messageID;
		this.topic = topic;
		this.content = content;
		this.dup = dup;
		this.retain = retain;
		return this;
	}

	@Override
	public int getLength()
	{
		int length = 7;
		length += content.capacity();
		if (content.capacity() > 248)
			length += 2;
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.PUBLISH;
	}

	public Integer getMessageID()
	{
		return messageID;
	}

	public void setMessageID(Integer messageID)
	{
		this.messageID = messageID;
	}

	public SNTopic getTopic()
	{
		return topic;
	}

	public void setTopic(SNTopic topic)
	{
		this.topic = topic;
	}

	public ByteBuf getContent()
	{
		return content;
	}

	public void setContent(ByteBuf content)
	{
		this.content = content;
	}

	public boolean isDup()
	{
		return dup;
	}

	public void setDup(boolean dup)
	{
		this.dup = dup;
	}

	public boolean isRetain()
	{
		return retain;
	}

	public void setRetain(boolean retain)
	{
		this.retain = retain;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processPublish(messageID, topic, content, retain, dup);
	}
}