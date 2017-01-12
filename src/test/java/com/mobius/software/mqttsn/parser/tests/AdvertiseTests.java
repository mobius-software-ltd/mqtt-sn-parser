package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.impl.Advertise;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class AdvertiseTests
{
	private static Advertise message;
	private static final int GW_ID = 123;
	private static final int DURATION = 10;

	@BeforeClass
	public static void beforeClass()
	{
		message = new Advertise(GW_ID, DURATION);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.ADVERTISE, message.getType());
			assertEquals(0, message.getType().getValue());
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
			assertEquals(5, message.getLength());
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
			Advertise advertise = new Advertise(GW_ID, DURATION);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(advertise);
			assertTrue(ByteBufUtil.equals(expected, actual));
			advertise = (Advertise) Parser.decode(expected);
			Assertion.assertAdvertise(message, advertise);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
}
