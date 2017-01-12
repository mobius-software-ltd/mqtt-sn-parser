package com.mobius.software.mqttsn.parser.avps;

import java.util.EnumSet;

import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;

public class Flags
{
	private boolean dup;
	private QoS qos;
	private boolean retain;
	private boolean will;
	private boolean cleanSession;
	private TopicType topicType;

	public Flags(boolean dup, QoS qos, boolean retain, boolean will, boolean cleanSession, TopicType topicType)
	{
		this.dup = dup;
		this.qos = qos;
		this.retain = retain;
		this.will = will;
		this.cleanSession = cleanSession;
		this.topicType = topicType;
	}

	public Flags reInit(boolean dup, QoS qos, boolean retain, boolean will, boolean cleanSession, TopicType topicType)
	{
		this.dup = dup;
		this.qos = qos;
		this.retain = retain;
		this.will = will;
		this.cleanSession = cleanSession;
		this.topicType = topicType;
		return this;
	}

	public static Flags decode(byte flagsByte, SNType type)
	{
		EnumSet<Flag> bitMask = EnumSet.noneOf(Flag.class);
		for (Flag flag : Flag.values())
		{
			if ((flagsByte & flag.getValue()) == flag.getValue())
				bitMask.add(flag);
		}
		return Flags.validateAndCreate(bitMask, type);
	}

	private static Flags validateAndCreate(EnumSet<Flag> bitMask, SNType type)
	{
		if (bitMask.contains(Flag.RESERVED_TOPIC))
			throw new MalformedMessageException("Invalid topic type encoding. TopicType reserved bit must not be encoded");

		boolean dup = bitMask.contains(Flag.DUPLICATE);
		boolean retain = bitMask.contains(Flag.RETAIN);
		boolean will = bitMask.contains(Flag.WILL);
		boolean cleanSession = bitMask.contains(Flag.CLEAN_SESSION);

		QoS qos = null;
		if (bitMask.contains(Flag.QOS_LEVEL_ONE))
			qos = QoS.LEVEL_ONE;
		else if (bitMask.contains(Flag.QOS_2))
			qos = QoS.EXACTLY_ONCE;
		else if (bitMask.contains(Flag.QOS_1))
			qos = QoS.AT_LEAST_ONCE;
		else
			qos = QoS.AT_MOST_ONCE;

		TopicType topicType = null;
		if (bitMask.contains(Flag.SHORT_TOPIC))
			topicType = TopicType.SHORT;
		else if (bitMask.contains(Flag.ID_TOPIC))
			topicType = TopicType.PREDEFINED;
		else
			topicType = TopicType.NAMED;

		switch (type)
		{
		case CONNECT:
			if (dup)
				throw new MalformedMessageException(type + " invalid encoding: dup flag");
			if (qos != QoS.AT_MOST_ONCE)
				throw new MalformedMessageException(type + " invalid encoding: qos flag - " + qos.getValue());
			if (retain)
				throw new MalformedMessageException(type + " invalid encoding: retain flag");
			if (topicType != TopicType.NAMED)
				throw new MalformedMessageException(type + " invalid encoding: topicIdType flag - " + topicType.getValue());
			break;
		case WILL_TOPIC:
			if (dup)
				throw new MalformedMessageException(type + " invalid encoding: dup flag");
			if (qos == null)
				throw new MalformedMessageException(type + " invalid encoding: qos flag");
			if (will)
				throw new MalformedMessageException(type + " invalid encoding: will flag");
			if (cleanSession)
				throw new MalformedMessageException(type + " invalid encoding: cleanSession flag");
			if (topicType != TopicType.NAMED)
				throw new MalformedMessageException(type + " invalid encoding: topicIdType flag - " + topicType.getValue());
			break;

		case PUBLISH:
			if (qos == null)
				throw new MalformedMessageException(type + " invalid encoding: qos flag");
			if (topicType == null)
				throw new MalformedMessageException(type + " invalid encoding: topicIdType flag");
			if (will)
				throw new MalformedMessageException(type + " invalid encoding: will flag");
			if (cleanSession)
				throw new MalformedMessageException(type + " invalid encoding: cleanSession flag");
			break;

		case SUBSCRIBE:
			if (qos == null)
				throw new MalformedMessageException(type + " invalid encoding: qos flag");
			if (retain)
				throw new MalformedMessageException(type + " invalid encoding: retain flag");
			if (will)
				throw new MalformedMessageException(type + " invalid encoding: will flag");
			if (cleanSession)
				throw new MalformedMessageException(type + " invalid encoding: cleanSession flag");
			if (topicType == null)
				throw new MalformedMessageException(type + " invalid encoding: retain flag");
			break;

		case SUBACK:
			if (dup)
				throw new MalformedMessageException(type + " invalid encoding: dup flag");
			if (qos == null)
				throw new MalformedMessageException(type + " invalid encoding: qos flag");
			if (retain)
				throw new MalformedMessageException(type + " invalid encoding: retain flag");
			if (will)
				throw new MalformedMessageException(type + " invalid encoding: will flag");
			if (cleanSession)
				throw new MalformedMessageException(type + " invalid encoding: cleanSession flag");
			if (topicType != TopicType.NAMED)
				throw new MalformedMessageException(type + " invalid encoding: topicIdType flag");
			break;

		case UNSUBSCRIBE:
			if (dup)
				throw new MalformedMessageException(type + " invalid encoding: dup flag");
			if (qos != QoS.AT_MOST_ONCE)
				throw new MalformedMessageException(type + " invalid encoding: qos flag");
			if (retain)
				throw new MalformedMessageException(type + " invalid encoding: retain flag");
			if (will)
				throw new MalformedMessageException(type + " invalid encoding: will flag");
			if (cleanSession)
				throw new MalformedMessageException(type + " invalid encoding: cleanSession flag");
			if (topicType == null)
				throw new MalformedMessageException(type + " invalid encoding: topicIdType flag");
			break;

		case WILL_TOPIC_UPD:
			if (dup)
				throw new MalformedMessageException(type + " invalid encoding: dup flag");
			if (qos == null)
				throw new MalformedMessageException(type + " invalid encoding: qos flag");
			if (will)
				throw new MalformedMessageException(type + " invalid encoding: will flag");
			if (cleanSession)
				throw new MalformedMessageException(type + " invalid encoding: cleanSession flag");
			if (topicType != TopicType.NAMED)
				throw new MalformedMessageException(type + " invalid encoding: topicIdType flag");
			break;

		default:
			break;
		}
		return new Flags(dup, qos, retain, will, cleanSession, topicType);
	}

