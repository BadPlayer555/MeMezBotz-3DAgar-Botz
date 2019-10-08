package com.MeMez.DAgarBot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BotThread implements Runnable {
	
	private BotUserClient c = null;
	private BufferedReader fileReader = null;
	private static Random rand = new Random();
	
	private static String getRandFromArray(String[] items) {
		return items[rand.nextInt(items.length)];
	}
	
	public boolean getRandomBoolean() {
	    return rand.nextBoolean();
	}
	
	private void useHttpProxy() throws IOException {
		fileReader = new BufferedReader(new FileReader("Proxy//Http.txt"));
		List<String> data = new ArrayList<String>();
		String s;
		while ((s = fileReader.readLine()) != null) {
			data.add(s);
		}
		fileReader.close();
		String[] dataArray = data.toArray(new String[] {});
		String Randomdata = getRandFromArray(dataArray);
		String[] splitedRandomdata = Randomdata.split(":");
		int port = Integer.parseInt(splitedRandomdata[1]);
		c.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(splitedRandomdata[0], port)));
	}
	
	private void useSocksProxy() throws IOException {
		fileReader = new BufferedReader(new FileReader("Proxy//Socks.txt"));
		List<String> data = new ArrayList<String>();
		String s;
		while ((s = fileReader.readLine()) != null) {
			data.add(s);
		}
		fileReader.close();
		String[] dataArray = data.toArray(new String[] {});
		String Randomdata = getRandFromArray(dataArray);
		String[] splitedRandomdata = Randomdata.split(":");
		int port = Integer.parseInt(splitedRandomdata[1]);
		c.setProxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(splitedRandomdata[0], port)));
	}

	public void run() {
		Map<String, String> httpHeaders = new HashMap<String, String>();
		httpHeaders.put("Origin", App.serverOrigin);
		httpHeaders.put("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
		httpHeaders.put("Accept-Encoding", "gzip, deflate");
		httpHeaders.put("Accept-Language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7");
		httpHeaders.put("Cache-Control", "no-cache");
		// TODO Auto-generated method stub
		try {
			this.c = new BotUserClient(new URI(App.serverIp), httpHeaders);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(getRandomBoolean()) {
			try {
				useHttpProxy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				useSocksProxy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		c.connect();
	}

}
