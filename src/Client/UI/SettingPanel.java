package Client.UI;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import Client.Entities.Entity;

import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class SettingPanel extends JPanel{
    ButtonGroup group = new ButtonGroup();
    JRadioButton hitBoxOn = new JRadioButton("On");
    JRadioButton hitBoxOff = new JRadioButton("Off");
    JButton exitBtn = new JButton("Exit");
    JLabel hitBoxLabel =new JLabel("HitBox");
    JLabel musicLabel = new JLabel("Music");
    JLabel soundLabel = new JLabel("Sound");
    JSlider musicVolume = new JSlider(-800,60,-230);
    JSlider soundVolume = new JSlider(-800,60,-230);
    Font pixelFont;

    public SettingPanel(PlayPanel playPanel){
        try {
            pixelFont =  Font.createFont(Font.TRUETYPE_FONT, new File("assets/PixelFont.ttf")).deriveFont(18f).deriveFont(Font.BOLD);
        } catch (IOException | FontFormatException ignored) {
        }
        setSize(300,160);
        setLayout(null);

        exitBtn.setBounds(260,0,40,40);
        exitBtn.setText("Exit");


        musicLabel.setBounds(10,60,60,40);
        musicVolume.setBounds(70,60,230,40);
        soundLabel.setBounds(10,100,60,40);
        soundVolume.setBounds(70,100,230,40);

        group.add(hitBoxOn);
        group.add(hitBoxOff);
        hitBoxLabel.setBounds(10,20,60,40);
        hitBoxOn.setBounds(70,20,80,40);
        hitBoxOff.setBounds(160,20,80,40);
        hitBoxOff.setSelected(true);

       
        setFontComps(exitBtn,musicLabel,soundLabel,hitBoxLabel,hitBoxOn,hitBoxOff);
        addComps(hitBoxLabel,hitBoxOn,hitBoxOff,musicLabel,musicVolume,soundLabel,soundVolume,exitBtn);

        hitBoxOn.addActionListener(e -> {
            Entity.showCollision = true;
        });
        hitBoxOff.addActionListener(e -> {
            Entity.showCollision = false;
        });
        musicVolume.addChangeListener(e -> {
            playPanel.getBgm().setVolume(musicVolume.getValue()/10f);
            playPanel.grabFocus();
        });
        soundVolume.addChangeListener(e -> {
            playPanel.getPlayer().setSoundVolumeControl(soundVolume.getValue()/10f);
            playPanel.grabFocus();
        });
        exitBtn.addActionListener(e -> {
            setVisible(false);
            playPanel.grabFocus();
        });

        
    }
    private void setFontComps(Component... components) {
        for (Component c:components) {
            c.setFont(pixelFont);
        }
    }

    private void addComps(Component... components) {

        for (Component c:components) {
            add(c);
        }

    }

    
}
