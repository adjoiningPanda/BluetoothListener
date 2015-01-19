/**
 * Created by Rabby on 4/2/2014.
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;

public class BluetoothTouchPadServer extends Thread {
	public static Vector<RemoteDevice> devicesDiscovered = new Vector<RemoteDevice>();
	// public static boolean connected;
	// Generic UUID to connect to typical android devices
	// "00001101-0000-1000-8000-00805F9B34FB";
	public static final String deviceUUID = "0000110100001000800000805F9B34FB";

	public BluetoothTouchPadServer() {
		setPriority(Thread.MAX_PRIORITY);
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		System.setProperty("bluecove.jsr82.psm_minimum_off", "true");

		
		
		// System.setProperty(BlueCoveConfigProperties.PROPERTY_JSR_82_PSM_MINIMUM_OFF,
		// "true");
		devicesDiscovered.clear();

		/*
		 * AndroidDeviceListener listener = new AndroidDeviceListener();
		 * 
		 * synchronized (listener.getInquiryCompletedEvent()) { boolean started
		 * = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(
		 * DiscoveryAgent.GIAC, listener); if (started) {
		 * System.out.println("wait for device inquiry to complete...");
		 * listener.getInquiryCompletedEvent().wait();
		 * System.out.println(listener.getDevicesDiscovered().size() +
		 * " device(s) found"); } } devicesDiscovered =
		 * listener.getDevicesDiscovered();
		 * 
		 * 
		 * //Use this to find what device to connect to //Needs to be a little
		 * more robust String myUUID = ""; for(RemoteDevice device :
		 * devicesDiscovered) { String friendlyName =
		 * device.getFriendlyName(true);
		 * if(friendlyName.equals("BluetoothTouchPad")){
		 * System.out.println(device.getBluetoothAddress()); myUUID =
		 * device.getBluetoothAddress(); }
		 * 
		 * }
		 */

		while (true) {
			LocalDevice localDevice = LocalDevice.getLocalDevice();

			try {
			localDevice.setDiscoverable(DiscoveryAgent.GIAC); // Advertising the
																// service
			}
			catch (Exception e){}

			String url = "btspp://localhost:" + deviceUUID
					+ ";name=BluetoothTouchPadServer";
			StreamConnectionNotifier server = (StreamConnectionNotifier) Connector
					.open(url);

			System.out.println("Waiting for connection...");
			StreamConnection connection = server.acceptAndOpen(); // Wait until
																	// client
																	// connects
			// === At this point, two devices should be connected ===//
			System.out.println("Connection was a success!");
			DataInputStream dataInputStream = connection.openDataInputStream();

			
			try {
				String c;
				while (true) {
					c = dataInputStream.readUTF();
					System.out.println(c);
					if (c == "x") {
						connection.close();
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				connection.close();
			}
		}
	}
}
