package org.example;

import javafx.scene.media.AudioClip;

public class SoundController {

	AudioClip clickSound;

	public void initialize() {
		String soundPath = getClass().getResource("/sounds/click.wav").toExternalForm();
		clickSound = new AudioClip(soundPath);
		clickSound.play(0.0);
	}

	public void playMoveSound() {
		clickSound.play(1.0);
	}
}