	public static byte encode(boolean dup, QoS qos, boolean retain, boolean will, boolean cleanSession, TopicType topicType)
	{
		byte flagsByte = 0;
		if (dup)
			flagsByte += Flag.DUPLICATE.getValue();
		if (qos != null)
			flagsByte += qos.getValue() << 5;
		if (retain)
			flagsByte += Flag.RETAIN.getValue();
		if (will)
			flagsByte += Flag.WILL.getValue();
		if (cleanSession)
			flagsByte += Flag.CLEAN_SESSION.getValue();
		if (topicType != null)
			flagsByte += topicType.getValue();
		return flagsByte;
	}

	public boolean isDup()
	{
		return dup;
	}

	public void setDup(boolean dup)
	{
		this.dup = dup;
	}

	public QoS getQos()
	{
		return qos;
	}

	public void setQos(QoS qos)
	{
		this.qos = qos;
	}

	public boolean isRetain()
	{
		return retain;
	}

	public void setRetain(boolean retain)
	{
		this.retain = retain;
	}

	public boolean isWill()
	{
		return will;
	}

	public void setWill(boolean will)
	{
		this.will = will;
	}

	public boolean isCleanSession()
	{
		return cleanSession;
	}

	public void setCleanSession(boolean cleanSession)
	{
		this.cleanSession = cleanSession;
	}

	public TopicType getTopicType()
	{
		return topicType;
	}

	public void setTopicType(TopicType topicType)
	{
		this.topicType = topicType;
	}
}
