package com.rapidftr.screens;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.controls.Button;
import com.rapidftr.controls.ScrollableImageField;
import com.rapidftr.model.Child;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.ImageHelper;
import com.rapidftr.controllers.ViewChildPhotoGalleryController;

public class ViewChildPhotoGalleryScreen extends CustomScreen {

	private LabelField childName;
	private Child curChild;
	private int headerHeight = 0;
	private SeparatorField separatorField;
	private static final String FILE_STORE_HOME_USER = "file:///store/home/user";

	BitmapField[] imageFields;
	int imageWidth = 80;
	int imageHeight = 80;
	int imageMargin = 10;
	String focusedImageName;

	private String[] imageLocations;

	public void setChild(Child child) {
		this.curChild = child;
		clearFields();
		childName = new LabelField(child.getField("name"));
		add(childName);
		separatorField = new SeparatorField();
		add(separatorField);
	}

	private void onAddPhotoButtonClicked() {
		Dialog.alert("Add Photo Clicked");
	}

	protected void onUiEngineAttached(boolean attached) {
		super.onUiEngineAttached(attached);
		setHeaderHeight();
		prepareImageFields();
	}

	private void setHeaderHeight() {
		int newHeaderHeight = separatorField.getTop()
				+ separatorField.getHeight();
		if (headerHeight != newHeaderHeight)
			headerHeight = newHeaderHeight;
	}

	private void setImageLocations() {

		try {
			imageLocations = curChild.getImageLocations();
		} catch (Exception ex) {
			// String imageLocations[] = { "default.jpg", "default.jpg",
			// "default.jpg", "default.jpg" };
			this.imageLocations = imageLocations;
		}
	}

	public void prepareImageFields() {
		setImageLocations();
		imageFields = new BitmapField[imageLocations.length];
		for (int i = 0; i < imageLocations.length; i++) {
			imageFields[i] = new BitmapField(
					new ImageHelper().getImage(getStorePath() + "/pictures/"
							+ imageLocations[i] + ".jpg"),
					BitmapField.FOCUSABLE) {
				protected boolean navigationClick(int status, int time) {
					((ViewChildPhotoGalleryController) controller)
							.viewSelectedPhoto(getStorePath() + "/pictures/"
									+ imageLocations[getFocusedImageIndex()]
									+ ".jpg");
					return true;
				}
			};
			imageFields[i].setMargin(imageMargin, imageMargin, imageMargin,
					imageMargin);

			imageFields[i].setFocusListener(new FocusChangeListener() {
				public void focusChanged(Field field, int eventType) {
					if (eventType == FOCUS_GAINED) {
						Border blueBorder = BorderFactory.createSimpleBorder(
								new XYEdges(2, 2, 2, 2), new XYEdges(
										Color.BLUE, Color.BLUE, Color.BLUE,
										Color.BLUE), Border.STYLE_SOLID);
						field.setBorder(blueBorder);
						setFocusedImage(field);
					} else if (eventType == FOCUS_LOST) {
						field.setBorder(null);
						setFocusedImage(null);
					}
				}
			});

			add(imageFields[i]);
		}
	}

	// private EncodedImage sizeImage(EncodedImage image, int width, int height)
	// {
	// EncodedImage result = null;
	//
	// int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
	// int currentHeightFixed32 = Fixed32.toFP(image.getHeight());
	//
	// int requiredWidthFixed32 = Fixed32.toFP(width);
	// int requiredHeightFixed32 = Fixed32.toFP(height);
	//
	// int scaleXFixed32 = Fixed32.div(currentWidthFixed32,
	// requiredWidthFixed32);
	// int scaleYFixed32 = Fixed32.div(currentHeightFixed32,
	// requiredHeightFixed32);
	//
	// result = image.scaleImage32(scaleXFixed32, scaleYFixed32);
	// return result;
	// }

	private void setFocusedImage(Field focusedImage) {
		if (focusedImage == null)
			focusedImageName = new String();
		else
			focusedImageName = ((BitmapField) focusedImage).toString();
	}

	protected void makeMenu(Menu menu, int instance) {
		menu.add(new MenuItem("Set as primary", 0, 1) {
			public void run() {
				onSetAsPrimaryClick();
			}
		});

		menu.add(new MenuItem("Add Photo", 1, 5) {
			public void run() {
				onAddPhotoClick();
			}
		});

		menu.add(new MenuItem("Delete Photo", 1, 4) {
			public void run() {
				onDeletePhotoClick();
			}
		});

		super.makeMenu(menu, instance);
	}

	private void onSetAsPrimaryClick() {
		Dialog.alert("" + getFocusedImageIndex());
	}

	private void onAddPhotoClick() {
		Dialog.alert("New Photo");
	}

	private void onDeletePhotoClick() {

	}

	private int getFocusedImageIndex() {
		for (int i = 0; i < imageFields.length; i++) {
			if (imageFields[i].isFocus()) {
				return i;
			}
		}
		return -1;
	}

	private String getStorePath() {
		String storePath = "";
		try {
			String sdCardPath = "file:///SDCard/Blackberry";
			FileConnection fc = (FileConnection) Connector.open(sdCardPath);
			if (fc.exists())
				storePath = sdCardPath;
			else
				storePath = FILE_STORE_HOME_USER;
		} catch (IOException ex) {
			storePath = FILE_STORE_HOME_USER;
		}
		return storePath;
	}
}