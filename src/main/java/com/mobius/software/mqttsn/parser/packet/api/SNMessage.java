package com.mobius.software.mqttsn.parser.packet.api;

import com.mobius.software.mqttsn.parser.avps.*;
import com.mobius.software.mqttsn.parser.packet.impl.*;

import io.netty.buffer.Unpooled;

public abstract class SNMessage
{
	public abstract int getLength();

	public abstract SNType getType();

	public static Builder builder()
	{
		return new Builder();
	}

	public static class Builder
	{
		public SubscribeBuilder subscribe()
		{
			return new SubscribeBuilder();
		}

		public SubackBuilder suback()
		{
			return new SubackBuilder();
		}

		public ConnectBuilder connect()
		{
			return new ConnectBuilder();
		}

		public ConnackBuilder connack()
		{
			return new ConnackBuilder();
		}

		public UnsubscribeBuilder unsubscribe()
		{
			return new UnsubscribeBuilder();
		}

		public UnsubackBuilder unsuback()
		{
			return new UnsubackBuilder();
		}

		public PublishBuilder publish()
		{
			return new PublishBuilder();
		}

		public PubackBuilder puback()
		{
			return new PubackBuilder();
		}

		public PubrecBuilder pubrec()
		{
			return new PubrecBuilder();
		}

		public PubrelBuilder pubrel()
		{
			return new PubrelBuilder();
		}

		public PubcompBuilder pubcomp()
		{
			return new PubcompBuilder();
		}

		public RegisterBuilder register()
		{
			return new RegisterBuilder();
		}

		public RegackBuilder regack()
		{
			return new RegackBuilder();
		}

		public DisconnectBuilder disconnect()
		{
			return new DisconnectBuilder();
		}

		public PingreqBuilder pingreq()
		{
			return new PingreqBuilder();
		}

		public static class SubscribeBuilder
		{
			private boolean dup;
			private Integer messageID;
			private SNTopic topic;

			public SNSubscribe build()
			{
				return new SNSubscribe(messageID, topic, dup);
			}

			public SubscribeBuilder duplicate()
			{
				this.dup = true;
				return this;
			}

			public SubscribeBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public SubscribeBuilder topic(String name, int qos)
			{
				this.topic = new FullTopic(name, SNQoS.valueOf(qos));
				return this;
			}

			public SubscribeBuilder topic(int code, int qos)
			{
				this.topic = new IdentifierTopic(code, SNQoS.valueOf(qos));
				return this;
			}

			public SubscribeBuilder shortTopic(String code, int qos)
			{
				this.topic = new ShortTopic(code, SNQoS.valueOf(qos));
				return this;
			}
		}

		public static class SubackBuilder
		{
			private Integer topicID;
			private Integer messageID;
			private ReturnCode code;
			private SNQoS allowedQos;

			public SubackBuilder topicID(int topicID)
			{
				this.topicID = topicID;
				return this;
			}

			public SubackBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public SubackBuilder code(ReturnCode code)
			{
				this.code = code;
				return this;
			}

			public SubackBuilder allowedQos(int allowedQos)
			{
				this.allowedQos = SNQoS.valueOf(allowedQos);
				return this;
			}

			public SNSuback build()
			{
				return new SNSuback(topicID, messageID, code, allowedQos);
			}
		}

		public static class ConnectBuilder
		{
			private boolean willPresent;
			private boolean cleanSession;
			private int duration;
			private String clientID;

			public ConnectBuilder willPresent()
			{
				this.willPresent = true;
				return this;
			}

			public ConnectBuilder cleanSession()
			{
				this.cleanSession = true;
				return this;
			}

			public ConnectBuilder duration(int duration)
			{
				this.duration = duration;
				return this;
			}

			public ConnectBuilder clientID(String clientID)
			{
				this.clientID = clientID;
				return this;
			}

			public SNConnect build()
			{
				return new SNConnect(cleanSession, duration, clientID, willPresent);
			}
		}

		public static class ConnackBuilder
		{
			private ReturnCode code;

			public ConnackBuilder code(ReturnCode code)
			{
				this.code = code;
				return this;
			}

