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
import com.mobius.software.mqttsn.parser.packet.impl.GWInfo;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class GWInfoTests
{
	private static final int ID = 5;
	private static final String ADDRESS = "192.168.0.1";
	private static GWInfo message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new GWInfo(ID, ADDRESS);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.GWINFO, message.getType());
			assertEquals(2, message.getType().getValue());
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
			assertEquals(3 + ADDRESS.length(), message.getLength());
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
			GWInfo gwInfo = new GWInfo(ID, ADDRESS);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(gwInfo);
			assertTrue(ByteBufUtil.equals(expected, actual));
			gwInfo = (GWInfo) Parser.decode(actual);
			Assertion.assertGWInfo(message, gwInfo);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testEmptyAddress()
	{
		try
		{
			GWInfo expected = new GWInfo(ID, null);
			GWInfo actual = (GWInfo) Parser.decode(Parser.encode(expected));
			Assertion.assertGWInfo(expected, actual);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
}
