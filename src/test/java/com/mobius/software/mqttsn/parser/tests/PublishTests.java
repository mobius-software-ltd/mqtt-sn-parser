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
import com.mobius.software.mqttsn.parser.avps.PredefinedTopic;
import com.mobius.software.mqttsn.parser.avps.QoS;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.avps.Topic;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.Publish;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class PublishTests
{
	private static final int MESSAGE_ID = 22;
	private static final Topic TOPIC = new PredefinedTopic(33, QoS.EXACTLY_ONCE);
	private static ByteBuf content;
	private static final boolean DUP = false;
	private static final boolean RETAIN = true;
	private static Publish message;

	@Before
	public void setUp()
	{
		content = loadContent();
		message = new Publish(MESSAGE_ID, TOPIC, content, DUP, RETAIN);
	}

	private static ByteBuf loadContent()
	{
		byte[] value = "message".getBytes();
		ByteBuf content = Unpooled.buffer(value.length);
		content.writeBytes(value);
		return content;
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.PUBLISH, message.getType());
			assertEquals(0x0C, message.getType().getValue());
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
			assertEquals(7 + content.capacity(), message.getLength());
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
			Publish publish = new Publish(MESSAGE_ID, TOPIC, loadContent(), DUP, RETAIN);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(publish);
			assertTrue(ByteBufUtil.equals(expected, actual));
			publish = (Publish) Parser.decode(actual);
			Assertion.assertPublish(message, publish);
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
		{ 0x0f, 0x0c, 0x51, 0x00, 0x21, 0x00, 0x16, 0x6d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x0d, 0x0c, 0x51, 0x00, 0x21, 0x00, 0x16, 0x6d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsWill()
	{
		byte[] invalidFlagsArray =
		{ 0x0e, 0x0c, (byte) (0x51 + Flag.WILL.getValue()), 0x00, 0x21, 0x00, 0x16, 0x6d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsCleanSession()
	{
		byte[] invalidFlagsArray =
		{ 0x0e, 0x0c, (byte) (0x51 + Flag.CLEAN_SESSION.getValue()), 0x00, 0x21, 0x00, 0x16, 0x6d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsTopicType()
	{
		byte[] invalidFlagsArray =
		{ 0x0e, 0x0c, (byte) (0x51 + Flag.RESERVED_TOPIC.getValue()), 0x00, 0x21, 0x00, 0x16, 0x6d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidFlagsArray.length);
		buf.writeBytes(invalidFlagsArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidMessageIDZero()
	{
		Publish Publish = new Publish(0, TOPIC, loadContent(), DUP, RETAIN);
		ByteBuf buf = Parser.encode(Publish);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidMessageIDNonZeroWithQos0()
	{
		Topic topic = new PredefinedTopic(123, QoS.AT_LEAST_ONCE);
		Publish Publish = new Publish(0, topic, loadContent(), DUP, RETAIN);
		ByteBuf buf = Parser.encode(Publish);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicIDZero()
	{
		Topic topic = new PredefinedTopic(0, QoS.AT_MOST_ONCE);
		Publish Publish = new Publish(MESSAGE_ID, topic, loadContent(), DUP, RETAIN);
		ByteBuf buf = Parser.encode(Publish);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidTopicID65535()
	{
		Topic topic = new PredefinedTopic(65535, QoS.AT_MOST_ONCE);
		Publish Publish = new Publish(MESSAGE_ID, topic, loadContent(), DUP, RETAIN);
		ByteBuf buf = Parser.encode(Publish);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidEmptyContent()
	{
		Publish publish = new Publish(MESSAGE_ID, TOPIC, Unpooled.EMPTY_BUFFER, DUP, RETAIN);
		ByteBuf buf = Parser.encode(publish);
		Parser.decode(buf);
	}

	@Test
	public void testThreeOctetLength()
	{
		try
		{
			int size = 249;
			ByteBuf content = Unpooled.buffer(size);
			for (int i = 0; i < size; i++)
				content.writeByte(i);
			Publish expected = new Publish(MESSAGE_ID, TOPIC, content, DUP, RETAIN);
			assertEquals(9 + content.capacity(), expected.getLength());
			ByteBuf buf = Parser.encode(expected);
			Publish actual = (Publish) Parser.decode(buf);
			assertEquals(expected.getLength(), actual.getLength());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
}
