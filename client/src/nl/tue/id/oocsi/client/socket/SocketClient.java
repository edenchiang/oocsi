package nl.tue.id.oocsi.client.socket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import nl.tue.id.oocsi.client.protocol.Handler;

public class SocketClient {

	private String name;
	private Map<String, Handler> channels;

	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;

	/**
	 * create a new socket client with the given name
	 * 
	 * @param name
	 * @param channels
	 */
	public SocketClient(String name, Map<String, Handler> channels) {
		this.name = name;
		this.channels = channels;
	}

	/**
	 * connect to OOCSI at address hostname:port
	 * 
	 * @param hostname
	 * @param port
	 * @return
	 */
	public boolean connect(String hostname, int port) {

		// connect
		try {
			socket = new Socket(hostname, port);
			output = new PrintWriter(socket.getOutputStream(), true);

			// send name
			output.println(name);

			input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			new Thread(new Runnable() {
				public void run() {
					try {
						String fromServer;
						while (!socket.isClosed()
								&& (fromServer = input.readLine()) != null) {
							if (fromServer.startsWith("send")) {
								// parse server output
								String[] tokens = fromServer.split(" ");
								if (tokens.length == 5) {
									String channel = tokens[1];
									String data = tokens[2];
									String timestamp = tokens[3];
									String sender = tokens[4];

									Handler c = channels.get(channel);
									if (c != null) {
										c.send(sender, data, timestamp);
									} else if (channel.equals(name)) {
										c = channels.get("SELF");
										if (c != null) {
											c.send(sender, data, timestamp);
										}
									}
								}
							}
						}

					} catch (IOException e) {
						// e.printStackTrace();
					} finally {
						output.close();
						try {
							input.close();
							socket.close();
						} catch (IOException e) {
							// e.printStackTrace();
						}

						System.out.println(" - OOCSI disconnected");
					}
				}
			}).start();

		} catch (UnknownHostException e) {
			System.out.println(" - OOCSI failed to connect (unknown host)");
			return false;
		} catch (ConnectException e) {
			System.out
					.println(" - OOCSI failed to connect (connection refused)");
			return false;
		} catch (IOException e) {
			System.out.println(" - OOCSI connection error");
			return false;
		}
		return true;
	}

	/**
	 * check if still connected to OOCSI
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return socket != null && !socket.isClosed() && socket.isConnected();
	}

	/**
	 * disconnect from OOCSI
	 * 
	 */
	public void disconnect() {
		// disconnect from server
		output.println("quit");

		try {
			output.close();
			input.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * subscribe to channel given by channelName
	 * 
	 * @param channelName
	 * @param handler
	 */
	public void subscribe(String channelName, Handler handler) {

		// register at server
		send("subscribe " + channelName);

		// add handler
		channels.put(channelName, handler);
	}

	/**
	 * subscribe to channel my own channel
	 * 
	 * @param handler
	 */
	public void subscribe(Handler handler) {

		// register at server
		send("subscribe " + name);

		// add handler
		channels.put("SELF", handler);
	}

	/**
	 * unsubscribe from channel given by channelName
	 * 
	 * @param channelName
	 */
	public void unsubscribe(String channelName) {

		// unregister at server
		send("unsubscribe " + channelName);

		// remove handler
		channels.remove(channelName);
	}

	/**
	 * unsubscribe from my channel
	 * 
	 */
	public void unsubscribe() {

		// unregister at server
		send("unsubscribe " + name);

		// remove handler
		channels.remove("SELF");
	}

	/**
	 * send raw message (no serialization)
	 * 
	 * @param channelName
	 * @param message
	 */
	public void send(String channelName, String message) {
		// send message
		send("sendraw " + channelName + " " + message);
	}

	/**
	 * send message with data payload (map of key value pairs which will be
	 * serialized before sending)
	 * 
	 * @param channelName
	 * @param data
	 */
	public void send(String channelName, Map<String, Object> data) {
		// send message with raw data
		send("send " + channelName + " " + serialize(data));
	}

	/**
	 * send the message to the socket
	 * 
	 * @param rawMessage
	 */
	private void send(String rawMessage) {
		if (isConnected() && output != null) {
			output.println(rawMessage);
		}
	}

	/**
	 * serialize a map of key value pairs
	 * 
	 * @param data
	 * @return
	 */
	private String serialize(Map<String, Object> data) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(data);
			byte[] rawData = baos.toByteArray();
			return new String(Base64Coder.encode(rawData));
		} catch (IOException e) {
			return "";
		}
	}

}