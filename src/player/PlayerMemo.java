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
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.swing.Timer;
import jaco.mp3.player.MP3Player;
import javax.swing.ImageIcon;
import javax.sound.sampled.*;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;


/**
 *
 * @author GPS
 */
public class PlayerMemo extends javax.swing.JFrame {
    private FileSystemView fileSystemView;
    Playlist pl=new Playlist();
    ArrayList updateList= new ArrayList();
    private MP3Player mp3Player;
    private long startTime;
    File simpan;
    Timer timer;
    
    

    /**
     * Creates new form PlayerMemo
     */
    public PlayerMemo() {
        
        initComponents();
         mp3Player = new MP3Player();
        this.setLocationRelativeTo(null);
        timer = new Timer(1000, e -> updateSlider()); // Actualiza cada segundo
        timer.setInitialDelay(0);
        TimerSlider.setEnabled(false);
        
        VolumeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                VolumeSliderChanged(evt);
            }
        });
       
         TimerSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateSlider();
            }
        });
        checkSongTerminada();
        
        
    }
    
    private void VolumeSliderChanged(javax.swing.event.ChangeEvent evt) {
        float newVolume = VolumeSlider.getValue() / 100.0f;
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
    
  

    private void updateSlider() {
        if (mp3Player != null) {
            if (mp3Player != null) {
            long currentTime = System.currentTimeMillis();
            long elapsedTimeInMillis = currentTime - startTime;
            int elapsedTimeInSeconds = (int) (elapsedTimeInMillis / 1000);
            int minutes = elapsedTimeInSeconds / 60;
            int seconds = elapsedTimeInSeconds % 60;
            String elapsedTimeString = String.format("%02d:%02d", minutes, seconds);
            minutos.setText(elapsedTimeString);
            TimerSlider.setValue(elapsedTimeInSeconds);
        }
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
                


                new Thread(() -> {
                    try {
                        mp3Player.play();
                        startTime = System.currentTimeMillis();
                        timer.start();
                         a = 1;
                         
                        while (mp3Player != null && !mp3Player.isStopped()) {
                        Thread.sleep(100);
                        }
                        timer.stop();
                        minutos.setText("00:00");
                        TimerSlider.setValue(0);
                        TimerSlider.setEnabled(false);
                        a = 0;

                         
                         
                    } catch (Exception e) {
                        System.out.println("Error playing music");
                        JOptionPane.showMessageDialog(null, "select a song from file", null, JOptionPane.ERROR_MESSAGE);

                    }
                }).start();
                


            } catch (Exception e) {
                System.out.println("Problem playing music");              
                JOptionPane.showMessageDialog(null, "select a song from playlist", null, JOptionPane.INFORMATION_MESSAGE);


            }

        } else {


            if (mp3Player.isPaused()) {
                System.out.println("play");
                mp3Player.play();
                timer.start();          
            } else {
                System.out.println("pausada");
                mp3Player.pause();          
                timer.stop();
            }
        }
    }
    
     private void checkSongTerminada() {
        if (mp3Player != null && mp3Player.isStopped()) {
            timer.stop();
            minutos.setText("00:00");
            TimerSlider.setValue(0);
            TimerSlider.setEnabled(false); 
            a = 0;
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
        mp3Player.stop();  
        a = 0;
        next();
    }
}

      
     void previous() {
    if (a == 0) {
        try {
            int s1 = jPlaylist.getSelectedIndex() - 1;
            sa = (File) this.pl.ls.get(s1);
            mp3Player = new MP3Player(sa.toURI().toURL()); 
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
            mp3Player.stop();  
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
        TimerSlider = new javax.swing.JSlider();
        minutos = new javax.swing.JLabel();
        VolumeSlider = new javax.swing.JSlider();
        imagendecancion = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

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

        TimerSlider.setMaximum(250);
        TimerSlider.setValue(0);

        minutos.setText("00:00");

        VolumeSlider.setMaximum(150);

        jButton2.setText("+");
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("-");
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnremove)
                                .addGap(18, 18, 18)
                                .addComponent(btndown)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnadd)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnup, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnopen, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnsave, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(imagendecancion, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TimerSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(VolumeSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(minutos, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(24, 24, 24))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(btnprevius, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnplay, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnnext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnstop, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(8, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnup)
                            .addComponent(btnopen, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnadd))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btndown)
                                .addComponent(btnremove))
                            .addComponent(btnsave)))
                    .addComponent(imagendecancion, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TimerSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(minutos))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton3)
                            .addComponent(VolumeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnplay, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                    .addComponent(btnprevius, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnnext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnstop)))
                .addContainerGap(29, Short.MAX_VALUE))
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        System.out.println("iba una funcion pero no la Use :no funciono");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        System.out.println("iba una funcion pero no la Use :no funciono");

    }//GEN-LAST:event_jButton3ActionPerformed

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
    private javax.swing.JSlider TimerSlider;
    private javax.swing.JSlider VolumeSlider;
    private javax.swing.JButton btnadd;
    private javax.swing.JButton btndown;
    private javax.swing.JButton btnnext;
    private javax.swing.JButton btnopen;
    private javax.swing.JButton btnplay;
    private javax.swing.JButton btnprevius;
    private javax.swing.JButton btnremove;
    private javax.swing.JButton btnsave;
    private javax.swing.JButton btnstop;
    private javax.swing.JButton btnup;
    private javax.swing.JLabel imagendecancion;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JList<String> jPlaylist;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel minutos;
    // End of variables declaration//GEN-END:variables

    private DefaultListModel DefaultListModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
