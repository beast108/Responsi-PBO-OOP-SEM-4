package responsi;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DaftarCerita extends JFrame{
    JFrame window 	= new JFrame();
    // Component to connect and access Database
    Koneksi koneksi = new Koneksi();
    ResultSet resultSet;
    Statement statement;

    // Set Component Table datas is data and kolom is Coloumn
    String[][] datas = new String[500][7];
    String[] kolom = new String[]{"User", "Kode", "Jenis Cerita", "Judul Cerita", "Isi Cerita"};

    // Component Table
    JTable tTable;
    JScrollPane pane;
    TableModel model;
    TableRowSorter sorter;

    // Label
    JLabel lId         = new JLabel("Kode");
    JLabel lJenis       = new JLabel("Jenis");
    JLabel lJudul       = new JLabel("Judul");
    JLabel lIsi      = new JLabel("Isi Cerita");
    JLabel lCari        = new JLabel("Cari Data");
    // Text Field
    JTextField fIdBerkas = new JTextField();
    JTextField fId      = new JTextField();
    JTextField fJudul   = new JTextField();
    JTextField fIsi     - new JTextField();
    JTextField fCari    = new JTextField();
    // Button
    JButton bUpdate 	= new JButton("Ubah");
    JButton bHapus   	= new JButton("Hapus");
    JButton bKembali 	= new JButton("Kembali");
    JButton bLihat      = new JButton("Lihat Progres");
    //ComboBox
    JComboBox<Object> cJenis   = new JComboBox<>();
    DataCerita jenis;
    List<DataCerita> jeniss= new ArrayList<>();



    // Constructor
    public DaftarCerita(){
        // Set Permission
        if(UserSession.getId_user() == null){
            JOptionPane.showMessageDialog(null, "Silahkan login terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            window.setVisible(false);
            new Login();
        }
        else {
            loadData();
            loadJenisCerita();
            initComponents();
            initListeners();
        }
    }

    // Set Component Swing
    private void initComponents(){
        // Set Table Component
        model = new DefaultTableModel(datas,kolom);
        sorter= new TableRowSorter<>(model);
        tTable= new JTable(model);
        tTable.setRowSorter(sorter);
        pane = new JScrollPane(tTable);
        tTable.setBackground(new Color(247, 252, 255));
        TableColumnModel columnModel = tTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(60);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(30);
        columnModel.getColumn(4).setPreferredWidth(10);

        // add Component to panel and Set Button
        window.add(lId);
        window.add(fIdBerkas);
        window.add(lJenis);
        window.add(lJudul);
        window.add(lIsi);
        window.add(bUpdate);
            bUpdate.setForeground(new Color(255, 255, 255));
            bUpdate.setBackground(new Color(168, 168, 50));
        window.add(bKembali);
            bKembali.setForeground(new Color(255, 255, 255));
            bKembali.setBackground(new Color(82, 77, 64));
        window.add(bLihat);
            bLihat.setForeground(new Color(255, 255, 255));
            bLihat.setBackground(new Color(58, 133, 86));
        window.add(pane);
            pane.setBackground(new Color(247, 252, 255));

        // Set Table Component
        window.getContentPane().setBackground(new Color(28, 27, 27));
        window.setLayout(null);
        window.setSize(690, 590);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setResizable(false);

        // Set Component ADMIN
        if(UserSession.getRole()==1) {
            window.setTitle("Kelola Cerita");
            lId.setBounds(20, 20, 150, 20);
            lId.setForeground(new Color(255, 255, 255));
            fIdBerkas.setBounds(130, 20, 170, 25);
            fIdBerkas.setEditable(false);

            lJenis.setBounds(20, 63, 150, 20);
            lJenis.setForeground(new Color(255, 255, 255));
            window.add(cJenis);
            cJenis.setBounds(130, 60, 170, 25);

            lJudul.setBounds(340, 23, 150, 20);
            lJudul.setForeground(new Color(255, 255, 255));
            window.add(fJudul);
            fJudul.setBounds(450, 20, 200, 25);

            lIsi.setBounds(340, 63, 150, 20);
            lIsi.setForeground(new Color(255, 255, 255));
            window.add(fIsi);
            fIsi.setBounds(450, 20, 200, 25);

            window.add(lCari);
            lCari.setBounds(20,100,150,20);
            lCari.setForeground(new Color(255, 255, 255));
                window.add(fCari);
                fCari.setBounds(130,100,520,25);

            bUpdate.setBounds(185, 500, 140, 30);
            bHapus.setBounds(345, 500, 140, 30);
            bKembali.setBounds(510, 500, 140, 30);
            bLihat.setBounds(20,500,140,30);
            pane.setBounds(20, 140, 630, 340);

            window.add(bHapus);
            bHapus.setForeground(new Color(255, 255, 255));
            bHapus.setBackground(new Color(102, 55, 51));
        }else{ // Set Component USER
            window.setTitle("Daftar Cerita");
            pane.setBounds(20, 20, 630, 340);
            bLihat.setBounds(20,400,190,30);
            bKembali.setBounds(220, 400, 140, 30);
        }
    }

    // Set EventHandling
    private void initListeners(){
        // Set Event Table, if mouse clicked show data on JTextField and JCombobox
        tTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int baris = tTable.rowAtPoint(e.getPoint());
                    String id = tTable.getValueAt(baris, 1).toString();
                    fIdBerkas.setText(id);
                    String judul = tTable.getValueAt(baris, 4).toString();
                    fJudul.setText(judul);
                   
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

        // Update Data
        bUpdate.addActionListener(e -> {
        String sql, sql2, check;
            try {
                statement = koneksi.getConnection().createStatement();
                String idCerita = jeniss.get(cJenis.getSelectedIndex()).getIdCerita();
                System.out.println(idCerita);
                check = "SELECT * FROM berkas WHERE id_berkas='"+fIdBerkas.getText()+"' AND judul='"+fJudul.getText()+"' AND id_cerita='"+ idCerita +"'";
                resultSet = statement.executeQuery(check);
                if(!resultSet.next()) {
                    String isi    = jeniss.get(cJenis.getSelectedIndex()).getIsiCerita();
                    
                    sql = "UPDATE berkas set id_cerita='" + idCerita + "',judul='" + fJudul.getText() + "' WHERE id_berkas='" + fIdBerkas.getText() + "'";
                }else {
                    sql = "UPDATE berkas set id_cerita='" + idCerita + "',judul='" + fJudul.getText() + "' WHERE idb_berkas='" + fIdBerkas.getText() + "'";
                }

            } catch (SQLException sqlError) {
                JOptionPane.showMessageDialog(null, "Gagal Update Data! error : " + sqlError);
            } catch (ClassNotFoundException classError) {
                JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
            }
        });
        // Delete Data
        bHapus.addActionListener(ae -> {
            int result = JOptionPane.showConfirmDialog(null, "Yakin ingin menghapus data dengan Kode "+fIdBerkas.getText()+"?", "INFO", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try {
                        statement = koneksi.getConnection().createStatement();
                        String sql = "DELETE FROM  berkas WHERE id_berkas='" + fIdBerkas.getText() + "'";
                        statement.execute(sql);
                        JOptionPane.showMessageDialog(null, "Berhasil Hapus Data!", "Informasi", JOptionPane.WARNING_MESSAGE);
                        statement.close();
                        window.setVisible(false);
                        new DaftarCerita();
                    }catch(HeadlessException | SQLException | ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage());
                    }
            }
        });

        // Back
        bKembali.addActionListener(e -> {
            window.setVisible(false);
            new MenuUtama();
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
            public void search(String str) {
                if (str.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(str));
                }
            }
        });
    

    // Load Data on SQL and then import to JTable
    private void loadData(){
        String sql;
        try {
            statement = koneksi.getConnection().createStatement();
            if (UserSession.getRole()==1) {
                sql = "SELECT * FROM berkas a INNER JOIN cerita b ON a.id_cerita=b.id_cerita INNER JOIN user c ON a.id_user=c.id_user";

            } else {
                sql = "SELECT * FROM berkas a INNER JOIN cerita b ON a.id_cerita=b.id_cerita INNER JOIN user c ON a.id_user=c.id_user WHERE a.id_user='" + UserSession.getId_user() + "'";
            }
            resultSet = statement.executeQuery(sql);

            int row = 0;
                while (resultSet.next()) {
                    datas[row][0] = resultSet.getString("username");
                    datas[row][1] = resultSet.getString("id_berkas");
                    datas[row][2] = resultSet.getString("jenis_cerita");
                    datas[row][3] = resultSet.getString("judul");
                    datas[row][4] = resultSet.getString("isi");
                    row++;
            }
            statement.close();

        } catch (SQLException sqlError) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan" + sqlError);
        } catch (ClassNotFoundException classError) {
            JOptionPane.showMessageDialog(null, "Driver tidak ditemukan !!");
        }
    }

    // Get All Jenis with Arraylist (Save on Arraylist)
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
    // Load Jenis Cerita and then import to JCombobox
    private void loadJenisCerita(){
        cJenis.removeAllItems();
        List<DataCerita> jeniss = getAllJenis();
        for(DataCerita data:jeniss){
            cJenis.addItem(data.getJenisCerita());
        }
    }
}
