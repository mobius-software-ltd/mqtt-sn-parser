package com.mobius.software.mqttsn.parser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.mobius.software.mqttsn.parser.avps.*;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;
import com.mobius.software.mqttsn.parser.packet.api.ResponseMessage;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;
import com.mobius.software.mqttsn.parser.packet.impl.*;
import com.mobius.software.mqttsn.parser.util.ValuesValidator;

public class Parser
{
	private static final String ENCODING = "UTF-8";
	private static final byte THREE_OCTET_LENGTH_SUFFIX = 0x01;

	public static final WillTopicReq WILL_TOPIC_REQ = new WillTopicReq();
	public static final WillMsgReq WILL_MSG_REQ = new WillMsgReq();
	public static final Pingresp PING_RESP = new Pingresp();

	public static SNMessage decode(ByteBuf buf)
	{
		SNMessage message = null;
		try
		{
			int currIndex = buf.readerIndex();
			int messageLength = decodeContentLength(buf);
			int bytesLeft = messageLength - (buf.readerIndex() - currIndex);

			short typeByte = buf.readUnsignedByte();
			SNType type = SNType.valueOf(typeByte);
			if (type == null)
				throw new MalformedMessageException("invalid packet type encoding:" + typeByte);
			bytesLeft--;

			switch (type)
			{
			case ADVERTISE:
				int advertiseGwID = buf.readUnsignedByte();
				int advertiseDuration = buf.readUnsignedShort();
				message = new Advertise(advertiseGwID, advertiseDuration);
				break;

			case SEARCHGW:
				int radius = buf.readUnsignedByte();
				message = new SearchGW(radius);
				break;

			case GWINFO:
				int gwInfoGwID = buf.readUnsignedByte();
				bytesLeft--;
				String gwInfoGwAddress = null;
				if (bytesLeft > 0)
				{
					byte[] gwInfoGwAddressBytes = new byte[bytesLeft];
					buf.readBytes(gwInfoGwAddressBytes);
					gwInfoGwAddress = new String(gwInfoGwAddressBytes);
				}
				message = new GWInfo(gwInfoGwID, gwInfoGwAddress);
				break;

			case CONNECT:
				Flags connectFlags = Flags.decode(buf.readByte(), type);
				bytesLeft--;
				int protocolID = buf.readByte();
				bytesLeft--;
				if (protocolID != Connect.MQTT_SN_PROTOCOL_ID)
					throw new MalformedMessageException("Invalid protocolID " + protocolID);
				int connectDuration = buf.readUnsignedShort();
				bytesLeft -= 2;
				if (!ValuesValidator.canRead(buf, bytesLeft))
					throw new MalformedMessageException(type + ", clientID can't be empty");
				byte[] connectClientIDBytes = new byte[bytesLeft];
				buf.readBytes(connectClientIDBytes);
				String connectClientID = new String(connectClientIDBytes, ENCODING);
				message = new Connect(connectFlags.isCleanSession(), connectDuration, connectClientID, connectFlags.isWill());
				break;

			case CONNACK:
				ReturnCode connackCode = ReturnCode.valueOf(buf.readByte());
				message = new Connack(connackCode);
				break;

			case WILL_TOPIC_REQ:
				message = WILL_TOPIC_REQ;
				break;

			case WILL_TOPIC:
				boolean willTopicRetain = false;
				NamedTopic willTopic = null;
				if (bytesLeft > 0)
				{
					Flags willTopicFlags = Flags.decode(buf.readByte(), type);
					bytesLeft--;
					willTopicRetain = willTopicFlags.isRetain();
					if (!ValuesValidator.canRead(buf, bytesLeft))
						throw new MalformedMessageException(type + " invalid topic encoding");
					byte[] willTopicBytes = new byte[bytesLeft];
					buf.readBytes(willTopicBytes);
					String willTopicValue = new String(willTopicBytes, ENCODING);
					willTopic = new NamedTopic(willTopicValue, willTopicFlags.getQos());
				}
				message = new WillTopic(willTopicRetain, willTopic);
				break;

			case WILL_MSG_REQ:
				message = WILL_MSG_REQ;
				break;

			case WILL_MSG:
				if (!ValuesValidator.canRead(buf, bytesLeft))
					throw new MalformedMessageException(type + " content must not be empty");
				ByteBuf willMessageContent = Unpooled.buffer(bytesLeft);
				buf.readBytes(willMessageContent);
				message = new WillMsg(willMessageContent);
				break;

			case REGISTER:
				int registerTopicID = buf.readUnsignedShort();
				if (!ValuesValidator.validateTopicID(registerTopicID))
					throw new MalformedMessageException(type + " invalid topicID value " + registerTopicID);
				bytesLeft -= 2;
				int registerMessageID = buf.readUnsignedShort();
				if (!ValuesValidator.validateMessageID(registerMessageID))
					throw new MalformedMessageException(type + " invalid messageID " + registerMessageID);
				bytesLeft -= 2;
				if (!ValuesValidator.canRead(buf, bytesLeft))
					throw new MalformedMessageException(type + " must contain a valid topic");
				byte[] registerTopicBytes = new byte[bytesLeft];
				buf.readBytes(registerTopicBytes);
				String registerTopicName = new String(registerTopicBytes, ENCODING);
				message = new Register(registerTopicID, registerMessageID, registerTopicName);
				break;

			case REGACK:
				int regackTopicID = buf.readUnsignedShort();
				if (!ValuesValidator.validateTopicID(regackTopicID))
					throw new MalformedMessageException(type + " invalid topicID value " + regackTopicID);
				int regackMessageID = buf.readUnsignedShort();
				if (!ValuesValidator.validateMessageID(regackMessageID))
					throw new MalformedMessageException(type + " invalid messageID " + regackMessageID);
				ReturnCode regackCode = ReturnCode.valueOf(buf.readByte());
				message = new Regack(regackTopicID, regackMessageID, regackCode);
				break;

			case PUBLISH:
				Flags publishFlags = Flags.decode(buf.readByte(), type);
				bytesLeft--;
				int publishTopicID = buf.readUnsignedShort();
				bytesLeft -= 2;
				int publishMessageID = buf.readUnsignedShort();
				bytesLeft -= 2;
				if (publishFlags.getQos() != QoS.AT_MOST_ONCE && publishMessageID == 0)
					throw new MalformedMessageException("invalid PUBLISH QoS-0 messageID:" + publishMessageID);
				if (!ValuesValidator.canRead(buf, bytesLeft))
					throw new MalformedMessageException(type + " content must not be empty");
				ByteBuf publishContent = Unpooled.buffer(bytesLeft);
				buf.readBytes(publishContent);
				Topic publishTopic = null;
				if (publishFlags.getTopicType() == TopicType.SHORT)
					publishTopic = new ShortTopic(String.valueOf(publishTopicID), publishFlags.getQos());
				else
				{
					if (!ValuesValidator.validateTopicID(publishTopicID))
						throw new MalformedMessageException(type + " invalid topicID value " + publishTopicID);
					publishTopic = new PredefinedTopic(publishTopicID, publishFlags.getQos());
				}
				message = new Publish(publishMessageID, publishTopic, publishContent, publishFlags.isDup(), publishFlags.isRetain());
				break;

			case PUBACK:
				int pubackTopicID = buf.readUnsignedShort();
				if (!ValuesValidator.validateTopicID(pubackTopicID))
					throw new MalformedMessageException(type + " invalid topicID value " + pubackTopicID);
				int pubackMessageID = buf.readUnsignedShort();
				if (!ValuesValidator.validateMessageID(pubackMessageID))
					throw new MalformedMessageException(type + " invalid messageID " + pubackMessageID);
				ReturnCode pubackCode = ReturnCode.valueOf(buf.readByte());
				message = new Puback(pubackTopicID, pubackMessageID, pubackCode);
				break;

			case PUBREC:
				int pubrecMessageID = buf.readUnsignedShort();
				if (!ValuesValidator.validateMessageID(pubrecMessageID))
					throw new MalformedMessageException(type + " invalid messageID " + pubrecMessageID);
				message = new Pubrec(pubrecMessageID);
				break;

			case PUBREL:
				int pubrelMessageID = buf.readUnsignedShort();
				if (!ValuesValidator.validateMessageID(pubrelMessageID))
					throw new MalformedMessageException(type + " invalid messageID " + pubrelMessageID);
				message = new Pubrel(pubrelMessageID);
				break;

			case PUBCOMP:
				int pubcompMessageID = buf.readUnsignedShort();
				if (!ValuesValidator.validateMessageID(pubcompMessageID))
					throw new MalformedMessageException(type + " invalid messageID " + pubcompMessageID);
				message = new Pubcomp(pubcompMessageID);
				break;

			case SUBSCRIBE:
				Flags subscribeFlags = Flags.decode(buf.readByte(), type);
				bytesLeft--;
				int subscribeMessageID = buf.readUnsignedShort();
				if (subscribeMessageID == 0)
					throw new MalformedMessageException(type + " invalid messageID " + subscribeMessageID);
				bytesLeft -= 2;
				if (!ValuesValidator.canRead(buf, bytesLeft) || bytesLeft < 2)
					throw new MalformedMessageException(type + " invalid topic encoding");
				byte[] subscribeTopicBytes = new byte[bytesLeft];
				buf.readBytes(subscribeTopicBytes);
				Topic subscribeTopic = null;
				switch (subscribeFlags.getTopicType())
				{
				case NAMED:
					String subscribeTopicName = new String(subscribeTopicBytes, ENCODING);
					subscribeTopic = new NamedTopic(subscribeTopicName, subscribeFlags.getQos());
					break;
				case PREDEFINED:
					int subscribeTopicID = ByteBuffer.wrap(subscribeTopicBytes).getShort();
					if (!ValuesValidator.validateTopicID(subscribeTopicID))
						throw new MalformedMessageException(type + " invalid topicID value " + subscribeTopicID);
					subscribeTopic = new PredefinedTopic(subscribeTopicID, subscribeFlags.getQos());
					break;
				case SHORT:
					String subscribeTopicShortName = new String(subscribeTopicBytes, ENCODING);
					subscribeTopic = new ShortTopic(subscribeTopicShortName, subscribeFlags.getQos());
					break;
				}
				message = new Subscribe(subscribeMessageID, subscribeTopic, subscribeFlags.isDup());
				break;

			case SUBACK:
				Flags subackFlags = Flags.decode(buf.readByte(), type);
				int subackTopicID = buf.readUnsignedShort();
				if (!ValuesValidator.validateTopicID(subackTopicID))
					throw new MalformedMessageException(type + " invalid topicID value " + subackTopicID);
				int subackMessageID = buf.readUnsignedShort();
				if (!ValuesValidator.validateMessageID(subackMessageID))
					throw new MalformedMessageException(type + " invalid messageID " + subackMessageID);
				ReturnCode subackCode = ReturnCode.valueOf(buf.readByte());
				message = new Suback(subackTopicID, subackMessageID, subackCode, subackFlags.getQos());
				break;

			case UNSUBSCRIBE:
				Flags unsubscribeFlags = Flags.decode(buf.readByte(), type);
				bytesLeft--;
				int unsubscribeMessageID = buf.readUnsignedShort();
				if (!ValuesValidator.validateMessageID(unsubscribeMessageID))
					throw new MalformedMessageException(type + " invalid messageID " + unsubscribeMessageID);
				bytesLeft -= 2;
				byte[] unsubscribeTopicBytes = new byte[bytesLeft];
				buf.readBytes(unsubscribeTopicBytes);
				Topic unsubscribeTopic = null;
				switch (unsubscribeFlags.getTopicType())
				{
				case NAMED:
					String unsubscribeTopicName = new String(unsubscribeTopicBytes, ENCODING);
					unsubscribeTopic = new NamedTopic(unsubscribeTopicName, unsubscribeFlags.getQos());
					break;
				case PREDEFINED:
					int unsubscribeTopicID = ByteBuffer.wrap(unsubscribeTopicBytes).getShort();
					if (!ValuesValidator.validateTopicID(unsubscribeTopicID))
						throw new MalformedMessageException(type + " invalid topicID value " + unsubscribeTopicID);
					unsubscribeTopic = new PredefinedTopic(unsubscribeTopicID, unsubscribeFlags.getQos());
					break;
				case SHORT:
					String unsubscribeTopicShortName = new String(unsubscribeTopicBytes, ENCODING);
					unsubscribeTopic = new ShortTopic(unsubscribeTopicShortName, unsubscribeFlags.getQos());
					break;
				}
				message = new Unsubscribe(unsubscribeMessageID, unsubscribeTopic);
				break;

			case UNSUBACK:
				int unsubackMessageID = buf.readUnsignedShort();
				if (!ValuesValidator.validateMessageID(unsubackMessageID))
					throw new MalformedMessageException(type + " invalid messageID " + unsubackMessageID);
				message = new Unsuback(unsubackMessageID);
				break;

			case PINGREQ:
				String pingreqClientID = null;
				if (bytesLeft > 0)
				{
					byte[] pingreqClientIDValue = new byte[bytesLeft];
					buf.readBytes(pingreqClientIDValue);
					pingreqClientID = new String(pingreqClientIDValue, ENCODING);
				}
				message = new Pingreq(pingreqClientID);
				break;

			case PINGRESP:
				message = PING_RESP;
				break;

			case DISCONNECT:
				int duration = 0;
				if (bytesLeft > 0)
					duration = buf.readUnsignedShort();
				message = new Disconnect(duration);
				break;

			case WILL_TOPIC_UPD:
				NamedTopic willTopicUpdTopic = null;
				boolean willTopicUpdateRetain = false;
				if (bytesLeft > 0)
				{
					Flags wilTopicUpdFlags = Flags.decode(buf.readByte(), type);
					bytesLeft--;
					byte[] willTopicUpdTopicBytes = new byte[bytesLeft];
					buf.readBytes(willTopicUpdTopicBytes);
					String willTopicUpdTopicValue = new String(willTopicUpdTopicBytes, ENCODING);
					willTopicUpdTopic = new NamedTopic(willTopicUpdTopicValue, wilTopicUpdFlags.getQos());
				}
				message = new WillTopicUpd(willTopicUpdateRetain, willTopicUpdTopic);
				break;

			case WILL_MSG_UPD:
				if (!ValuesValidator.canRead(buf, bytesLeft))
					throw new MalformedMessageException(type + " must contain content data");
				ByteBuf willMsgUpdContent = Unpooled.buffer(bytesLeft);
				buf.readBytes(willMsgUpdContent);
				message = new WillMsgUpd(willMsgUpdContent);
				break;

			case WILL_TOPIC_RESP:
				ReturnCode willTopicRespCode = ReturnCode.valueOf(buf.readByte());
				message = new WillTopicResp(willTopicRespCode);
				break;

			case WILL_MSG_RESP:
				ReturnCode willMsgRespCode = ReturnCode.valueOf(buf.readByte());
				message = new WillMsgResp(willMsgRespCode);
				break;

			case ENCAPSULATED:

				Controls control = Controls.decode(buf.readByte());
				bytesLeft--;
				byte[] wirelessNodeIDBytes = new byte[bytesLeft];
				buf.readBytes(wirelessNodeIDBytes);
				String wirelessNodeID = new String(wirelessNodeIDBytes, ENCODING);
				SNMessage encapsulatedMessage = Parser.decode(buf);
				message = new Encapsulated(control.getRadius(), wirelessNodeID, encapsulatedMessage);
				break;
			}

			if (buf.isReadable())
				throw new MalformedMessageException("not all bytes have been read from buffer:" + buf.readableBytes());

			if (messageLength != message.getLength())
				throw new MalformedMessageException(String.format("Invalid length. Encoded: %d, actual: %d", messageLength, message.getLength()));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new MalformedMessageException("packet contains invalid UTF-8 string encoding:" + e.getMessage());
		}

		return message;
	}

