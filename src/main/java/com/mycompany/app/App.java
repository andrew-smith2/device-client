package com.mycompany.app;

/**
 * DEvice Client
 *
 */
 
import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
 
 
public class App 
{

	private static String connString = "HostName=utopiahub1.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=FNtx8vg1ZGwnVGGukV8rf66//C1eL9xIsml+Kon30bQ=";
	private static IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;
	private static String deviceId = "device4";
	private static final int METHOD_SUCCESS = 200;
	private static final int METHOD_NOT_DEFINED = 404;

	public static void main(String[] args)
	  throws IOException, URISyntaxException
	{
	  System.out.println("Starting device sample...");

	  DeviceClient client = new DeviceClient(connString, protocol);

	  try
	  {
		client.open();
		client.subscribeToDeviceMethod(new DirectMethodCallback(), null, new DirectMethodStatusCallback(), null);
		System.out.println("Subscribed to direct methods. Waiting...");
	  }
	  catch (Exception e)
	  {
		System.out.println("On exception, shutting down \n" + " Cause: " + e.getCause() + " \n" +  e.getMessage());
		client.close();
		System.out.println("Shutting down...");
	  }

	  System.out.println("Press any key to exit...");
	  Scanner scanner = new Scanner(System.in);
	  scanner.nextLine();
	  scanner.close();
	  client.close();
	  System.out.println("Shutting down...");
	}

	
	protected static class DirectMethodStatusCallback implements IotHubEventCallback
	{
	  public void execute(IotHubStatusCode status, Object context)
	  {
		System.out.println("IoT Hub responded to device method operation with status " + status.name());
	  }
	}
	protected static class DirectMethodCallback implements com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceMethodCallback
	{
	  @Override
	  public DeviceMethodData call(String methodName, Object methodData, Object context)
	  {
		DeviceMethodData deviceMethodData;
		switch (methodName)
		{
		  case "writeLine" :
		  {
			int status = METHOD_SUCCESS;
			System.out.println(new String((byte[])methodData));
			deviceMethodData = new DeviceMethodData(status, "Executed direct method " + methodName);
			break;
		  }
		  default:
		  {
			int status = METHOD_NOT_DEFINED;
			deviceMethodData = new DeviceMethodData(status, "Not defined direct method " + methodName);
		  }
		}
		return deviceMethodData;
	  }
	}
	
	
}
