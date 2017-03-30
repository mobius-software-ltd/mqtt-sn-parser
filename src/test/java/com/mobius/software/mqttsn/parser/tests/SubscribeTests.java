package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import org.junit.Before;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.Flag;
import com.mobius.software.mqttsn.parser.avps.FullTopic;
import com.mobius.software.mqttsn.parser.avps.IdentifierTopic;
import com.mobius.software.mqttsn.parser.avps.SNQoS;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.avps.SNTopic;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.SNSubscribe;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class SubscribeTests
{
	private static final int MESSAGE_ID = 22;
	private static final SNTopic TOPIC = new IdentifierTopic(33, SNQoS.EXACTLY_ONCE);
	private static final boolean DUP = false;
	private static SNSubscribe message;

	@Before
	public void setUp()
	{
		message = new SNSubscribe(MESSAGE_ID, TOPIC, DUP);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.SUBSCRIBE, message.getType());
			assertEquals(0x12, message.getType().getValue());
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
			assertEquals(5 + TOPIC.length(), message.getLength());
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
			SNSubscribe subscribe = new SNSubscribe(MESSAGE_ID, TOPIC, DUP);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(subscribe);
			assertTrue(ByteBufUtil.equals(expected, actual));
			subscribe = (SNSubscribe) Parser.decode(actual);
			Assertion.assertSubscribe(message, subscribe);
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
		{ 0x08, 0x12, 0x41, 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x06, 0x12, 0x41, 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidMessageIDZero()
	{
		SNSubscribe Subscribe = new SNSubscribe(0, TOPIC, DUP);
		ByteBuf buf = Parser.encode(Subscribe);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicIDZero()
	{
		SNTopic topic = new IdentifierTopic(0, SNQoS.AT_MOST_ONCE);
		SNSubscribe Subscribe = new SNSubscribe(MESSAGE_ID, topic, DUP);
		ByteBuf buf = Parser.encode(Subscribe);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicID65535()
	{
		SNTopic topic = new IdentifierTopic(65535, SNQoS.AT_MOST_ONCE);
		SNSubscribe Subscribe = new SNSubscribe(MESSAGE_ID, topic, DUP);
		ByteBuf buf = Parser.encode(Subscribe);
		Parser.decode(buf);
	}

	@Test
	public void testThreeOctetLength()
	{
		try
		{
			int totalSegments = 28;
			StringBuilder sb = new StringBuilder();
			sb.append("root");
			for (int i = 0; i < totalSegments; i++)
				sb.append("/segment").append(i);
			String topicName = sb.toString();
			FullTopic topic = new FullTopic(topicName, SNQoS.EXACTLY_ONCE);
			SNSubscribe expected = new SNSubscribe(MESSAGE_ID, topic, DUP);
			assertEquals(7 + expected.getTopic().length(), expected.getLength());
			ByteBuf buf = Parser.encode(expected);
			SNSubscribe actual = (SNSubscribe) Parser.decode(buf);
			assertEquals(expected.getLength(), actual.getLength());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsRetain()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x12, (byte) (0x41 + Flag.RETAIN.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsWill()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x12, (byte) (0x41 + Flag.WILL.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsCleanSession()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x12, (byte) (0x41 + Flag.CLEAN_SESSION.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsTopicType()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x12, (byte) (0x41 + Flag.RESERVED_TOPIC.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}
}
