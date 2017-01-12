package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.Connack;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class ConnackTests
{
	private static final ReturnCode code = ReturnCode.ACCEPTED;
	private static Connack message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new Connack(code);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.CONNACK, message.getType());
			assertEquals(5, message.getType().getValue());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testLength()
	{
		try
		{
			assertEquals(3, message.getLength());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testEncodeDeode()
	{
		try
		{
			Connack connack = new Connack(code);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(connack);
			assertTrue(ByteBufUtil.equals(expected, actual));
			connack = (Connack) Parser.decode(actual);
			Assertion.assertConnack(message, connack);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidCode()
	{
		byte[] invalidReturnCodeArray =
		{ 0x03, 0x05, 0x04 };
		ByteBuf buf = Unpooled.buffer(invalidReturnCodeArray.length);
		buf.writeBytes(invalidReturnCodeArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidExceedingLength()
	{
		byte[] invalidLengthArray =
		{ 0x04, 0x05, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x02, 0x05, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}
}
