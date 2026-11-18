package org.example;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;

public class SoundController {

	private Clip moveClip;
	private Clip checkmateClip;

	public void initialize() {
		moveClip = loadClip("/sounds/move.wav");
		checkmateClip = loadClip("/sounds/checkmate.wav");
		warmUpAudioPlayer();
	}

	private Clip loadClip(String path) {
		try (
			InputStream raw = getClass().getResourceAsStream(path);
			BufferedInputStream buffered = new BufferedInputStream(raw);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(buffered)
		) {
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			return clip;
		} catch (Exception e) {
			throw new RuntimeException("Failed to load sound: " + path, e);
		}
	}

	public void playMoveSound() {
		moveClip.setFramePosition(0);
		moveClip.start();
	}

	public void playCheckmateSound() {
		checkmateClip.setFramePosition(0);
		checkmateClip.start();
	}

	private void warmUpAudioPlayer() {
		Clip clip = moveClip;
		if (clip == null) return;
		FloatControl gainControl = null;
		float originalGain = 0.0f;
		if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			originalGain = gainControl.getValue();
			float dB = (float) (20.0 * Math.log10(0.001)); // -60.0 dB
			gainControl.setValue(Math.max(dB, gainControl.getMinimum()));
		}
		clip.setFramePosition(0);
		clip.start();
		final FloatControl controlRef = gainControl;
		final float restoreValue = originalGain;
		new Thread(() -> {
			try {
				Thread.sleep(50);
			} catch (InterruptedException ignored) {}
			clip.stop();
			clip.setFramePosition(0);
			if (controlRef != null) {
				controlRef.setValue(restoreValue);
			}
		})
			.start();
	}
}
