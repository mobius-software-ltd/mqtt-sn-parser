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
import com.mobius.software.mqttsn.parser.avps.NamedTopic;
import com.mobius.software.mqttsn.parser.avps.PredefinedTopic;
import com.mobius.software.mqttsn.parser.avps.QoS;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.avps.Topic;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.Unsubscribe;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class UnsubscribeTests
{
	private static final int MESSAGE_ID = 22;
	private static final Topic TOPIC = new PredefinedTopic(33, QoS.AT_MOST_ONCE);
	private static Unsubscribe message;

	@Before
	public void setUp()
	{
		message = new Unsubscribe(MESSAGE_ID, TOPIC);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.UNSUBSCRIBE, message.getType());
			assertEquals(0x14, message.getType().getValue());
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
	public void testEncodeDecode()
	{
		try
		{
			Unsubscribe unsubscribe = new Unsubscribe(MESSAGE_ID, TOPIC);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(unsubscribe);
			assertTrue(ByteBufUtil.equals(expected, actual));
			unsubscribe = (Unsubscribe) Parser.decode(actual);
			Assertion.assertUnsubscribe(message, unsubscribe);
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
		Unsubscribe Unsubscribe = new Unsubscribe(0, TOPIC);
		ByteBuf buf = Parser.encode(Unsubscribe);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicIDZero()
	{
		Topic topic = new PredefinedTopic(0, QoS.AT_MOST_ONCE);
		Unsubscribe Unsubscribe = new Unsubscribe(MESSAGE_ID, topic);
		ByteBuf buf = Parser.encode(Unsubscribe);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicID65535()
	{
		Topic topic = new PredefinedTopic(65535, QoS.AT_MOST_ONCE);
		Unsubscribe Unsubscribe = new Unsubscribe(MESSAGE_ID, topic);
		ByteBuf buf = Parser.encode(Unsubscribe);
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
			NamedTopic topic = new NamedTopic(topicName, QoS.EXACTLY_ONCE);
			Unsubscribe expected = new Unsubscribe(MESSAGE_ID, topic);
			assertEquals(7 + expected.getTopic().length(), expected.getLength());
			ByteBuf buf = Parser.encode(expected);
			Unsubscribe actual = (Unsubscribe) Parser.decode(buf);
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
		{ 0x07, 0x14, (byte) (0x01 + Flag.RETAIN.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsDup()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x14, (byte) (0x01 + Flag.DUPLICATE.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsWill()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x14, (byte) (0x01 + Flag.WILL.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsCleanSession()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x14, (byte) (0x01 + Flag.CLEAN_SESSION.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsTopicType()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x14, (byte) (0x01 + Flag.RESERVED_TOPIC.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsQoS1()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x14, (byte) (0x01 + Flag.QOS_1.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsQoS2()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x14, (byte) (0x01 + Flag.QOS_2.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsQoSLevelOne()
	{
		byte[] invalidLengthArray =
		{ 0x07, 0x14, (byte) (0x01 + Flag.QOS_LEVEL_ONE.getValue()), 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}
}
