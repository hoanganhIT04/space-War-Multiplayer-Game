package Client.Utility;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {

    private Clip clip;
    private FloatControl volume;

    public Sound(String pathname) {
        try {
            File file = new File(pathname);

            // ===== CHECK FILE =====
            if (!file.exists()) {
                System.err.println("[Sound] File not found: " + pathname);
                clip = null;
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // volume có thể không tồn tại với một số clip
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            }

        } catch (Exception e) {
            System.err.println("[Sound] Failed to load sound: " + pathname);
            e.printStackTrace();
            clip = null;
        }
    }

    // ===== SAFE METHODS =====

    public void start() {
        if (clip == null)
            return;
        clip.start();
    }

    public void reset() {
        if (clip == null)
            return;
        clip.setMicrosecondPosition(0);
    }

    public void stop() {
        if (clip == null)
            return;
        clip.stop();
    }

    public void close() {
        if (clip == null)
            return;
        clip.close();
    }

    public void loop() {
        if (clip == null)
            return;
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // ===== VOLUME =====

    public void setVolume(float value) {
        if (volume == null)
            return;
        volume.setValue(value);
    }

    public float getVolume() {
        if (volume == null)
            return 0f;
        return volume.getValue();
    }

    public long length() {
        if (clip == null)
            return 0;
        return clip.getMicrosecondLength();
    }
}
