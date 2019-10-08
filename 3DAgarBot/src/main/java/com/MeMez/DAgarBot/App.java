package com.MeMez.DAgarBot;

import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
	static int maxBot = 100;// default 100
	static int loadedBots = 0;
	static int i = 0;
	static int delay = 100;
	static boolean isRunningBots = false;
	static boolean needSplit = false;
	static String BotNick = "123";
	static String serverOrigin = "http://asia2.biome3d.com";
	static Float playerX = (float) 0;
	static Float playerY = (float) 0;
	static String serverIp = "ws://asia2.biome3d.com/socket";
	static ExecutorService botExecutor = Executors.newFixedThreadPool(maxBot);

	public static void main(String[] args) throws InterruptedException, IOException {
		System.out.println("Enter botCount: ");
		Scanner scanner = new Scanner(System.in);
		App.maxBot = Integer.parseInt(scanner.nextLine());
		System.out.println("Enter delay: ");
		Scanner scanner1 = new Scanner(System.in);
		App.delay = Integer.parseInt(scanner1.nextLine());
		ClientWebSocketServer.startServer();
	}

	public static void startBot() throws InterruptedException {
		Timer execute = new Timer();
		execute.schedule(new TimerTask() {

			@Override
			public void run() {
				if (i < maxBot) {
					System.out.println("Executed BotThread: " + i);
					botExecutor.execute(new BotThread());
					loadedBots++;
					i++;
				}
			}
		}, 0, 500);

	}

	public static void executeBotThread() {
		loadedBots++;
		botExecutor.execute(new BotThread());
	}
}
