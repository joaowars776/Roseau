/*
 * Copyright (c) 2012 Quackster <alex.daniel.97@gmail>. 
 * 
 * This file is part of Sierra.
 * 
 * Sierra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Sierra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Sierra.  If not, see <http ://www.gnu.org/licenses/>.
 */

package org.alexdev.roseau.server.netty.codec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.alexdev.roseau.log.Log;
import org.alexdev.roseau.messages.OutgoingMessageComposer;
import org.alexdev.roseau.server.netty.readers.NettyResponse;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class NetworkEncoder extends SimpleChannelHandler {
	
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
		
		Charset charset = Charset.forName("ISO-8859-1");
		
		try {
			
			if (e.getMessage() instanceof String) {
				Channels.write(ctx, e.getFuture(), ChannelBuffers.copiedBuffer((String) e.getMessage(), charset));
				return;
			}
			
			if (e.getMessage() instanceof OutgoingMessageComposer) {
				
				OutgoingMessageComposer msg = (OutgoingMessageComposer) e.getMessage();
				NettyResponse response = new NettyResponse();
				
				msg.write(response);
				
				Log.println("SENT: " + response.getBodyString() );
				
				//ChannelBuffer buffer = (ChannelBuffer)response.get();
				//Channels.write(ctx, e.getFuture(), (ChannelBuffer)e.getMessage());
				
				Channels.write(ctx, e.getFuture(), ChannelBuffers.copiedBuffer(response.get(), charset));
				
				return;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
