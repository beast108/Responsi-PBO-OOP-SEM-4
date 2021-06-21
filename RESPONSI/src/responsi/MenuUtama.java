package responsi;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class MenuUtama extends JFrame {
    JFrame window 		= new JFrame("Menu Utama");
    JLabel halo			= new JLabel();
    JButton bICerita 	= new JButton("Input Cerita");
    JButton bDCerita 	= new JButton("Daftar Cerita");
    JButton bKelolaUser = new JButton("Kelola User");
    JButton bLogout 	= new JButton("Log Out");

    NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
    public MenuUtama(){
        if(UserSession.getId_user() == null){
            JOptionPane.showMessageDialog(null, "Silahkan login terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            window.setVisible(false);
            new Login();
        }else {
            initComponents();
            initListeners();
        }
    }

    private void initComponents(){
        window.getContentPane().setBackground(new Color(28, 27, 27));
        window.add(halo);
        halo.setText("Halo, "+UserSession.getNama());
        halo.setFont(new Font("Arial", Font.BOLD,20));
        halo.setForeground(new Color(255,255,255));
        halo.setBounds(150,30,400,30);

        window.add(bICerita);
            bICerita.setForeground(new Color(255, 255, 255));
            bICerita.setBackground(new Color(82, 77, 64));
        window.add(bDCerita);
            bDCerita.setForeground(new Color(255,255,255));
            bDCerita.setBackground(new Color(82, 77, 64));
        window.add(bLogout);
            bLogout.setForeground(new Color(255, 255, 255));
            bLogout.setBounds(250, 160, 220, 30);

        if(UserSession.getRole() == 1) {
            window.add(bKelolaUser);
            bKelolaUser.setBounds(10, 160, 220, 30);
            bKelolaUser.setForeground(new Color(255, 255, 255));
            bKelolaUser.setBackground(new Color(82, 77, 64));

            bICerita.setBounds(10, 120, 220, 30);
            bDCerita.setBounds(250, 120, 220, 30);
            bLogout.setBackground(new Color(102, 55, 51));
        }else{
            bLogout.setBackground(new Color(102, 55, 51));
            bICerita.setBounds(10, 120, 220, 30);
            bDCerita.setBounds(250, 120, 220, 30);

        }
        window.setLayout(null);
        window.setSize(500, 370);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }

    private void initListeners(){
        bICerita.addActionListener(e -> {
            window.setVisible(false);
            new InputCerita();
        });

        bDCerita.addActionListener(e -> {
            window.setVisible(false);
                new DaftarCerita();
        });

        if (UserSession.getRole() == 1) {
            bKelolaUser.addActionListener(e -> {
                window.setVisible(false);
                new KelolaUser();
            });
        }

        bLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Berhasil keluar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            UserSession.setId_user(null);
            window.setVisible(false);
            new Login();
        });
    }
}
