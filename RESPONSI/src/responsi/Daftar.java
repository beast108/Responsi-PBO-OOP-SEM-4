package responsi;


import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Daftar extends JFrame{
    Statement statement;
    private static final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    // Label
    JFrame window 	= new JFrame("Daftar Member");
    JLabel lNama	= new JLabel("Nama");
    JLabel lUsername= new JLabel("Username");
    JLabel lNomorHp = new JLabel("Nomor Ponsel");
    JLabel lSandi	= new JLabel("Sandi");
    JLabel lKSandi	= new JLabel("Konfirmasi Sandi");
    // Text Field
    JTextField fNama  		= new JTextField();
    JTextField fUsername	= new JTextField();
    JTextField fNomorHp     = new JTextField(12);
    JPasswordField fSandi	= new JPasswordField();
    JPasswordField fKSandi 	= new JPasswordField();
    // Button
    JButton bDaftar 	= new JButton("Daftar");
    JButton bKembali 	= new JButton("Kembali");
    JCheckBox cLihat	= new JCheckBox("Lihat");
    JCheckBox cLihat2	= new JCheckBox("Lihat");

    // Constructor
    public Daftar(){
        if(UserSession.getId_user() != null){
            window.setVisible(true);
            new MenuUtama();
        }else {
            initComponents();
            initListeners();
        }
    }

    private void initComponents(){
        window.getContentPane().setBackground(new Color(28, 27, 27));

        window.add(lNama);
        lNama.setBounds(50, 15, 50, 60);
        lNama.setForeground(new Color(255, 255, 255));
        window.add(fNama);
        fNama.setBounds(190, 30,270,30);

        window.add(lUsername);
        lUsername.setBounds(50, 65, 140, 60);
        lUsername.setForeground(new Color(255, 255, 255));
        window.add(fUsername);
        fUsername.setBounds(190, 80, 270, 30);

        window.add(lNomorHp);
        lNomorHp.setBounds(50, 115, 140, 60);
        lNomorHp.setForeground(new Color(255, 255, 255));
        window.add(fNomorHp);
        fNomorHp.setBounds(190,130,270,30);

        window.add(lSandi);
        lSandi.setBounds(50, 165, 50, 60);
        lSandi.setForeground(new Color(255, 255, 255));
        window.add(fSandi);
        fSandi.setBounds(190,180,200,30);

        window.add(cLihat);
        cLihat.setBounds(400,180,70,30);
        cLihat.setBackground(new Color(28, 27, 27));
        cLihat.setForeground(new Color(255,255,255));

        window.add(lKSandi);
        lKSandi.setBounds(50, 215, 140, 60);
        lKSandi.setForeground(new Color(255, 255, 255));
        window.add(fKSandi);
        fKSandi.setBounds(190,230,200,30);

        window.add(cLihat2);
        cLihat2.setBounds(400,230,70,30);
        cLihat2.setBackground(new Color(28, 27, 27));
        cLihat2.setForeground(new Color(255,255,255));

        window.add(bDaftar);
        bDaftar.setBounds(190, 280, 165, 30);
        bDaftar.setForeground(new Color(255,255,255));
        bDaftar.setBackground(new Color(58, 133, 86));

        window.add(bKembali);
        bKembali.setBounds(370, 280, 90,30);
        bKembali.setForeground(new Color(255,255,255));
        bKembali.setBackground(new Color(58, 119, 133));

        window.setLayout(null);
        window.setSize(520, 390);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }

    // Event Handling menggunakan Action Listener
    private void initListeners(){
        bDaftar.addActionListener(e -> {
            Koneksi koneksi = new Koneksi();
            String nama 	= fNama.getText();
            String username = fUsername.getText();
            String noPonsel = fNomorHp.getText();
            char[] getSandi = fSandi.getPassword();
            char[] getKsandi= fKSandi.getPassword();
            String sandi 	= String.valueOf(getSandi);
            String sandiK 	= String.valueOf(getKsandi);

            if(nama.isEmpty() || username.isEmpty() || sandi.isEmpty() || sandiK.isEmpty()){
                if(nama.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Nama Tidak boleh kosong!","Peringatan",JOptionPane.WARNING_MESSAGE);
                }else if(username.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Username Tidak boleh kosong!","Peringatan",JOptionPane.WARNING_MESSAGE);
                } else if(noPonsel.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nomor Ponsel boleh kosong!","Peringatan",JOptionPane.WARNING_MESSAGE);
                }else if(sandi.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Sandi boleh kosong!","Peringatan",JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Konfirmasi Sandi Tidak boleh kosong!","Peringatan",JOptionPane.WARNING_MESSAGE);
                }
            }else if(!sandi.equals(sandiK)){
                JOptionPane.showMessageDialog(null, "Sandi tidak sama!","Peringatan",JOptionPane.WARNING_MESSAGE);
                fSandi.setText("");
                fKSandi.setText("");
            } else {
                try {
                    String MD5 = DataUser.getMd5(sandi);
                    statement = koneksi.getConnection().createStatement();
                    String q = "INSERT INTO user VALUES(default,'" + nama + "','" + username + "','" + noPonsel +"','" + MD5 + "','" + 2 + "','" + time.format(timestamp) + "','" + time.format(timestamp) + "' )";
                    int disimpan = statement.executeUpdate(q);
                    if (disimpan == 1) { JOptionPane.showMessageDialog(null, "Selamat anda berhasil daftar!", "Informasi", JOptionPane.WARNING_MESSAGE); statement.close(); window.setVisible(false); new Login(); }
                } catch (SQLException SqlError) { JOptionPane.showMessageDialog(null, "Gagal mendaftar! error : " + SqlError); } catch (ClassNotFoundException classError) { JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!"); }
            }
        });

        bKembali.addActionListener(e -> {
            window.setVisible(false);
            new Login();
        });
        cLihat.addActionListener (e -> {
            JCheckBox c = (JCheckBox) e.getSource();
            fSandi.setEchoChar(c.isSelected() ? '\u0000' : (Character) UIManager.get("PasswordField.echoChar"));
            if(c.isSelected()) {
                cLihat.setText("Tutup");
            }else{
                cLihat.setText("Lihat");
            }
        });
        cLihat2.addActionListener (e -> {
            JCheckBox c = (JCheckBox) e.getSource();
            fKSandi.setEchoChar(c.isSelected() ? '\u0000' : (Character) UIManager.get("PasswordField.echoChar"));
            if(c.isSelected()) {
                cLihat2.setText("Tutup");
            }else{
                cLihat2.setText("Lihat");
            }
        });
    }
}
