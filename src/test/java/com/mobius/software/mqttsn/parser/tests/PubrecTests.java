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
import com.mobius.software.mqttsn.parser.packet.impl.SNPubrec;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class PubrecTests
{
	private static final int MESSAGE_ID = 22;
	private static SNPubrec message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new SNPubrec(MESSAGE_ID);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.PUBREC, message.getType());
			assertEquals(0x0F, message.getType().getValue());
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
			SNPubrec Pubrec = new SNPubrec(MESSAGE_ID);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(Pubrec);
			assertTrue(ByteBufUtil.equals(expected, actual));
			Pubrec = (SNPubrec) Parser.decode(actual);
			Assertion.assertPubrec(message, Pubrec);
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
		SNPubrec pubrec = new SNPubrec(0);
		ByteBuf buf = Parser.encode(pubrec);
		Parser.decode(buf);
	}
}
