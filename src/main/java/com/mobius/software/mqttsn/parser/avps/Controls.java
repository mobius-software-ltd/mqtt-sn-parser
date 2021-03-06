package com.mobius.software.mqttsn.parser.avps;

import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;

public class Controls
{
	private Radius radius;

	private Controls(Radius radius)
	{
		this.radius = radius;
	}

	public static Controls decode(byte ctrlByte)
	{
		if (ctrlByte > 3 || ctrlByte < 0)
			throw new MalformedMessageException("Invalid Encapsulated message control encoding:" + ctrlByte);
		
		return new Controls(Radius.valueOf(ctrlByte));
	}

	public static byte encode(Radius radius)
	{
		byte ctrlByte = 0;
		ctrlByte |= radius.getValue();
		return ctrlByte;
	}

	public Radius getRadius()
	{
		return radius;
	}

	public void setRadius(Radius radius)
	{
		this.radius = radius;
	}
}
