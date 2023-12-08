/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package player;

import java.io.*;
import javax.swing.DefaultListModel;
import java.util.ArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JFrame;
import javax.swing.Timer;
import jaco.mp3.player.MP3Player;
import javax.swing.ImageIcon;

import javax.sound.sampled.*;



import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import static jdk.vm.ci.common.InitTimer.timer;

/**
 *
 * @author GPS
 */
public class PlayerMemo extends javax.swing.JFrame {
    private FileSystemView fileSystemView;
    Playlist pl=new Playlist();
    float currentVolume = 0.5f;
    ArrayList updateList= new ArrayList();
    private MP3Player mp3Player;
    javazoom.jl.player.Player player;
    
    File simpan;
    Timer timer;
    boolean playing;
    
    

    /**
     * Creates new form PlayerMemo
     */
    public PlayerMemo() {
        
        initComponents();
         mp3Player = new MP3Player();
        this.setLocationRelativeTo(null);
        timer = new Timer(1000, e -> updateSlider()); // Actualiza cada segundo
        timer.setInitialDelay(0);
        
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });
         jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
//                seekToPosition();
            }
        });
        
    }
    
    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {
        float newVolume = jSlider2.getValue() / 100.0f;
        adjustSystemVolume(newVolume);
    }
    
    
    
     
       private void adjustSystemVolume(float newVolume) {
        try {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            for (Mixer.Info info : mixerInfo) {
                Mixer mixer = AudioSystem.getMixer(info);
                if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                    Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
                    port.open();

                    if (port.isControlSupported(FloatControl.Type.VOLUME)) {
                        FloatControl volumeControl = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
                        volumeControl.setValue(newVolume);
                    }

                    port.close();
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
  

 private void adjustVolumeUp() {
        try {
            float step = 0.1f; // Adjust the step as needed
            float newVolume = Math.min(1.0f, jSlider2.getValue() + step);
            jSlider2.setValue((int) (newVolume * 100));

            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            for (Mixer.Info info : mixerInfo) {
                Mixer mixer = AudioSystem.getMixer(info);
                if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                    Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
                    port.open();

                    if (port.isControlSupported(FloatControl.Type.VOLUME)) {
                        FloatControl volumeControl = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
                        volumeControl.setValue(newVolume);
                    }

                    port.close();
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    // Function to decrease system volume
   private void adjustVolumeDown() {
        try {
            float step = 0.1f; // Adjust the step as needed
            float newVolume = Math.max(0.0f, jSlider2.getValue() - step);
            jSlider2.setValue((int) (newVolume * 100));

            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            for (Mixer.Info info : mixerInfo) {
                Mixer mixer = AudioSystem.getMixer(info);
                if (mixer.isLineSupported(Port.Info.SPEAKER)) {
                    Port port = (Port) mixer.getLine(Port.Info.SPEAKER);
                    port.open();

                    if (port.isControlSupported(FloatControl.Type.VOLUME)) {
                        FloatControl volumeControl = (FloatControl) port.getControl(FloatControl.Type.VOLUME);
                        volumeControl.setValue(newVolume);
                    }

                    port.close();
                }
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void updateSlider() {
        if (mp3Player != null) {
//            int currentPosition = (int) (mp3Player.getPlayTime() / 1000); // Position in seconds
//            jSlider1.setValue(currentPosition);
        }
    }
      
      
     
    
    public void updateList(){
    
        updateList=pl.getListSong();
        DefaultListModel model=new DefaultListModel();
        
        for(int i=0;i<updateList.size();i++)
            {
                int j=1+1;
                model.add(i,j + " | " + ((File) updateList.get(i)).getName());
                
                
            }
        jPlaylist.setModel(model);
           
    }
    
    
    // panel de control
    
    
    void pause() {
    if (mp3Player != null && mp3Player.isPaused()) {
        mp3Player.pause();
        a = 0;
        // Stop the timer when playback is paused
        timer.stop();
    }
}

    void add(){
    
        pl.add(this);
        updateList();
    }
    
    void remove(){
    
        try {
              int memoherdez=jPlaylist.getLeadSelectionIndex();
              pl.ls.remove(memoherdez);
              updateList();
            
        } catch (Exception e) {
        }
        
    }

     void up(){
    
         try {
                int s1=jPlaylist.getLeadSelectionIndex();
                simpan=(File)pl.ls.get(s1);
                pl.ls.remove(s1);
                pl.ls.add(s1-1,simpan);
                updateList();
                jPlaylist.setSelectedIndex(s1-1);
             
         } catch (Exception e) {
         }
        
    }
     
      void down(){
          
          try {
                int s1=jPlaylist.getLeadSelectionIndex();
                simpan=(File)pl.ls.get(s1);
                pl.ls.remove(s1);
                pl.ls.add(s1+1,simpan);
                updateList();
                jPlaylist.setSelectedIndex(s1+1);
             
         } catch (Exception e) {
         }
        
    }
      
      void open(){
    
        pl.openPls(this);
        updateList();
    }
      
      void save(){
    
       pl.saveAsPlaylist(this);
       updateList();
    }
      
       File play1;
      public static int a=0;
      
void putar() {
    if (a == 0) {
        try {
            int p1 = jPlaylist.getSelectedIndex();
            play1 = (File) this.updateList.get(p1);
            mp3Player = new MP3Player(play1.toURI().toURL()); 
            
            fileSystemView = FileSystemView.getFileSystemView();
            ImageIcon fileIcon = (ImageIcon) fileSystemView.getSystemIcon(play1);
            imagendecancion.setIcon(fileIcon);
            imagendecancion.setSize(150, 150);

            new Thread(() -> {
                try {
                    mp3Player.play();
                      a = 1;
                } catch (Exception e) {
                    System.out.println("Error playing music");
                    System.out.println(e);
                }
            }).start();

            // Start the timer when playback starts
            timer.start();
        } catch (Exception e) {
            System.out.println("Problem playing music");
            System.out.println(e);
        }
        System.out.println("se esta reporduciendo");
    } else {
        System.out.println("nos e esta reporduciendo");
        
        if (mp3Player.isPaused()) {
            // If the song is paused, resume
            mp3Player.play();
            a = 1;
            // Start the timer when playback resumes
            timer.start();
        } else {
            // If the song is playing, pause
            mp3Player.pause();
            a = 0;
            // Stop the timer when playback is paused
            timer.stop();
        }
    }
}

      File sa;
      void next() {
    if (a == 0) {
        try {
            int s1 = jPlaylist.getSelectedIndex() + 1;
            sa = (File) this.pl.ls.get(s1);
            mp3Player = new MP3Player(sa.toURI().toURL()); // Use the File's URL
            a = 1;
            jPlaylist.setSelectedIndex(s1);
        } catch (Exception e) {
            System.out.println("Problem playing music");
            System.out.println(e);
        }

        new Thread(() -> {
            try {
                mp3Player.play();
            } catch (Exception e) {
                System.out.println("Error playing music");
                System.out.println(e);
            }
        }).start();
    } else {
        mp3Player.stop();  // Stop the playback using jaco.mp3.player.MP3Player
        a = 0;
        next();
    }
}

      
     void previous() {
    if (a == 0) {
        try {
            int s1 = jPlaylist.getSelectedIndex() - 1;
            sa = (File) this.pl.ls.get(s1);
            mp3Player = new MP3Player(sa.toURI().toURL()); // Use the File's URL
            a = 1;
            jPlaylist.setSelectedIndex(s1);
        } catch (Exception e) {
            System.out.println("Problem playing music");
            System.out.println(e);
        }

        new Thread(() -> {
            try {
                mp3Player.play();
            } catch (Exception e) {
                System.out.println("Error playing music");
                System.out.println(e);
            }
        }).start();
    } else {
        if (mp3Player != null) {
            mp3Player.stop();  // Stop the playback using jaco.mp3.player.MP3Player
        }
        a = 0;
        previous();
    }
}

      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnadd = new javax.swing.JButton();
        btnremove = new javax.swing.JButton();
        btndown = new javax.swing.JButton();
        btnsave = new javax.swing.JButton();
        btnup = new javax.swing.JButton();
        btnopen = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPlaylist = new javax.swing.JList<>();
        btnprevius = new javax.swing.JButton();
        btnplay = new javax.swing.JButton();
        btnnext = new javax.swing.JButton();
        btnstop = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        imagendecancion = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnpause = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnadd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_add_126583.png"))); // NOI18N
        btnadd.setText("ADD");
        btnadd.setBorderPainted(false);
        btnadd.setContentAreaFilled(false);
        btnadd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddActionPerformed(evt);
            }
        });

        btnremove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_remove-rounded_383082.png"))); // NOI18N
        btnremove.setText("REMOVE");
        btnremove.setBorderPainted(false);
        btnremove.setContentAreaFilled(false);
        btnremove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnremoveActionPerformed(evt);
            }
        });

        btndown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_145_111066.png"))); // NOI18N
        btndown.setText("DOWN");
        btndown.setBorderPainted(false);
        btndown.setContentAreaFilled(false);
        btndown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndownActionPerformed(evt);
            }
        });

        btnsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_save_326688.png"))); // NOI18N
        btnsave.setText("SAVE");
        btnsave.setBorderPainted(false);
        btnsave.setContentAreaFilled(false);
        btnsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsaveActionPerformed(evt);
            }
        });

        btnup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_arrow-up-thick_216098.png"))); // NOI18N
        btnup.setText("UP");
        btnup.setBorderPainted(false);
        btnup.setContentAreaFilled(false);
        btnup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnupActionPerformed(evt);
            }
        });

        btnopen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_folder-open_1608888.png"))); // NOI18N
        btnopen.setText("OPEN");
        btnopen.setBorderPainted(false);
        btnopen.setContentAreaFilled(false);
        btnopen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnopenActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jPlaylist);

        btnprevius.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_skip-previous_326509.png"))); // NOI18N
        btnprevius.setBorderPainted(false);
        btnprevius.setContentAreaFilled(false);
        btnprevius.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpreviusActionPerformed(evt);
            }
        });

        btnplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_play-arrow_326577.png"))); // NOI18N
        btnplay.setBorderPainted(false);
        btnplay.setContentAreaFilled(false);
        btnplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnplayActionPerformed(evt);
            }
        });

        btnnext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_next_293690.png"))); // NOI18N
        btnnext.setBorderPainted(false);
        btnnext.setContentAreaFilled(false);
        btnnext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnextActionPerformed(evt);
            }
        });

        btnstop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_media-stop_216325.png"))); // NOI18N
        btnstop.setBorderPainted(false);
        btnstop.setContentAreaFilled(false);
        btnstop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnstopActionPerformed(evt);
            }
        });

        jSlider1.setValue(0);

        jLabel1.setText("00:00");

        imagendecancion.setText("jLabel2");

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("+");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("-");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnpause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/iconfinder_play-arrow_326577.png"))); // NOI18N
        btnpause.setBorderPainted(false);
        btnpause.setContentAreaFilled(false);
        btnpause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpauseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnadd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnremove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btndown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnopen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnsave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                .addComponent(imagendecancion, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton3)))
                        .addGap(60, 60, 60)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnprevius, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnplay, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnpause, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnnext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnstop, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(imagendecancion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnsave))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnadd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnremove))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnup)
                                    .addComponent(btnopen))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btndown)))))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnnext, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnstop)))
                    .addComponent(btnplay, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnprevius, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnpause, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnplayActionPerformed
       putar(); 
    }//GEN-LAST:event_btnplayActionPerformed

    private void btnaddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddActionPerformed
       add();
    }//GEN-LAST:event_btnaddActionPerformed

    private void btnremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnremoveActionPerformed
        remove();
    }//GEN-LAST:event_btnremoveActionPerformed

    private void btnpreviusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpreviusActionPerformed
        previous();
    }//GEN-LAST:event_btnpreviusActionPerformed

    private void btnnextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnextActionPerformed
        next();
    }//GEN-LAST:event_btnnextActionPerformed

    private void btnupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnupActionPerformed
        up();
    }//GEN-LAST:event_btnupActionPerformed

    private void btndownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndownActionPerformed
       down();
    }//GEN-LAST:event_btndownActionPerformed

    private void btnopenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnopenActionPerformed
        open();
    }//GEN-LAST:event_btnopenActionPerformed

    private void btnsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsaveActionPerformed
        save();
    }//GEN-LAST:event_btnsaveActionPerformed

    private void btnstopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnstopActionPerformed
        if (mp3Player != null) {
        mp3Player.stop(); // Close the MP3 player using jaco.mp3.player.MP3Player
        mp3Player = null; // Set the player to null after closing
        a = 0;
    }
        
    }//GEN-LAST:event_btnstopActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        pause();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        adjustVolumeUp();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        
        adjustVolumeDown();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnpauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpauseActionPerformed
        // TODO add your handling code here:
          pause();
    }//GEN-LAST:event_btnpauseActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PlayerMemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlayerMemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlayerMemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlayerMemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PlayerMemo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btndown;
    private javax.swing.JButton btnnext;
    private javax.swing.JButton btnopen;
    private javax.swing.JButton btnpause;
    private javax.swing.JButton btnplay;
    private javax.swing.JButton btnprevius;
    private javax.swing.JButton btnremove;
    private javax.swing.JButton btnsave;
    private javax.swing.JButton btnstop;
    private javax.swing.JButton btnup;
    private javax.swing.JLabel imagendecancion;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList<String> jPlaylist;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    // End of variables declaration//GEN-END:variables

    private DefaultListModel DefaultListModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
