package com.rapidftr.services;

import com.rapidftr.net.ControllerCallback;
import com.rapidftr.net.RequestCallBack;
import com.rapidftr.net.ScreenCallBack;
import com.sun.me.web.request.Response;

public class RequestCallBackImpl implements RequestCallBack {
	private ScreenCallBack screenCallback;
	private ControllerCallback controllerCallback;

	public RequestCallBackImpl(ScreenCallBack screenCallback,
			ControllerCallback controllerCallback) {
		super();
		this.screenCallback = screenCallback;
		this.controllerCallback = controllerCallback;
	}

	public void handleConnectionProblem() {
		screenCallback.handleConnectionProblem();
	}

	public void handleException(Exception exception) {
		controllerCallback.onRequestFailure(exception);
	}

	public void handleUnauthorized() {
		screenCallback.handleAuthenticationFailure();
	}

	public void onSuccess(Object context, Response result) {
		controllerCallback.onRequestSuccess(context, result);
	}

	public void writeProgress(Object context, int bytes, int total) {
		// TODO Auto-generated method stub

	}

	public void updateRequestProgress(int size) {
		screenCallback.updateRequestProgress(size);

	}

	public void onProcessComplete() {
		screenCallback.onProcessComplete();
		
	}

	public void onProcessFail() {
		screenCallback.onProcessFail();		
	}

}