package responsi;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InputCerita extends JFrame {
    JFrame window = new JFrame("Input Cerita");
    // JLabel
    JLabel lUser        = new JLabel("User");
    JLabel lJudul       = new JLabel("Judul");
    JLabel lJenis       = new JLabel("Jenis Cerita");
    JLabel lIsi         = new JLabel("Isi");
  
    // JTextFields
    JTextField fJudul   = new JTextField();
    JTextField fIsi  = new JTextField();
 
    // JComboBox
    JComboBox<String> cUser     = new JComboBox<>();
    JComboBox<String> cJenis    = new JComboBox<>();
    DataCerita jenis;
    List<DataCerita>jeniss=new ArrayList<>();
    //Button
    JButton bLanjut     = new JButton("Lanjut");
    JButton bKembali    = new JButton("Kembali");

    Koneksi koneksi     = new Koneksi();
    ResultSet resultSet;
    Statement statement;
    
    Boolean input = false;

    static String judulCerita, idCerita, isiCerita;

    public InputCerita() {
        if (UserSession.getId_user() == null) {
            JOptionPane.showMessageDialog(null, "Silahkan login terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            window.setVisible(false);
            new Login();
        } else{
            getAllUser();
            initComponents();
            loadJenisCerita();
            initListeners();
        }
    }

    private void initComponents(){
        window.getContentPane().setBackground(new Color(28, 27, 27));
        if(UserSession.getId_user().equals("1")) {
            window.add(lUser);
                lUser.setBounds(40,20,150,20);
                lUser.setForeground(new Color(255, 255, 255));
            window.add(cUser);
                cUser.setBounds(150,20,170,25);

            lJudul.setBounds(40, 63, 150, 20);
            fJudul.setBounds(150, 60, 170, 25);
            cJenis.setBounds(150, 100, 170, 25);
            lJenis.setBounds(40, 103, 150, 20);
            lIsi.setBounds(40, 143, 150, 20);
            fIsi.setBounds(150, 140, 170, 70);
            bLanjut.setBounds(150,265,170,25);
            bKembali.setBounds(40,265,90,25);
            window.setSize(375, 360);

        }else{
            lJudul.setBounds(40, 23, 150, 20);
            fJudul.setBounds(150, 20, 170, 25);
            lJenis.setBounds(40, 63, 150, 20);
            cJenis.setBounds(150, 60, 170, 25);
            lIsi.setBounds(40, 103, 150, 20);
            fIsi.setBounds(150, 100, 170, 70);
            bLanjut.setBounds(150,220,170,25);
            bKembali.setBounds(40,220,90,25);
            window.setSize(375, 320);
        }
        window.add(lJudul);
            lJudul.setForeground(new Color(255, 255, 255));
            window.add(fJudul);

        window.add(lJenis);
            lJenis.setForeground(new Color(255, 255, 255));
            window.add(cJenis);

        window.add(lIsi);
            lIsi.setForeground(new Color(255, 255, 255));
            window.add(fIsi);

        window.add(bLanjut);
        window.add(bKembali);

        window.setLayout(null);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
    }

    private List<DataCerita> getAllJenis(){
        try {
            statement = koneksi.getConnection().createStatement();
            String sql = "SELECT * FROM cerita";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                jenis = new DataCerita();
                jenis.setIdCerita(resultSet.getString("id_cerita"));
                jenis.setJenisCerita(resultSet.getString("jenis_cerita"));
                jeniss.add(jenis);
            }
        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
        return jeniss;
    }

    private void loadJenisCerita(){
        cJenis.removeAllItems();
        List<DataCerita> jeniss = getAllJenis();
        for(DataCerita data:jeniss){
            cJenis.addItem(data.getJenisCerita().toString());
        }
    }

    public void initListeners(){
        cJenis.addActionListener(e -> {
                idCerita = jeniss.get(cJenis.getSelectedIndex()).getIdCerita();
                judulCerita = fJudul.getText();
                isiCerita = fIsi.getText();
 
        });

        bLanjut.addActionListener(e -> {
            insertCerita();
            if (input = true) {
                window.setVisible(false);
                JOptionPane.showMessageDialog(null, "Berhasil Menginput!");
                window.setVisible(false);
                new MenuUtama();
            } else {
                JOptionPane.showMessageDialog(null, "gagal Menginput!");
            }
        });

        bKembali.addActionListener(e -> {
            window.setVisible(false);
            new MenuUtama();
        });
    }

    private void insertCerita(){
        DataCerita jenis = new DataCerita();
        String kode;
        try{
                statement = koneksi.getConnection().createStatement();
                String sqlMax = "SELECT max(id_berkas) as max_kode FROM berkas";
                resultSet = statement.executeQuery(sqlMax);
                if (resultSet.next()) {
                    String kode_berkas = resultSet.getString("max_kode");
                    if(kode_berkas == null ){
                        kode = "BRKS-001";
                    }else {
                        String kode_berkas_bersih = kode_berkas.substring(5, 8);
                        int no_urut = Integer.parseInt(kode_berkas_bersih);
                        no_urut += 1;

                        String brks = "BRKS-";
                        kode = brks + String.format("%03d", no_urut);
                    }
                    if(UserSession.getId_user().equals("1")){
                        String sql = "SELECT id_user,username FROM user WHERE username='"+cUser.getSelectedItem()+"'";
                        resultSet  = statement.executeQuery(sql);
                        resultSet.next();
                        String idUser = resultSet.getString("id_user");
                        statement.executeUpdate("INSERT INTO berkas VALUES('" + kode + "','" + idCerita + "','" + idUser + "','" + judulCerita + "','" + isiCerita +"')");
                        input = true;
                    }else {
                        statement.executeUpdate("INSERT INTO berkas VALUES('" + kode + "','" + UserSession.getId_user() + "','" + idCerita + "','" + judulCerita + "','" + isiCerita + "' )");
                        input = true;
                    }
                }

            resultSet.close();

        }catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(rootPane, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(rootPane, "Driver tidak ditemukan !!");
        }catch (NumberFormatException e){
            System.err.println("error"+e);
        }
    }

    private void getAllUser(){
        try {
            statement = koneksi.getConnection().createStatement();
            String sql = "SELECT * FROM user";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                cUser.addItem(resultSet.getString("username"));
            }
        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
    }

}