	private static int decodeContentLength(ByteBuf buf)
	{
		int length = 0;
		short firstLengthByte = buf.readUnsignedByte();
		if (firstLengthByte == THREE_OCTET_LENGTH_SUFFIX)
			length = buf.readUnsignedShort();
		else
			length = firstLengthByte;
		return length;
	}

	public static ByteBuf encode(SNMessage message)
	{
		int length = message.getLength();
		ByteBuf buf = Unpooled.buffer(length);
		if (length <= 255)
			buf.writeByte(length);
		else
		{
			buf.writeByte(THREE_OCTET_LENGTH_SUFFIX);
			buf.writeShort(length);
		}
		SNType type = message.getType();
		buf.writeByte(type.getValue());

		switch (type)
		{
		case ADVERTISE:
			Advertise advertise = (Advertise) message;
			buf.writeByte(advertise.getGwID());
			buf.writeShort(advertise.getDuration());
			break;

		case SEARCHGW:
			SearchGW searchGw = (SearchGW) message;
			buf.writeByte(searchGw.getRadius());
			break;

		case GWINFO:
			GWInfo gwInfo = (GWInfo) message;
			buf.writeByte(gwInfo.getGwID());
			if (gwInfo.getGwAddress() != null)
				buf.writeBytes(gwInfo.getGwAddress().getBytes());
			break;

		case CONNECT:
			Connect connect = (Connect) message;
			byte connectFlagsByte = Flags.encode(false, null, false, connect.isWillPresent(), connect.isCleanSession(), null);
			buf.writeByte(connectFlagsByte);
			buf.writeByte(connect.getProtocolID());
			buf.writeShort(connect.getDuration());
			buf.writeBytes(connect.getClientID().getBytes());
			break;

		case CONNACK:
		case WILL_TOPIC_RESP:
		case WILL_MSG_RESP:
			ResponseMessage responseMessage = (ResponseMessage) message;
			buf.writeByte(responseMessage.getCode().getValue());
			break;

		case WILL_TOPIC:
			WillTopic willTopic = (WillTopic) message;
			if (willTopic.getTopic() != null)
			{
				byte willTopicFlagsByte = Flags.encode(false, willTopic.getTopic().getQos(), willTopic.isRetain(), false, false, willTopic.getTopic().getType());
				buf.writeByte(willTopicFlagsByte);
				buf.writeBytes(willTopic.getTopic().getValue().getBytes());
			}
			break;

		case WILL_MSG:
			WillMsg willMsg = (WillMsg) message;
			buf.writeBytes(willMsg.getContent());
			break;

		case REGISTER:
			Register register = (Register) message;
			buf.writeShort(register.getTopicID());
			buf.writeShort(register.getMessageID());
			buf.writeBytes(register.getTopicName().getBytes());
			break;

		case REGACK:
			Regack regack = (Regack) message;
			buf.writeShort(regack.getTopicID());
			buf.writeShort(regack.getMessageID());
			buf.writeByte(regack.getCode().getValue());
			break;

		case PUBLISH:
			Publish publish = (Publish) message;
			byte publishFlagsByte = Flags.encode(publish.isDup(), publish.getTopic().getQos(), publish.isRetain(), false, false, publish.getTopic().getType());
			buf.writeByte(publishFlagsByte);
			buf.writeBytes(publish.getTopic().encode());
			buf.writeShort(publish.getMessageID());
			buf.writeBytes(publish.getContent());
			break;

		case PUBACK:
			Puback puback = (Puback) message;
			buf.writeShort(puback.getTopicID());
			buf.writeShort(puback.getMessageID());
			buf.writeByte(puback.getCode().getValue());
			break;

		case PUBREC:
		case PUBREL:
		case PUBCOMP:
		case UNSUBACK:
			CountableMessage contableMessage = (CountableMessage) message;
			buf.writeShort(contableMessage.getMessageID());
			break;

		case SUBSCRIBE:
			Subscribe subscribe = (Subscribe) message;
			byte subscribeFlags = Flags.encode(subscribe.isDup(), subscribe.getTopic().getQos(), false, false, false, subscribe.getTopic().getType());
			buf.writeByte(subscribeFlags);
			buf.writeShort(subscribe.getMessageID());
			buf.writeBytes(subscribe.getTopic().encode());
			break;

		case SUBACK:
			Suback suback = (Suback) message;
			byte subackByte = Flags.encode(false, suback.getAllowedQos(), false, false, false, null);
			buf.writeByte(subackByte);
			buf.writeShort(suback.getTopicID());
			buf.writeShort(suback.getMessageID());
			buf.writeByte(suback.getCode().getValue());
			break;

		case UNSUBSCRIBE:
			Unsubscribe unsubscribe = (Unsubscribe) message;
			byte unsubscribeFlags = Flags.encode(false, null, false, false, false, unsubscribe.getTopic().getType());
			buf.writeByte(unsubscribeFlags);
			buf.writeShort(unsubscribe.getMessageID());
			buf.writeBytes(unsubscribe.getTopic().encode());
			break;

		case PINGREQ:
			if (length > 2)
			{
				Pingreq pingreq = (Pingreq) message;
				buf.writeBytes(pingreq.getClientID().getBytes());
			}
			break;

		case DISCONNECT:
			if (length > 2)
			{
				Disconnect disconnect = (Disconnect) message;
				buf.writeShort(disconnect.getDuration());
			}
			break;

		case WILL_TOPIC_UPD:
			WillTopicUpd willTopicUpd = (WillTopicUpd) message;
			byte willTopicUpdByte = Flags.encode(false, willTopicUpd.getTopic().getQos(), willTopicUpd.isRetain(), false, false, null);
			buf.writeByte(willTopicUpdByte);
			buf.writeBytes(willTopicUpd.getTopic().getValue().getBytes());
			break;

		case WILL_MSG_UPD:
			WillMsgUpd willMsgUpd = (WillMsgUpd) message;
			buf.writeBytes(willMsgUpd.getContent());
			break;

		case WILL_TOPIC_REQ:
		case WILL_MSG_REQ:
		case PINGRESP:
			break;

		case ENCAPSULATED:
			Encapsulated encapsulated = (Encapsulated) message;
			buf.writeByte(Controls.encode(encapsulated.getRadius()));
			buf.writeBytes(encapsulated.getWirelessNodeID().getBytes());
			buf.writeBytes(Parser.encode(encapsulated.getMessage()));
			break;

		default:
			break;
		}

		if (type != SNType.ENCAPSULATED && message.getLength() != buf.readableBytes())
			throw new MalformedMessageException("invalid message encoding: expected length-" + message.getLength() + ",actual-" + buf.readableBytes());

		return buf;
	}
}
