package com.MeMez.DAgarBot;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Timer;
import java.util.TimerTask;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ClientWebSocketServer extends WebSocketServer {
	static int port = 8080;

	public ClientWebSocketServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public ClientWebSocketServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("[Server] User Connected" + handshake);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// broadcast( conn + " has left the room!" );
		System.out.println("[Server] User Disconnected " + reason);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		String arr[] = message.split(" ", 2);
		App.serverIp = arr[0];
		App.serverOrigin = arr[1];
		System.out.println("[Server] Recieved serverIp from client: "+arr[0]);
		System.out.println("[Server] Recieved serverOrigin from client: "+arr[1]);
		// ( message );
		// System.out.println( conn + ": " + message );
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		message.order(ByteOrder.LITTLE_ENDIAN);
		int opCode = message.get(0);
		switch (opCode) {
		case 0:// Start
			System.out.println("[Server] User Requested to StartBot");
			try {
				App.startBot();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 1:// movement
			App.playerX = message.getFloat(1);
			App.playerY = message.getFloat(5);
			break;
			//System.out.println("x: " + App.playerX + " y: " + App.playerY);

		}
	}

	public static void startServer() throws InterruptedException, IOException {
		final ClientWebSocketServer s = new ClientWebSocketServer(port);
		s.start();
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a specific
			// websocket
		}
	}

	@Override
	public void onStart() {
		System.out.println("[Server] ServerStarted !");
		System.out.println("[Server] Listening on Port: " + port);

		Timer sendBotCount = new Timer();
		sendBotCount.schedule(new TimerTask() {

			@Override
			public void run() {
				broadcast(String.valueOf(App.loadedBots));
			}
		}, 0, 1000);
	}

}