import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyFrame extends JFrame {

    Connection conn                 =null;
    PreparedStatement state         =null;
    ResultSet result                =null;

    int weaponsId;
    String weaponsName;
    int weaponsAttack;
    int weaponsDefence;

    int armorId;
    String armorName;
    int armorAttack;
    int armorDefence;

    int id;
    int reset = 0;

    JTable playerTable              = new JTable();
    JTable armorTable               = new JTable();
    JTable weaponTable              = new JTable();

    JButton buyWeaponButton         = new JButton();
    JButton buyArmorButton          = new JButton();
    JButton sellButton              = new JButton();
    JButton upgradeButton           = new JButton();
    JButton searchButton            = new JButton();
    //clear selected object

    SearchPanel searchPanel         = new SearchPanel();
    MainPanel playerPanel           = new MainPanel (0,0,400,1000);
    MainPanel weaponTraderPanel     = new MainPanel (400,0,400,1000);
    MainPanel armorTraderPanel      = new MainPanel (800,0,400,1000);
    int count = 0;

    public MyFrame(){

        //----------------------------------------------------- BUY BUTTON FOR WEAPONS
        buyWeaponButton.setText("Buy");
        buyWeaponButton.addActionListener(e -> {
            conn            =DBConnection.getConnection();
            String sql      ="delete from weapons_trader where weapon_id=?";
            String insertSql="INSERT INTO player (armor_id, weapon_id, item_name, attack, defence) values(?,?,?,?,?)";

            try {

                state = conn.prepareStatement(sql);
                state.setInt(1, weaponsId);
                state.execute();
                refreshTables();

                state = conn.prepareStatement(insertSql);
                state.setInt(1, 0);
                state.setInt(2, weaponsId);
                state.setString(3, weaponsName);
                state.setInt(4, weaponsAttack);
                state.setInt(5, weaponsDefence);

                state.execute();
                refreshTables();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        });
        //----------------------------------------------------- BUY BUTTON FOR ARMORS
        buyArmorButton.setText("Buy");
        buyArmorButton.addActionListener(e -> {
            conn            =DBConnection.getConnection();
            String sql      ="delete from armor_trader where armor_id=?";
            String insertSql="INSERT INTO player (armor_id, weapon_id, item_name, attack, defence) values(?,?,?,?,?)";

            try {

                state = conn.prepareStatement(sql);
                state.setInt(1, armorId);
                state.execute();
                refreshTables();

                state = conn.prepareStatement(insertSql);
                state.setInt(1, armorId);
                state.setInt(2, 0);
                state.setString(3, armorName);
                state.setInt(4, armorAttack);
                state.setInt(5, armorDefence);

                state.execute();
                refreshTables();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        //----------------------------------------------------- UPGRADE BUTTON
        upgradeButton.setText("UPGRADE");
        upgradeButton.addActionListener(e -> {
            int upgradeNumber   = 5;
            conn                =DBConnection.getConnection();

            try {
                if(weaponsId == 0){
                    String insertSql="UPDATE player SET defence=? WHERE armor_id=?";

                    state       =conn.prepareStatement(insertSql);
                    state.setInt(2, armorId);
                    state.setInt(1, armorDefence + upgradeNumber);
                    state.execute();
                    refreshTables();

                }

                if(armorId == 0){
                    String insertSql="UPDATE player SET attack=? WHERE weapon_id=?";

                    state       =conn.prepareStatement(insertSql);
                    state.setInt(2, weaponsId);
                    state.setInt(1, weaponsAttack + upgradeNumber);
                    state.execute();
                    refreshTables();

                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        //----------------------------------------------------- SELL BUTTON

        sellButton.setText("Sell");
        sellButton.addActionListener(e -> {
            conn        =DBConnection.getConnection();
            String sql  ="delete from player where id=?";


            try {
                state   =conn.prepareStatement(sql);
                state.setInt(1,id);
                state.execute();
                refreshTables();

                if(weaponsId == 0){
                    String insertSql="INSERT INTO armor_trader (armor_id, item_name, attack, defence) values(?,?,?,?)";

                    state=conn.prepareStatement(insertSql);
                    state.setInt(1 , armorId);
                    state.setString(2, armorName);
                    state.setInt(3, armorAttack);
                    state.setInt(4, armorDefence);
                    state.execute();
                    refreshTables();

                }

                if(armorId == 0){
                    String insertSql="INSERT INTO weapons_trader (weapon_id, item_name, attack, defence) values(?,?,?,?)";

                    state=conn.prepareStatement(insertSql);
                    state.setInt(1 , weaponsId);
                    state.setString(2, weaponsName);
                    state.setInt(3, weaponsAttack);
                    state.setInt(4, weaponsDefence);
                    state.execute();
                    refreshTables();

                }

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        //----------------------------------------------------- SEARCH BUTTON

        searchButton.setText("SEARCH");
        searchButton.addActionListener(e -> {

            if(count == 0) {
                searchPanel.setVisible(true);
                this.setSize(1600,1000);
                count = 1;
            }else {
                searchPanel.setVisible(false);
                this.setSize(1200,1000);
                count = 0;
            }
        });

        //----------------------------------------------------- FRAME SETTINGS

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1200,1000);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        //----------------------------------------------------- PLAYER
        playerPanel.add(playerTable);
        playerPanel.setBackground(Color.lightGray);
        playerPanel.add(searchButton);
        playerPanel.add(upgradeButton);
        playerPanel.add(sellButton);
        playerTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                // if(weapons id=0){armor}    if(armor id = 0){weapon}
                int row             = playerTable.getSelectedRow();
                id                  = Integer.parseInt(playerTable.getValueAt(row,0).toString());
                System.out.println(id);

                armorId             = Integer.parseInt(playerTable.getValueAt(row,1).toString());
                weaponsId           = Integer.parseInt(playerTable.getValueAt(row,2).toString());

                if(weaponsId == 0){
                    armorName       = playerTable.getValueAt(row,3).toString();
                    armorAttack     = Integer.parseInt(playerTable.getValueAt(row,4).toString());
                    armorDefence    = Integer.parseInt(playerTable.getValueAt(row,5).toString());
                    System.out.println(armorId);

                }

                if(armorId == 0){
                    weaponsName     = playerTable.getValueAt(row,3).toString();
                    weaponsAttack   = Integer.parseInt(playerTable.getValueAt(row,4).toString());
                    weaponsDefence  = Integer.parseInt(playerTable.getValueAt(row,5).toString());
                    System.out.println(weaponsId);

                }
            }

        });

        //----------------------------------------------------- WEAPON TRADER
        weaponTraderPanel.setBackground(Color.gray);
        weaponTraderPanel.add(weaponTable);
        weaponTraderPanel.add(buyWeaponButton);
        weaponTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int row         = weaponTable.getSelectedRow();
                weaponsId       = Integer.parseInt(weaponTable.getValueAt(row,0).toString());
                weaponsName     = weaponTable.getValueAt(row,1).toString();
                weaponsAttack   = Integer.parseInt(weaponTable.getValueAt(row,2).toString());
                weaponsDefence  = Integer.parseInt(weaponTable.getValueAt(row,3).toString());
                System.out.println(weaponsId);
            }

        });

        //----------------------------------------------------- ARMOR TRADER
        armorTraderPanel.setBackground(Color.darkGray);
        armorTraderPanel.add(armorTable);
        armorTraderPanel.add(buyArmorButton);
        armorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row         = armorTable.getSelectedRow();
                armorId         = Integer.parseInt(armorTable.getValueAt(row,0).toString());
                armorName       = armorTable.getValueAt(row,1).toString();
                armorAttack     = Integer.parseInt(armorTable.getValueAt(row,2).toString());
                armorDefence    = Integer.parseInt(armorTable.getValueAt(row,3).toString());
                System.out.println(armorId);
            }
        });

        //----------------------------------------------------- FRAME ADD METHODS
        this.add(searchPanel);
        this.add(playerPanel);
        this.add(weaponTraderPanel);
        this.add(armorTraderPanel);

        refreshTables();

    }

    public void refreshTables(){

        refreshTableForPlayer();
        refreshTableForWeaponTrader();
        refreshTableForArmorTrader();

    }

    private void refreshTableForPlayer() {
        conn=DBConnection.getConnection();
        try {
            state   =conn.prepareStatement("select * from player");
            result  =state.executeQuery();
            playerTable.setModel(new Model(result));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTableForWeaponTrader() {
        conn=DBConnection.getConnection();
        try {
            state   =conn.prepareStatement("select * from weapons_trader");
            result  =state.executeQuery();
            weaponTable.setModel(new Model(result));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void refreshTableForArmorTrader() {
        conn=DBConnection.getConnection();
        try {
            state   =conn.prepareStatement("select * from armor_trader");
            result  =state.executeQuery();
            armorTable.setModel(new Model(result));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
