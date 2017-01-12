package com.mobius.software.mqttsn.parser.avps;

public class NamedTopic implements Topic
{
	private String value;
	private QoS qos;

	public NamedTopic()
	{
		super();
	}

	public NamedTopic(String value, QoS qos)
	{
		this.value = value;
		this.qos = qos;
	}

	public NamedTopic reInit(String value, QoS qos)
	{
		this.value = value;
		this.qos = qos;
		return this;
	}

	@Override
	public TopicType getType()
	{
		return TopicType.NAMED;
	}

	@Override
	public byte[] encode()
	{
		return value.getBytes();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((qos == null) ? 0 : qos.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NamedTopic other = (NamedTopic) obj;
		if (qos != other.qos)
			return false;
		if (value == null)
		{
			if (other.value != null)
				return false;
		}
		else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int length()
	{
		return value.length();
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public QoS getQos()
	{
		return qos;
	}

	public void setQos(QoS qos)
	{
		this.qos = qos;
	}
}
