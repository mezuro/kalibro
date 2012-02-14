package org.kalibro.desktop;

public class KalibroFrameManualTest implements KalibroFrameListener {

	public static void main(String[] args) {
		new KalibroFrame(new KalibroFrameManualTest()).setVisible(true);
	}

	@Override
	public void editSettings() {
		System.out.println("Edit settings");
	}
}