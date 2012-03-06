package com.rapidftr.controls;

import java.io.IOException;
import javax.microedition.content.ContentHandlerException;
import javax.microedition.content.Invocation;
import javax.microedition.content.Registry;

public class PlayerField {

	public void onAudioButtonChanged(String filePath) {
		if (filePath != null) {
			Invocation invocation = new Invocation(filePath);
			Registry reg = Registry
					.getRegistry("net.rim.device.api.content.BlackBerryContentHandler");
			invocation.setResponseRequired(false);
			try {
				reg.invoke(invocation);
			} catch (ContentHandlerException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}

	}

}
