package responsi;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class KelolaUser {
    private static final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp     = new Timestamp(System.currentTimeMillis());
    JFrame window 		    = new JFrame("Kelola User");
    Koneksi koneksi         = new Koneksi();

    // Component Table
    String[][] datas        = new String[500][8];
    String[] kolom          = {"ID","Nama User", "Username","No Hp","Sandi", "Role","Dibuat","Diubah"};
    JTable tTable;
    JScrollPane pane;
    TableModel model;
    TableRowSorter sorter;

    // Tabel

//    JTable tTable           = new JTable(datas, kolom);
//    JScrollPane pane  = new JScrollPane(tTable);
    // Label
    JLabel lNama            = new JLabel("Nama");
    JLabel lUsername        = new JLabel("Username");
    JLabel lInfo            = new JLabel("Abaikan Jika tidak ingin merubah Sandi");
    JLabel lSandi           = new JLabel("Sandi");
    JLabel lKSandi          = new JLabel("Konfirmasi Sandi");
    JLabel lRole            = new JLabel("Role");
    JLabel lCari            = new JLabel("Cari");
    JLabel lNomorHp         = new JLabel("Nomor Hp");
    JCheckBox cLihat        = new JCheckBox("Lihat");
    JCheckBox cLihat2       = new JCheckBox("Lihat");

    // Text Field
    JTextField fId          = new JTextField();
    JTextField fNama        = new JTextField();
    JTextField fUsername    = new JTextField();
    JTextField fCari        = new JTextField();
    JTextField fNomorHp     = new JTextField();
    JPasswordField fSandi   = new JPasswordField();
    JPasswordField fKSandi  = new JPasswordField();
    JComboBox<String> cRole = new JComboBox<>();
    // Button
    JButton bTambah     = new JButton("Tambah");
    JButton bUpdate 	= new JButton("Ubah");
    JButton bHapus   	= new JButton("Hapus");
    JButton bKembali 	= new JButton("Kembali");

    DataUser role;
    List<DataUser> roles=new ArrayList<>();
    ResultSet resultSet;
    Statement statement;


    public KelolaUser(){
        if(UserSession.getId_user() == null){
            JOptionPane.showMessageDialog(null, "Silahkan login terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            window.setVisible(false);
            new Login();
        }else if(UserSession.getRole() != 1){
            JOptionPane.showMessageDialog(null, "Akses tidak diberikan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            window.setVisible(false);
            new Login();
        }else {
            loadData();
            initComponents();
            initListeners();
        }
    }

    private void initComponents(){
        window.getContentPane().setBackground(new Color(28, 27, 27));

        // Set Table Component
        model = new DefaultTableModel(datas,kolom);
        sorter= new TableRowSorter<>(model);
        tTable= new JTable(model);
        tTable.setRowSorter(sorter);
        pane = new JScrollPane(tTable);

        window.add(lNama);
        lNama.setBounds(680, 20, 150, 20);
        lNama.setForeground(new Color(255, 255, 255));
            window.add(fNama);
            fNama.setBounds(810, 20, 170, 25);

        window.add(lUsername);
        lUsername.setBounds(680, 63, 150, 20);
        lUsername.setForeground(new Color(255, 255, 255));
            window.add(fUsername);
            fUsername.setBounds(810, 60, 170, 25);

        window.add(lCari);
        lCari.setBounds(20,23,150,20);
        lCari.setForeground(new Color(255, 255, 255));
            window.add(fCari);
            fCari.setBounds(75,20,570,25);

        window.add(lNomorHp);
        lNomorHp.setBounds(680,103,150,20);
        lNomorHp.setForeground(new Color(255, 255, 255));
            window.add(fNomorHp);
            fNomorHp.setBounds(810,100,170,25);

        window.add(lRole);
        lRole.setBounds(680, 143, 150, 20);
        lRole.setForeground(new Color(255, 255, 255));
            window.add(cRole);
            cRole.setBounds(810, 140, 170, 25);

        window.add(lInfo);
        lInfo.setBounds(680,180,1000,20);
        lInfo.setForeground(new Color(255,255,255));
        lInfo.setFont(new Font("Arial",Font.ITALIC,12));
        window.add(lSandi);
            lSandi.setBounds(680, 213, 150, 20);
            lSandi.setForeground(new Color(255, 255, 255));
            window.add(fSandi);
                fSandi.setBounds(810, 210, 170, 25);
                window.add(cLihat);
                    cLihat.setBounds(985,210,100,25);
                    cLihat.setBackground(new Color(28, 27, 27));
                    cLihat.setForeground(new Color(255,255,255));
        window.add(lKSandi);
        lKSandi.setBounds(680,253,150,20);
        lKSandi.setForeground(new Color(255, 255, 255));
            window.add(fKSandi);
                fKSandi.setBounds(810,250,170,25);
                window.add(cLihat2);
                    cLihat2.setBounds(985,250,100,25);
                    cLihat2.setBackground(new Color(28, 27, 27));
                    cLihat2.setForeground(new Color(255,255,255));



        window.add(bTambah);
        bTambah.setBounds(20, 240, 140, 30);
        bTambah.setForeground(new Color(255, 255, 255));
        bTambah.setBackground(new Color(58, 133, 86));

        window.add(bUpdate);
        bUpdate.setBounds(185, 240, 140, 30);
        bUpdate.setForeground(new Color(255,255,255));
        bUpdate.setBackground(new Color(168, 168, 50));

        window.add(bHapus);
        bHapus.setBounds(345, 240, 140, 30);
        bHapus.setForeground(new Color(255,255,255));
        bHapus.setBackground(new Color(102, 55, 51));

        window.add(bKembali);
        bKembali.setBounds(510, 240, 140, 30);
        bKembali.setForeground(new Color(255,255,255));
        bKembali.setBackground(new Color(82, 77, 64));

        tTable.setBackground(new Color(247, 252, 255));
        TableColumnModel columnModel = tTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(2);
        columnModel.getColumn(1).setPreferredWidth(60);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(30);
        columnModel.getColumn(4).setPreferredWidth(70);
        columnModel.getColumn(5).setPreferredWidth(70);
        columnModel.getColumn(6).setPreferredWidth(70);
        window.add(pane);
        pane.setBounds(20, 70, 630, 140);
        pane.setBackground(new Color(247, 252, 255));

        window.setLayout(null);
        window.setSize(1060, 340);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }

    private void initListeners(){
        tTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int baris = tTable.rowAtPoint(e.getPoint());
                    String id = tTable.getValueAt(baris, 0).toString();
                    fId.setText(id);
                    String nama = tTable.getValueAt(baris, 1).toString();
                    fNama.setText(nama);
                    String username = tTable.getValueAt(baris, 2).toString();
                    fUsername.setText(username);
                    String nomorHp = tTable.getValueAt(baris, 3).toString();
                    fNomorHp.setText(nomorHp);
                    String role = tTable.getValueAt(baris, 5).toString();
                    cRole.setSelectedItem(role);

                } catch (Exception ea) {
                    JOptionPane.showMessageDialog(null, "Mohon Maaf Data " + ea.getMessage());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        bTambah.addActionListener(e -> {
            try {
                if(fNama.getText()==null){
                    JOptionPane.showMessageDialog(null, "Nama Harus Diisi!", "Informasi", JOptionPane.WARNING_MESSAGE);
                }else if(fUsername.getText()==null){
                    JOptionPane.showMessageDialog(null, "Username Harus Diisi!", "Informasi", JOptionPane.WARNING_MESSAGE);
                }else if(fNomorHp.getText()==null){
                    JOptionPane.showMessageDialog(null, "Nomor Hp Harus Diisi!", "Informasi", JOptionPane.WARNING_MESSAGE);
                }else if(fSandi.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Sandi Harus Diisi!", "Informasi", JOptionPane.WARNING_MESSAGE);
                }else if(fKSandi.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Konfirmasi Sandi Harus Diisi!", "Informasi", JOptionPane.WARNING_MESSAGE);
                }else if(!fSandi.getText().equals(fKSandi.getText())){
                    JOptionPane.showMessageDialog(null, "Kombinasi Sandi tidak sama!", "Informasi", JOptionPane.WARNING_MESSAGE);
                }else {
                    String MD5 = DataUser.getMd5(fSandi.getText());
                    statement = koneksi.getConnection().createStatement();
                    String sql = "INSERT INTO user VALUES(default,'" + fNama.getText() + "','" + fUsername.getText() + "','" + fNomorHp.getText() + "','" + MD5 + "','" + roles.get(cRole.getSelectedIndex()).getIdRole() + "','" + time.format(timestamp) + "','" + time.format(timestamp) + "' )";
                    int disimpan = statement.executeUpdate(sql);
                    if (disimpan == 1) {
                        JOptionPane.showMessageDialog(null, "Selamat anda berhasil mendaftar!", "Informasi", JOptionPane.WARNING_MESSAGE);
                        statement.close();
                        window.setVisible(false);
                        new KelolaUser();
                    }
                }
            } catch (SQLException sqlError) {
                JOptionPane.showMessageDialog(null, "Gagal mendaftar! error : " + sqlError);
            } catch (ClassNotFoundException classError) {
                JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
            }
        });

        bUpdate.addActionListener(e -> {
            try {
                statement = koneksi.getConnection().createStatement();
                if (fSandi.getText().isEmpty() && fKSandi.getText().isEmpty()) {
                    String sql = "UPDATE user set nama='" + fNama.getText() + "',username='" + fUsername.getText() + "',role_id='" + roles.get(cRole.getSelectedIndex()).getIdRole() + "',diubah='" + time.format(timestamp) + "' WHERE id_user='" + fId.getText() + "'";
                    int disimpan = statement.executeUpdate(sql);
                    if (disimpan == 1) {
                        JOptionPane.showMessageDialog(null, "Berhasil Diubah!", "Informasi", JOptionPane.WARNING_MESSAGE);
                        statement.close();
                        window.setVisible(false);
                        new KelolaUser();
                    }
                } else {
                    if (!fSandi.getText().equals(fKSandi.getText())) {
                        JOptionPane.showMessageDialog(null, "Kombinasi sandi tidak sama!", "Informasi", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String MD5 = DataUser.getMd5(fSandi.getText());
                        String sql = "UPDATE user set nama='" + fNama.getText() + "',email='" + fUsername.getText() + "',sandi='" + MD5 + "',role_id='" + roles.get(cRole.getSelectedIndex()).getIdRole() + "',diubah='" + time.format(timestamp) + "' WHERE id_user='" + fId.getText() + "'";
                        int disimpan = statement.executeUpdate(sql);
                        if (disimpan == 1) {
                            JOptionPane.showMessageDialog(null, "Berhasil Diubah!", "Informasi", JOptionPane.WARNING_MESSAGE);
                            statement.close();
                            window.setVisible(false);
                            new KelolaUser();
                        }
                    }
                }
            } catch (SQLException sqlError) {
                JOptionPane.showMessageDialog(null, "Gagal Mengubah! error : " + sqlError);
            } catch (ClassNotFoundException classError) {
                JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
            }
        });

        bHapus.addActionListener(ae -> {
            try {
                statement = koneksi.getConnection().createStatement();
                String sql = "DELETE FROM user WHERE id_user='" + fId.getText() + "'";
                statement.execute(sql);
                JOptionPane.showMessageDialog(null, "Berhasil Hapus Data!", "Informasi", JOptionPane.WARNING_MESSAGE);
                statement.close();
                window.setVisible(false);
                new KelolaUser();
            } catch (HeadlessException | SQLException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        });

        bKembali.addActionListener(e -> {
            window.setVisible(false);
            new MenuUtama();
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

        // Search Data with DocumentListener
        fCari.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search(fCari.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                search(fCari.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                search(fCari.getText());
            }
            public void search(String str) { if (str.length() == 0) { sorter.setRowFilter(null); } else { sorter.setRowFilter(RowFilter.regexFilter(str)); } }
        });
    }

    private void loadData(){
        cRole.removeAllItems();
        List<DataUser> roles = getAllRole();
        for (DataUser user : roles) {
            cRole.addItem(user.getNamaRole().toString());
        }

        try{
            statement = koneksi.getConnection().createStatement();
            String sql = "SELECT * FROM user a INNER JOIN role b ON a.role_id=b.id_role";
            resultSet = statement.executeQuery(sql);

            int row = 0;
            while (resultSet.next()){
                datas[row][0] = resultSet.getString("id_user");
                datas[row][1] = resultSet.getString("nama");
                datas[row][2] = resultSet.getString("username");
                datas[row][3] = resultSet.getString("no_ponsel");
                datas[row][4] = resultSet.getString("sandi");
                datas[row][5] = resultSet.getString("nama_role");
                datas[row][6] = resultSet.getString("dibuat");
                datas[row][7] = resultSet.getString("diubah");
                row++;
            }
            statement.close();

        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
    }

    private List<DataUser> getAllRole() {
        try {
            statement = koneksi.getConnection().createStatement();
            String sql = "SELECT * FROM role";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                role = new DataUser();
                role.setIdRole(resultSet.getInt("id_role"));
                role.setNamaRole(resultSet.getString("nama_role"));
                roles.add(role);
            }
        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
        return roles;
    }


}
