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
				assertConnack((Connack) expected, (Connack) actual);
				break;
			case CONNECT:
				assertConnect((Connect) expected, (Connect) actual);
				break;
			case DISCONNECT:
				assertDisconnect((Disconnect) expected, (Disconnect) actual);
				break;
			case GWINFO:
				assertGWInfo((GWInfo) expected, (GWInfo) actual);
				break;
			case PINGREQ:
				assertPingreq((Pingreq) expected, (Pingreq) actual);
				break;
			case PINGRESP:
				assertConstants(expected, actual);
				break;
			case PUBACK:
				assertPuback((Puback) expected, (Puback) actual);
				break;
			case PUBCOMP:
				assertPubcomp((Pubcomp) expected, (Pubcomp) actual);
				break;
			case PUBLISH:
				assertPublish((Publish) expected, (Publish) actual);
				break;
			case PUBREC:
				assertPubrec((Pubrec) expected, (Pubrec) actual);
				break;
			case PUBREL:
				assertPubrel((Pubrel) expected, (Pubrel) actual);
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
				assertSuback((Suback) expected, (Suback) actual);
				break;
			case SUBSCRIBE:
				assertSubscribe((Subscribe) expected, (Subscribe) actual);
				break;
			case UNSUBACK:
				assertUnsuback((Unsuback) expected, (Unsuback) actual);
				break;
			case UNSUBSCRIBE:
				assertUnsubscribe((Unsubscribe) expected, (Unsubscribe) actual);
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
		boolean doContinue;
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
			doContinue = true;
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

	public static void assertConnect(Connect expected, Connect actual)
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

	public static void assertConnack(Connack expected, Connack actual)
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

	public static void assertPublish(Publish expected, Publish actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopic(), actual.getTopic());
			assertEquals(expected.isDup(), actual.isDup());
			assertEquals(expected.isRetain(), actual.isRetain());
		}
	}

	public static void assertPuback(Puback expected, Puback actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopicID(), actual.getTopicID());
			assertEquals(expected.getCode(), actual.getCode());
		}
	}

	public static void assertPubrec(Pubrec expected, Pubrec actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getMessageID(), actual.getMessageID());
	}

	public static void assertPubrel(Pubrel expected, Pubrel actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getMessageID(), actual.getMessageID());
	}

	public static void assertPubcomp(Pubcomp expected, Pubcomp actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getMessageID(), actual.getMessageID());
	}

	public static void assertSubscribe(Subscribe expected, Subscribe actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopic(), actual.getTopic());
			assertEquals(expected.isDup(), actual.isDup());
		}
	}

	public static void assertSuback(Suback expected, Suback actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopicID(), actual.getTopicID());
			assertEquals(expected.getAllowedQos(), actual.getAllowedQos());
		}
	}

	public static void assertUnsubscribe(Unsubscribe expected, Unsubscribe actual)
	{
		if (assertConstants(expected, actual))
		{
			assertEquals(expected.getMessageID(), actual.getMessageID());
			assertEquals(expected.getTopic(), actual.getTopic());
		}
	}

	public static void assertUnsuback(Unsuback expected, Unsuback actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getMessageID(), actual.getMessageID());
	}

	public static void assertPingreq(Pingreq expected, Pingreq actual)
	{
		if (assertConstants(expected, actual))
			assertEquals(expected.getClientID(), actual.getClientID());
	}

	public static void assertDisconnect(Disconnect expected, Disconnect actual)
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
