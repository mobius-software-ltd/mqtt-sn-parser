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
import com.mobius.software.mqttsn.parser.avps.Flag;
import com.mobius.software.mqttsn.parser.avps.QoS;
import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.Suback;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class SubackTests
{
	private static final int TOPIC_ID = 11;
	private static final int MESSAGE_ID = 22;
	private static final ReturnCode CODE = ReturnCode.ACCEPTED;
	private static final QoS QOS = QoS.AT_LEAST_ONCE;
	private static Suback message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new Suback(TOPIC_ID, MESSAGE_ID, CODE, QOS);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.SUBACK, message.getType());
			assertEquals(0x13, message.getType().getValue());
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
			assertEquals(8, message.getLength());
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
			Suback Suback = new Suback(TOPIC_ID, MESSAGE_ID, CODE, QOS);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(Suback);
			assertTrue(ByteBufUtil.equals(expected, actual));
			Suback = (Suback) Parser.decode(actual);
			Assertion.assertSuback(message, Suback);
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
		{ 0x09, 0x13, 0x20, 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x13, 0x20, 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidMessageID()
	{
		Suback suback = new Suback(TOPIC_ID, 0, CODE, QOS);
		ByteBuf buf = Parser.encode(suback);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicIDZero()
	{
		Suback suback = new Suback(0x0000, MESSAGE_ID, CODE, QOS);
		ByteBuf buf = Parser.encode(suback);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicID65535()
	{
		Suback suback = new Suback(0xFFFF, MESSAGE_ID, CODE, QOS);
		ByteBuf buf = Parser.encode(suback);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidCode()
	{
		byte[] invalidLengthArray =
		{ 0x08, 0x13, 0x20, 0x00, 0x0b, 0x00, 0x16, 0x04 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsDup()
	{
		byte[] invalidFlagsArray =
		{ 0x08, 0x13, (byte) (0x20 + Flag.DUPLICATE.getValue()), 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsWill()
	{
		byte[] invalidFlagsArray =
		{ 0x08, 0x13, (byte) (0x20 + Flag.WILL.getValue()), 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsRetain()
	{
		byte[] invalidFlagsArray =
		{ 0x08, 0x13, (byte) (0x20 + Flag.RETAIN.getValue()), 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsCleanSession()
	{
		byte[] invalidFlagsArray =
		{ 0x08, 0x13, (byte) (0x20 + Flag.CLEAN_SESSION.getValue()), 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsTopicTypeShort()
	{
		byte[] invalidFlagsArray =
		{ 0x08, 0x13, (byte) (0x20 + Flag.SHORT_TOPIC.getValue()), 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsTopicTypePredefined()
	{
		byte[] invalidFlagsArray =
		{ 0x08, 0x13, (byte) (0x20 + Flag.ID_TOPIC.getValue()), 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsTopicTypeReserved()
	{
		byte[] invalidFlagsArray =
		{ 0x08, 0x13, (byte) (0x20 + Flag.RESERVED_TOPIC.getValue()), 0x00, 0x0b, 0x00, 0x16, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}
}
