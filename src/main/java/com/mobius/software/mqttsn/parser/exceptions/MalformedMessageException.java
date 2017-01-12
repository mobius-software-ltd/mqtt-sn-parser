package com.mobius.software.mqttsn.parser.exceptions;

public class MalformedMessageException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public MalformedMessageException(String message)
	{
		super(message);
	}
}
