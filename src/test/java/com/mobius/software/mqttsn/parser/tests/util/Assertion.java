package com.mobius.software.mqttsn.parser.tests.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.ByteBufUtil;

import com.mobius.software.mqttsn.parser.packet.api.SNMessage;
import com.mobius.software.mqttsn.parser.packet.impl.*;

public class Assertion
{
	public static void assertMessage(SNMessage expected, SNMessage actual)
	{
		if (assertConstants(expected, actual))
		{
			switch (expected.getType())
			{
			case ADVERTISE:
				assertAdvertise((Advertise) expected, (Advertise) actual);
				break;
			case CONNACK:
				assertConnack((SNConnack) expected, (SNConnack) actual);
				break;
			case CONNECT:
				assertConnect((SNConnect) expected, (SNConnect) actual);
				break;
			case DISCONNECT:
				assertDisconnect((SNDisconnect) expected, (SNDisconnect) actual);
				break;
			case GWINFO:
				assertGWInfo((GWInfo) expected, (GWInfo) actual);
				break;
			case PINGREQ:
				assertPingreq((SNPingreq) expected, (SNPingreq) actual);
				break;
			case PINGRESP:
				assertConstants(expected, actual);
				break;
			case PUBACK:
				assertPuback((SNPuback) expected, (SNPuback) actual);
				break;
			case PUBCOMP:
				assertPubcomp((SNPubcomp) expected, (SNPubcomp) actual);
				break;
			case PUBLISH:
				assertPublish((SNPublish) expected, (SNPublish) actual);
				break;
			case PUBREC:
				assertPubrec((SNPubrec) expected, (SNPubrec) actual);
				break;
			case PUBREL:
				assertPubrel((SNPubrel) expected, (SNPubrel) actual);
				break;
			case REGACK:
				assertRegack((Regack) expected, (Regack) actual);
				break;
			case REGISTER:
				assertRegister((Register) expected, (Register) actual);
				break;
			case SEARCHGW:
				assertSearchGw((SearchGW) expected, (SearchGW) actual);
				break;
			case SUBACK:
				assertSuback((SNSuback) expected, (SNSuback) actual);
				break;
			case SUBSCRIBE:
				assertSubscribe((SNSubscribe) expected, (SNSubscribe) actual);
				break;
			case UNSUBACK:
				assertUnsuback((SNUnsuback) expected, (SNUnsuback) actual);
				break;
			case UNSUBSCRIBE:
				assertUnsubscribe((SNUnsubscribe) expected, (SNUnsubscribe) actual);
				break;
			case WILL_MSG:
				assertWillMsg((WillMsg) expected, (WillMsg) actual);
				break;
			case WILL_MSG_REQ:
				assertWillMsgReq((WillMsgReq) expected, (WillMsgReq) actual);
				break;
			case WILL_MSG_RESP:
				assertWillMsgResp((WillMsgResp) expected, (WillMsgResp) actual);
				break;
			case WILL_MSG_UPD:
				assertWillMsgUpd((WillMsgUpd) expected, (WillMsgUpd) actual);
				break;
			case WILL_TOPIC:
				assertWillTopic((WillTopic) expected, (WillTopic) actual);
				break;
			case WILL_TOPIC_REQ:
				assertWillTopicReq((WillTopicReq) expected, (WillTopicReq) actual);
				break;
			case WILL_TOPIC_RESP:
				assertWillTopicResp((WillTopicResp) expected, (WillTopicResp) actual);
				break;
			case WILL_TOPIC_UPD:
				assertWillTopicUpd((WillTopicUpd) expected, (WillTopicUpd) actual);
				break;
			case ENCAPSULATED:
				assertEncapsulated((Encapsulated) expected, (Encapsulated) actual);
				break;
			}
		}
	}

	public static void assertAdvertise(Advertise expected, Advertise actual)
	{
		if (assertConstants(expected, actual))
		{
			assertNotNull(actual);
			assertEquals(expected.getType(), actual.getType());
			assertEquals(expected.getLength(), actual.getLength());
			assertEquals(expected.getGwID(), actual.getGwID());
			assertEquals(expected.getDuration(), actual.getDuration());
		}
	}

	private static boolean assertConstants(SNMessage expected, SNMessage actual)
	{
		boolean doContinue = true;
		if (expected == null)
		{
			assertNull(actual);
			doContinue = false;
		}
		else
		{
			assertNotNull(actual);
			assertEquals(expected.getType(), actual.getType());
			assertEquals(expected.getLength(), actual.getLength());
		}
		return doContinue;
	}

	public static void assertSearchGw(SearchGW expected, SearchGW actual)
	{
		if (assertConstants(expected, actual))
		{
			assertNotNull(actual);
			assertEquals(expected.getType(), actual.getType());
			assertEquals(expected.getLength(), actual.getLength());
			assertEquals(expected.getRadius(), actual.getRadius());
		}
	}