			public SNConnack build()
			{
				return new SNConnack(code);
			}
		}

		public static class UnsubscribeBuilder
		{
			private int messageID;
			private SNTopic topic;

			public UnsubscribeBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public UnsubscribeBuilder topic(int code, int qos)
			{
				this.topic = new IdentifierTopic(code, SNQoS.valueOf(qos));
				return this;
			}

			public UnsubscribeBuilder shortTopic(String code, int qos)
			{
				this.topic = new ShortTopic(code, SNQoS.valueOf(qos));
				return this;
			}

			public SNUnsubscribe build()
			{
				return new SNUnsubscribe(messageID, topic);
			}
		}

		public static class UnsubackBuilder
		{
			private int messageID;

			public UnsubackBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public SNUnsuback build()
			{
				return new SNUnsuback(messageID);
			}
		}

		public static class PublishBuilder
		{
			private Integer messageID;
			private SNTopic topic;
			private byte[] content = new byte[0];
			private boolean dup;
			private boolean retain;

			public PublishBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public PublishBuilder topic(int code, int qos)
			{
				this.topic = new IdentifierTopic(code, SNQoS.valueOf(qos));
				return this;
			}

			public PublishBuilder shortTopic(String code, int qos)
			{
				this.topic = new ShortTopic(code, SNQoS.valueOf(qos));
				return this;
			}

			public PublishBuilder content(byte[] content)
			{
				this.content = content;
				return this;
			}

			public PublishBuilder duplicate()
			{
				this.dup = true;
				return this;
			}

			public PublishBuilder retain()
			{
				this.retain = true;
				return this;
			}

			public SNPublish build()
			{
				return new SNPublish(messageID, topic, Unpooled.copiedBuffer(content), dup, retain);
			}
		}

		public static class PubackBuilder
		{
			private int topicID;
			private int messageID;
			private ReturnCode code;

			public PubackBuilder topicID(int topicID)
			{
				this.topicID = topicID;
				return this;
			}

			public PubackBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public PubackBuilder code(ReturnCode code)
			{
				this.code = code;
				return this;
			}

			public SNPuback build()
			{
				return new SNPuback(topicID, messageID, code);
			}
		}

		public static class PubrecBuilder
		{
			private int messageID;

			public PubrecBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public SNPubrec build()
			{
				return new SNPubrec(messageID);
			}
		}

		public static class PubrelBuilder
		{
			private int messageID;

			public PubrelBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public SNPubrel build()
			{
				return new SNPubrel(messageID);
			}
		}

		public static class PubcompBuilder
		{
			private int messageID;

			public PubcompBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public SNPubcomp build()
			{
				return new SNPubcomp(messageID);
			}
		}

		public static class RegisterBuilder
		{
			private int topicID;
			private int messageID;
			private String topicName;

			public RegisterBuilder topicID(int topicID)
			{
				this.topicID = topicID;
				return this;
			}

			public RegisterBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public RegisterBuilder topicName(String topicName)
			{
				this.topicName = topicName;
				return this;
			}

			public Register build()
			{
				return new Register(topicID, messageID, topicName);
			}
		}

		public static class RegackBuilder
		{
			private int topicID;
			private int messageID;
			private ReturnCode code;

			public RegackBuilder topicID(int topicID)
			{
				this.topicID = topicID;
				return this;
			}

			public RegackBuilder messageID(int messageID)
			{
				this.messageID = messageID;
				return this;
			}

			public RegackBuilder code(ReturnCode code)
			{
				this.code = code;
				return this;
			}

			public Regack build()
			{
				return new Regack(topicID, messageID, code);
			}
		}

		public static class DisconnectBuilder
		{
			private int duration;

			public DisconnectBuilder duration(int duration)
			{
				this.duration = duration;
				return this;
			}

			public SNDisconnect build()
			{
				return new SNDisconnect(duration);
			}
		}

		public static class PingreqBuilder
		{
			private String clientID;

			public PingreqBuilder clientID(String clientID)
			{
				this.clientID = clientID;
				return this;
			}

			public SNPingreq build()
			{
				return new SNPingreq(clientID);
			}
		}
	}
}
