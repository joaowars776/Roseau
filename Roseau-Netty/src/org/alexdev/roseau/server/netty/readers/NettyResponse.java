package org.alexdev.roseau.server.netty.readers;

import java.io.IOException;
import java.nio.charset.Charset;

import org.alexdev.roseau.server.messages.Response;
import org.alexdev.roseau.server.messages.SerializableObject;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;

public class NettyResponse implements Response {

	private String header;
	private boolean finalised;
	private StringBuilder buffer;

	public NettyResponse() { }

	@Override
	public void init(String header) {

		this.finalised = false;
		this.header = header;
		this.buffer = new StringBuilder();

		this.buffer.append('#');
		this.append(header);
	}

	@Override
	public void append(String s) {
		this.buffer.append(s);
	}

	@Override
	public void appendArgument(String arg) {
		appendArgument(arg, ' ');
	}

	@Override
	public void appendNewArgument(String arg) {
		appendArgument(arg, (char)13);
	}

	@Override
	public void appendPartArgument(String arg) {
		appendArgument(arg, '/');
	}

	@Override
	public void appendTabArgument(String arg) {
		appendArgument(arg, (char)9);
	}

	@Override
	public void appendKVArgument(String key, String value) {
			this.buffer.append((char)13);
			this.buffer.append(key);
			this.buffer.append('=');
			this.buffer.append(value);
	}

	@Override
	public void appendKV2Argument(String key, String value) {
			this.buffer.append((char)13);
			this.buffer.append(key);
			this.buffer.append(':');
			this.buffer.append(value);
	}

	@Override
	public void appendArgument(String arg, char delimiter) {
			this.buffer.append(delimiter);
			this.buffer.append(arg);	
	}


	@Override
	public void appendObject(SerializableObject obj) {
		obj.serialise(this);
	}

	@Override
	public String getBodyString() {
		String str = this.get();
		for (int i = 0; i < 14; i++) { 
			str = str.replace(Character.toString((char)i), "[" + i + "]");
		}
		return str;
	}

	@Override
	//# @type \r data\r##
	public String get() {

		if (!this.finalised) {
				this.buffer.append('#');
				this.buffer.append('#');
			this.finalised = true;
		}
		
		return this.buffer.toString();
	}

	public String getHeader() {
		return header;
	}	
}
