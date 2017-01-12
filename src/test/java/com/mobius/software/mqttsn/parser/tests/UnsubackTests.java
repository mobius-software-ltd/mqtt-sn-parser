package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.Unsuback;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class UnsubackTests
{
	private static final int MESSAGE_ID = 22;
	private static Unsuback message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new Unsuback(MESSAGE_ID);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.UNSUBACK, message.getType());
			assertEquals(0x15, message.getType().getValue());
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
			assertEquals(4, message.getLength());
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
			Unsuback Unsuback = new Unsuback(MESSAGE_ID);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(Unsuback);
			assertTrue(ByteBufUtil.equals(expected, actual));
			Unsuback = (Unsuback) Parser.decode(actual);
			Assertion.assertUnsuback(message, Unsuback);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidExceedingLength()
	{
		byte[] invalidLengthArray =
		{ 0x05, 0x0f, 0x00, 0x16 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x03, 0x0f, 0x00, 0x16 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidMessageID()
	{
		Unsuback unsuback = new Unsuback(0);
		ByteBuf buf = Parser.encode(unsuback);
		Parser.decode(buf);
	}
}
