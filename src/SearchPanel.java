import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.Integer.parseInt;

public class SearchPanel extends JPanel{

    Connection conn                     = null;
    PreparedStatement state             = null;
    ResultSet result                    = null;

    JLabel attackSearchLabel            = new JLabel("Search by attack:");
    JLabel defenceSearchLabel           = new JLabel("Search by defence:");

    JTextField attackSearchField        = new JTextField();
    JTextField defenceSearchField       = new JTextField();
    JButton searchButton                = new JButton();
    JTable searchTable                  = new JTable();

    public SearchPanel() {

        //----------------------------------------------------- ATTACK SEARCH FIELD
        attackSearchLabel.setBounds(0,0,110,20);
        attackSearchLabel.setForeground(Color.white);
        attackSearchField.setBounds(120,0,100,20);
        //----------------------------------------------------- DEFENCE SEARCH FIELD
        defenceSearchLabel.setBounds(0,55,110,20);
        defenceSearchLabel.setForeground(Color.white);
        defenceSearchField.setBounds(120,55,100,20);
        //----------------------------------------------------- SEARCH BUTTON
        searchButton.setBounds(120,110,100,50);
        searchButton.setText("Search");
        searchButton.addActionListener(e -> searchAction());
        //----------------------------------------------------- SEARCH TABLE
        searchTable.setBounds(0,200,400,400);

        //----------------------------------------------------- SEARCH PANEL SETTINGS
        this.setBounds(1200,0,400,1000);
        this.setLayout(null);
        this.setBackground(new Color(51,51,51));
        this.setVisible(false);

        //----------------------------------------------------- SEARCH PANEL ADDONS
        this.add(searchTable);
        this.add(attackSearchLabel);
        this.add(defenceSearchLabel);
        this.add(attackSearchField);
        this.add(defenceSearchField);
        this.add(searchButton);

        refreshTableForSearch();

    }

    private void searchAction(){
        deleteFromSearchTable();
        if(!attackSearchField.getText().equals("")) {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO search_table (armor_id, weapon_id, item_name, attack, defence)\n" +
                    "SELECT 0, weapon_id, item_name, attack, defence\n" +
                    "FROM weapons_trader\n" +
                    "WHERE attack=? and defence = ?;";

            try {
                state = conn.prepareStatement(sql);
                state.setInt(1, parseInt(attackSearchField.getText()));
                state.setInt(2, parseInt(defenceSearchField.getText()));
                state.execute();

            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(!defenceSearchField.getText().equals("")) {
            conn = DBConnection.getConnection();
            String sql = "INSERT INTO search_table (armor_id, weapon_id, item_name, attack, defence)\n" +
                    "SELECT armor_id, 0, item_name, attack, defence\n" +
                    "FROM armor_trader\n" +
                    "WHERE defence=? and attack = ?;";

            try {
                state = conn.prepareStatement(sql);
                state.setInt(1, parseInt(defenceSearchField.getText()));
                state.setInt(2, parseInt(attackSearchField.getText()));
                state.execute();

            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        defenceSearchField.setText("");
        attackSearchField.setText("");
        refreshTableForSearch();

    }

    private void deleteFromSearchTable() {

        conn = DBConnection.getConnection();
        try {
            state   =conn.prepareStatement("delete from search_table");
            state.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void refreshTableForSearch() {
        conn = DBConnection.getConnection();
        try {
            state   =conn.prepareStatement("select * from search_table");
            result  =state.executeQuery();
            searchTable.setModel(new Model(result));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}