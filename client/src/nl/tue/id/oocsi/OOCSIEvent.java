package nl.tue.id.oocsi;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * event class for receiving events from OOCSI
 * 
 * @author matsfunk
 */
public class OOCSIEvent {

	protected String channelName;
	protected String sender;

	protected Map<String, Object> data;
	protected Date timestamp;

	/**
	 * constructor (implicit timestamp upon creation)
	 * 
	 * @param channelName where to?
	 * @param data        what data?
	 * @param sender      who sends it?
	 */
	public OOCSIEvent(String channelName, Map<String, Object> data, String sender) {
		this(channelName, data, sender, new Date());
	}

	/**
	 * constructor
	 * 
	 * @param channelName where to?
	 * @param data        what data?
	 * @param sender      who sends it?
	 * @param timestamp   when? (as UNIX timestamp, long value)
	 */
	public OOCSIEvent(String channelName, Map<String, Object> data, String sender, long timestamp) {
		this(channelName, data, sender, new Date(timestamp));
	}

	/**
	 * constructor
	 * 
	 * @param channelName where to?
	 * @param data        what data?
	 * @param sender      who sends it?
	 * @param timestamp   when? (as Date object)
	 */
	public OOCSIEvent(String channelName, Map<String, Object> data, String sender, Date timestamp) {
		this.channelName = channelName;
		this.data = data;
		this.sender = sender;
		this.timestamp = timestamp;
	}

	/**
	 * get value for the given key as boolean
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		Object result = this.data.get(key);
		if (result != null) {
			if (result instanceof Boolean) {
				return ((Boolean) result).booleanValue();
			} else {
				try {
					return Boolean.parseBoolean(result.toString());
				} catch (NumberFormatException nfe) {
					return defaultValue;
				}
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * get value for the given key as int
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getInt(String key, int defaultValue) {
		Object result = this.data.get(key);
		if (result != null) {
			if (result instanceof Integer) {
				return ((Integer) result).intValue();
			} else {
				try {
					return Integer.parseInt(result.toString());
				} catch (NumberFormatException nfe) {
					return defaultValue;
				}
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * get value for the given key as long
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public long getLong(String key, long defaultValue) {
		Object result = this.data.get(key);
		if (result != null) {
			if (result instanceof Long) {
				return ((Long) result).longValue();
			} else {
				try {
					return Long.parseLong(result.toString());
				} catch (NumberFormatException nfe) {
					return defaultValue;
				}
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * get value for the given key as float
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public float getFloat(String key, float defaultValue) {
		Object result = this.data.get(key);
		if (result != null) {
			if (result instanceof Float) {
				return ((Float) result).floatValue();
			} else {
				try {
					return Float.parseFloat(result.toString());
				} catch (NumberFormatException nfe) {
					return defaultValue;
				}
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * get value for the given key as double
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public double getDouble(String key, double defaultValue) {
		Object result = this.data.get(key);
		if (result != null) {
			if (result instanceof Double) {
				return ((Double) result).doubleValue();
			} else {
				try {
					return Double.parseDouble(result.toString());
				} catch (NumberFormatException nfe) {
					return defaultValue;
				}
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * get the value for the given key as String
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		Object result = this.data.get(key);
		return result != null ? result.toString() : null;
	}

	/**
	 * get the value for the given key as String
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getString(String key, String defaultValue) {
		Object result = this.data.get(key);
		return result != null ? result.toString() : defaultValue;
	}

	////////////////////////////////////////////////////////////////////

	/**
	 * get the value for the given key as boolean array (boolean[])
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean[] getBooleanArray(String key, boolean[] defaultValue) {
		try {
			Object object = getObject(key);
			if (object instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<Boolean> list = (ArrayList<Boolean>) object;
				boolean[] array = new boolean[list.size()];
				for (int i = 0; i < list.size(); i++) {
					array[i] = list.get(i);
				}
				return array;
			}
			return (boolean[]) object;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * get the value for the given key as int array (int[])
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int[] getIntArray(String key, int[] defaultValue) {
		try {
			Object object = getObject(key);
			if (object instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<Number> list = (ArrayList<Number>) object;
				int[] array = new int[list.size()];
				for (int i = 0; i < list.size(); i++) {
					array[i] = ((Number) list.get(i)).intValue();
				}
				return array;
			}
			return (int[]) object;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * get the value for the given key as float array (float[])
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public float[] getFloatArray(String key, float[] defaultValue) {
		try {
			Object object = getObject(key);
			if (object instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<Number> list = (ArrayList<Number>) object;
				float[] array = new float[list.size()];
				for (int i = 0; i < list.size(); i++) {
					array[i] = ((Number) list.get(i)).floatValue();
				}
				return array;
			}
			return (float[]) object;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * get the value for the given key as long array (long[])
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public long[] getLongArray(String key, long[] defaultValue) {
		try {
			Object object = getObject(key);
			if (object instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<Number> list = (ArrayList<Number>) object;
				long[] array = new long[list.size()];
				for (int i = 0; i < list.size(); i++) {
					array[i] = ((Number) list.get(i)).longValue();
				}
				return array;
			}
			return (long[]) object;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * get the value for the given key as double array (double[])
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public double[] getDoubleArray(String key, double[] defaultValue) {
		try {
			Object object = getObject(key);
			if (object instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<Double> list = (ArrayList<Double>) object;
				double[] array = new double[list.size()];
				for (int i = 0; i < list.size(); i++) {
					array[i] = list.get(i);
				}
				return array;
			}
			return (double[]) object;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * get the value for the given key as String array (String[])
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String[] getStringArray(String key, String[] defaultValue) {
		try {
			Object object = getObject(key);
			if (object instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<String> list = (ArrayList<String>) object;
				String[] array = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					array[i] = list.get(i);
				}
				return array;
			}
			return (String[]) object;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * get the array value for the given key as array of type T. note that you need to call this with the type T
	 * specified.
	 * 
	 * example: getArray<Float>()
	 * 
	 * @param <T>
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] getArray(String key, T[] defaultValue) {
		try {
			return (T[]) getObject(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	////////////////////////////////////////////////////////////////////

	/**
	 * get the value for the given key as Object
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(String key) {
		return this.data.get(key);
	}

	/**
	 * check if the event contains data with the key
	 * 
	 * @param key
	 * @return
	 */
	public boolean has(String key) {
		return this.data.containsKey(key);
	}

	/**
	 * retrieve all keys from the event
	 * 
	 * @return
	 */
	public String[] keys() {
		return this.data.keySet().toArray(new String[] {});
	}

	/**
	 * get the name or handle of the sender who sent this event
	 * 
	 * @return
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * get the name of the recipient or channel that this event was sent to
	 * 
	 * @return
	 */
	public String getRecipient() {
		return channelName;
	}

	/**
	 * get the name of the recipient or channel that this event was sent to
	 * 
	 * @return
	 */
	public String getChannel() {
		return channelName;
	}

	/**
	 * get timestamp of this event as Date object
	 * 
	 * @return
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * get timestamp of this event as long value
	 * 
	 * @return
	 */
	public long getTime() {
		return timestamp.getTime();
	}

	/**
	 * get a String representation of this event
	 * 
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("Sender: " + getSender());
		sb.append("Recipient: " + getRecipient() + ", ");
		sb.append("Timestamp: " + getTimestamp() + ", ");
		sb.append("\n");
		for (String key : keys()) {
			sb.append(key + ": " + getString(key) + ", ");
		}
		sb.append("\n");
		return sb.toString();
	}
}
