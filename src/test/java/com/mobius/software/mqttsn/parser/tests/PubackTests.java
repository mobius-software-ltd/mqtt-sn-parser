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
import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.SNPuback;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class PubackTests
{
	private static final int TOPIC_ID = 11;
	private static final int MESSAGE_ID = 22;
	private static final ReturnCode CODE = ReturnCode.ACCEPTED;
	private static SNPuback message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new SNPuback(TOPIC_ID, MESSAGE_ID, CODE);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.PUBACK, message.getType());
			assertEquals(0x0D, message.getType().getValue());
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
			assertEquals(7, message.getLength());
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
			SNPuback Puback = new SNPuback(TOPIC_ID, MESSAGE_ID, CODE);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(Puback);
			assertTrue(ByteBufUtil.equals(expected, actual));
			Puback = (SNPuback) Parser.decode(actual);
			Assertion.assertPuback(message, Puback);
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
		{ 0x08, 0x0d, 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x06, 0x0d, 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidMessageID()
	{
		SNPuback puback = new SNPuback(TOPIC_ID, 0, CODE);
		ByteBuf buf = Parser.encode(puback);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicIDZero()
	{
		SNPuback puback = new SNPuback(0x0000, MESSAGE_ID, CODE);
		ByteBuf buf = Parser.encode(puback);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicID65535()
	{
		SNPuback puback = new SNPuback(0xFFFF, MESSAGE_ID, CODE);
		ByteBuf buf = Parser.encode(puback);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidCode()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x0d, 0x00, 0x0b, 0x00, 0x16, 0x04 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}
}