	public static void assertGWInfo(GWInfo expected, GWInfo actual)
	{
		if (assertConstants(expected, actual))
		{
			assertNotNull(actual);
			assertEquals(expected.getType(), actual.getType());
			assertEquals(expected.getLength(), actual.getLength());
			assertEquals(expected.getGwAddress(), actual.getGwAddress());
			assertEquals(expected.getGwID(), actual.getGwID());
		}
	}

	public static void assertConnect(SNConnect expected, SNConnect actual)
	{
		if (assertConstants(expected, actual))
		{
			assertNotNull(actual);
			assertEquals(expected.getType(), actual.getType());
			assertEquals(expected.getLength(), actual.getLength());
			assertEquals(expected.getClientID(), actual.getClientID());
			assertEquals(expected.getDuration(), actual.getDuration());
			assertEquals(expected.getProtocolID(), actual.getProtocolID());
			assertEquals(expected.isCleanSession(), actual.isCleanSession());
			assertEquals(expected.isWillPresent(), actual.isWillPresent());
		}
	}

	public static void assertConnack(SNConnack expected, SNConnack actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getCode(), actual.getCode());
	}

	public static void assertWillTopicReq(WillTopicReq expected, WillTopicReq actual)
	{
		assertConstants(expected, actual);
	}

	public static void assertWillTopic(WillTopic expected, WillTopic actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getType(), actual.getType());
			assertEquals(expected.getLength(), actual.getLength());
			assertEquals(expected.getTopic().getQos(), actual.getTopic().getQos());
			assertEquals(expected.getTopic().getValue(), actual.getTopic().getValue());
			assertEquals(expected.isRetain(), actual.isRetain());
		}
	}

	public static void assertWillMsgReq(WillMsgReq expected, WillMsgReq actual)
	{
		assertConstants(expected, actual);
	}

	public static void assertWillMsg(WillMsg expected, WillMsg actual)
	{
		if (assertConstants(expected, actual))
			assertTrue(ByteBufUtil.equals(expected.getContent(), actual.getContent()));
	}

	public static void assertRegister(Register expected, Register actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopicID(), actual.getTopicID());
			assertEquals(expected.getTopicName(), actual.getTopicName());
		}
	}

	public static void assertRegack(Regack expected, Regack actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopicID(), actual.getTopicID());
			assertEquals(expected.getCode(), actual.getCode());
		}
	}

	public static void assertPublish(SNPublish expected, SNPublish actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopic(), actual.getTopic());
			assertEquals(expected.isDup(), actual.isDup());
			assertEquals(expected.isRetain(), actual.isRetain());
		}
	}

	public static void assertPuback(SNPuback expected, SNPuback actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopicID(), actual.getTopicID());
			assertEquals(expected.getCode(), actual.getCode());
		}
	}

	public static void assertPubrec(SNPubrec expected, SNPubrec actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getMessageID(), actual.getMessageID());
	}

	public static void assertPubrel(SNPubrel expected, SNPubrel actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getMessageID(), actual.getMessageID());
	}

	public static void assertPubcomp(SNPubcomp expected, SNPubcomp actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getMessageID(), actual.getMessageID());
	}

	public static void assertSubscribe(SNSubscribe expected, SNSubscribe actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopic(), actual.getTopic());
			assertEquals(expected.isDup(), actual.isDup());
		}
	}

	public static void assertSuback(SNSuback expected, SNSuback actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopicID(), actual.getTopicID());
			assertEquals(expected.getAllowedQos(), actual.getAllowedQos());
		}
	}

	public static void assertUnsubscribe(SNUnsubscribe expected, SNUnsubscribe actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopic(), actual.getTopic());
		}
	}

	public static void assertUnsuback(SNUnsuback expected, SNUnsuback actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getMessageID(), actual.getMessageID());
	}

	public static void assertPingreq(SNPingreq expected, SNPingreq actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getClientID(), actual.getClientID());
	}

	public static void assertDisconnect(SNDisconnect expected, SNDisconnect actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getDuration(), actual.getDuration());
	}

	public static void assertWillTopicUpd(WillTopicUpd expected, WillTopicUpd actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getTopic(), actual.getTopic());
			assertEquals(expected.isRetain(), actual.isRetain());
		}
	}

	public static void assertWillMsgUpd(WillMsgUpd expected, WillMsgUpd actual)
	{
		if (assertConstants(expected, actual))
			assertTrue(ByteBufUtil.equals(expected.getContent(), actual.getContent()));
	}

	public static void assertWillTopicResp(WillTopicResp expected, WillTopicResp actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getCode(), actual.getCode());
	}

	public static void assertWillMsgResp(WillMsgResp expected, WillMsgResp actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getCode(), actual.getCode());
	}

	public static void assertEncapsulated(Encapsulated expected, Encapsulated actual)
	{
		if (expected != null)
		{
			assertEquals(expected.getRadius(), actual.getRadius());
			assertMessage(expected.getMessage(), actual.getMessage());
		}
		else
			assertNull(actual.getMessage());
	}
}
