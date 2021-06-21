package responsi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends JFrame{
    ResultSet resultSet;
    Statement statement;
    String id,nama;
    int role;

    JFrame window 		= new JFrame("Login");
    JLabel lUsername	= new JLabel("Username");
    JLabel lSandi		= new JLabel("Sandi");

    JTextField fUsername	= new JTextField();
    JPasswordField fSandi	= new JPasswordField();

    JButton bMasuk = new JButton("Masuk");
    JButton bDaftar = new JButton("Daftar");
    JCheckBox cLihat= new JCheckBox("Lihat");

    public Login(){
        if(UserSession.getId_user() != null){
            window.setVisible(false);
            new MenuUtama();
        }else {
            initComponents();
            initListeners();
        }
    }

    private void initComponents(){
        window.getContentPane().setBackground(new Color(28, 27, 27));

        window.add(lUsername);
        lUsername.setBounds(50, 15, 90, 60);
        lUsername.setForeground(new Color(255, 255, 255));
        window.add(fUsername);
        fUsername.setBounds(150, 30,270,30);

        window.add(lSandi);
        lSandi.setBounds(50, 65, 50, 60);
        lSandi.setForeground(new Color(255, 255, 255));
        window.add(fSandi);
        fSandi.setBounds(150, 80, 200, 30);

        window.add(cLihat);
        cLihat.setBounds(362,80,70,30);
        cLihat.setBackground(new Color(28, 27, 27));
        cLihat.setForeground(new Color(255,255,255));

        window.add(bMasuk);
        bMasuk.setBounds(150, 130, 165, 30);
        bMasuk.setForeground(new Color(255,255,255));
        bMasuk.setBackground(new Color(58, 133, 86));

        window.add(bDaftar);
        bDaftar.setBounds(330, 130, 90,30);
        bDaftar.setForeground(new Color(255,255,255));
        bDaftar.setBackground(new Color(58, 119, 133));

        window.setLayout(null);
        window.setSize(480, 250);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }

    private void initListeners(){
        bMasuk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Koneksi koneksi = new Koneksi();
                String username = fUsername.getText();
                char[] getpass = fSandi.getPassword();
                String sandi = String.valueOf(getpass);
                if (username.isEmpty() || sandi.isEmpty()) {
                    if (username.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Silahkan Masukkan Username anda!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Silahkan Masukkan Sandi anda!");
                        fUsername.setText(username);
                    }
                } else {
                    try {
                        String MD5 = DataUser.getMd5(sandi);
                        statement = koneksi.getConnection().createStatement();
                        String sql = "SELECT * FROM user WHERE username='" + username + "' AND sandi='" + MD5 + "'";
                        resultSet = statement.executeQuery(sql);

                        if (resultSet.next()) {
                            id = resultSet.getString("id_user");
                            UserSession.setId_user(id);
                            nama = resultSet.getString("nama");
                            UserSession.setNama(nama);
                            role = resultSet.getInt("role_id");
                            UserSession.setRole(role);

                            window.setVisible(false);
                            new MenuUtama();
                            statement.close();

                        } else {
                            JOptionPane.showMessageDialog(null, "Username atau sandi salah!");
                            fUsername.setText("");
                            fSandi.setText("");
                        }
                    } catch (SQLException sqlError) {
                        JOptionPane.showMessageDialog(rootPane, "Data Gagal Ditampilkan" + sqlError);
                    } catch (ClassNotFoundException classError) {
                        JOptionPane.showMessageDialog(rootPane, "Driver tidak ditemukan !!");
                    }

                }
            }
        });
        bDaftar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.setVisible(false);
                new Daftar();
            }
        });
        cLihat.addActionListener(ae -> {
            JCheckBox c = (JCheckBox) ae.getSource();
            fSandi.setEchoChar(c.isSelected() ? '\u0000' : (Character) UIManager.get("PasswordField.echoChar"));
            if (c.isSelected()) {
                cLihat.setText("Tutup");
            } else {
                cLihat.setText("Lihat");
            }
        });
    }
}

