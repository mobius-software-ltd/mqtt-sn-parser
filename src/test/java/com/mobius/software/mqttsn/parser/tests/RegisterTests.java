package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.Register;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class RegisterTests
{
	private static final int TOPIC_ID = 11;
	private static final int MESSAGE_ID = 22;
	private static final String TOPIC_NAME = "root/first";
	private static Register message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new Register(TOPIC_ID, MESSAGE_ID, TOPIC_NAME);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.REGISTER, message.getType());
			assertEquals(0x0A, message.getType().getValue());
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
			assertEquals(6 + TOPIC_NAME.length(), message.getLength());
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
			Register register = new Register(TOPIC_ID, MESSAGE_ID, TOPIC_NAME);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(register);
			assertTrue(ByteBufUtil.equals(expected, actual));
			register = (Register) Parser.decode(actual);
			Assertion.assertRegister(message, register);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidExceedingLength()
	{
		byte[] invalidLengthArray =
		{ 0x11, 0x0a, 0x00, 0x0b, 0x00, 0x16, 0x72, 0x6f, 0x6f, 0x74, 0x2f, 0x66, 0x69, 0x72, 0x73, 0x74 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x09, 0x0a, 0x00, 0x0b, 0x00, 0x16, 0x72, 0x6f, 0x6f, 0x74, 0x2f, 0x66, 0x69, 0x72, 0x73, 0x74 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidMessageID()
	{
		Register register = new Register(TOPIC_ID, 0, TOPIC_NAME);
		ByteBuf buf = Parser.encode(register);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicIDZero()
	{
		Register register = new Register(0x0000, MESSAGE_ID, TOPIC_NAME);
		ByteBuf buf = Parser.encode(register);
		Parser.decode(buf);
	}
	
	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicID65535()
	{
		Register register = new Register(0xFFFF, MESSAGE_ID, TOPIC_NAME);
		ByteBuf buf = Parser.encode(register);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicName()
	{
		byte[] invalidTopicArray =
		{ 0x09, 0x0a, 0x00, 0x0b, 0x00, 0x16 };
		ByteBuf buf = Unpooled.buffer(invalidTopicArray.length);
		buf.writeBytes(invalidTopicArray);
		Parser.decode(buf);
	}
}
