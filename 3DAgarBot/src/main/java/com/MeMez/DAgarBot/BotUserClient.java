package com.MeMez.DAgarBot;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class BotUserClient extends WebSocketClient {

	private long threadId = Thread.currentThread().getId();
	private String status = "dead";

	public BotUserClient(URI serverUri, Map<String, String> httpHeaders) {
		super(serverUri, httpHeaders);
		// TODO Auto-generated constructor stub
	}

	public void log(String msg) {
		System.out.println("BotID: " + threadId + " " + msg);
	}

	public void sendPacket(String packet) {
		if (!(getReadyState() == READYSTATE.OPEN)) {
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendPacket(packet);
		} else {
			send(packet);
		}

	}

	public void sendPacket(ByteBuffer packet) {
		if (getReadyState() == READYSTATE.CLOSING || getReadyState() == READYSTATE.CLOSED || getReadyState() == READYSTATE.NOT_YET_CONNECTED || getReadyState() == READYSTATE.CONNECTING) {
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendPacket(packet);
		} else {
			send(packet);
		}

	}

	public void sendPacket(byte[] packet) {
		if (getReadyState() == READYSTATE.CLOSING || getReadyState() == READYSTATE.CLOSED || getReadyState() == READYSTATE.NOT_YET_CONNECTED || getReadyState() == READYSTATE.CONNECTING) {
			try {
				Thread.sleep(750);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sendPacket(packet);
		} else {
			send(packet);
		}

	}

	public void spawn() {
		/*JsonObject userDetails = new JsonObject();
		userDetails.addProperty("cmd", "Start");
		userDetails.addProperty("name", App.BotNick);
		userDetails.addProperty("skin", "");
		sendPacket(userDetails.toString());*/
		sendPacket("{\"cmd\":\"start\",\"name\":\"MeMezBotz\",\"skin\":\"troll\"}");
	}

	public void moveTo() {
		ByteBuffer buf = ByteBuffer.allocate(9);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(0, (byte) 1);
		buf.putFloat(1, 100 * App.playerX);
		buf.putFloat(5, 100 * App.playerY);
		sendPacket(buf);
	}

	public void split() {
		sendPacket(new byte[] { 10 });
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		log("OpenedConnection: "+handshakedata);
		sendPacket(new byte[] { 1, 0, 0, 0, 0, 0, 0, 0, 0 });
		Timer sendMovePacket = new Timer();
		sendMovePacket.schedule(new TimerTask() {

			@Override
			public void run() {
				moveTo();
			}
		}, 0, 500);
		spawn();
	}

	@Override
	public void onMessage(ByteBuffer message) {
		message.order(ByteOrder.LITTLE_ENDIAN);
		int opcode = message.get(0);
		switch(opcode) {
		case 6:
			if(status.equals("dead")) {
				status = "spawned";
			}else if(status.equals("spawned")){
				status = "dead";
				spawn();
			}
		    break;
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// TODO Auto-generated method stub
		log("Closed - Reason: " + reason + " By remote? " + remote);
		App.loadedBots--;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		App.executeBotThread();
	}

	@Override
	public void onError(Exception ex) {
		// TODO Auto-generated method stub
		log("Error" + ex.toString());
		App.loadedBots--;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		App.executeBotThread();

	}

	@Override
	public void onMessage(String message) {
		// TODO Auto-generated method stub

	}

}
