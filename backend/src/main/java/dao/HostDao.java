package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Host;
import utils.Encrypt;


public class HostDao {

    //? ----------- Create - POST -----------

    public void insert(Host host) {
        String sql = "INSERT INTO Host (name, email, password, font_color, background_color) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, host.getName());
            stmt.setString(2, host.getEmail());
            
            //* Criptografa antes de salvar
            stmt.setString(3, Encrypt.encrypt(host.getPassword()));
            
            stmt.setString(4, host.getFontColor());
            stmt.setString(5, host.getBackgroundColor());
            stmt.executeUpdate();
            
            try (java.sql.ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    host.setId(rs.getInt(1));
                    System.out.println("[HostDao] Host inserido com ID: " + host.getId());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //? ----------- Read - GET -----------

    public List<Host> findAll() {
        List<Host> hosts = new ArrayList<>();
        String sql = "SELECT * FROM Host";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                hosts.add(new Host(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("font_color"),
                    rs.getString("background_color")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hosts;
    }

    public Host findById(int id) {
        String sql = "SELECT * FROM Host WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Host(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("font_color"),
                    rs.getString("background_color")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Host findByEmail(String email) {
        String sql = "SELECT * FROM Host WHERE email = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Host(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("font_color"),
                    rs.getString("background_color")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Host> findByEmailLike(String email) {
        List<Host> hosts = new ArrayList<>();
        String sql = "SELECT * FROM Host WHERE email LIKE ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + email + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                hosts.add(new Host(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("font_color"),
                    rs.getString("background_color")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hosts;
    }

    public List<Host> findByName(String name) {
        List<Host> hosts = new ArrayList<>();
        String sql = "SELECT * FROM Host WHERE name LIKE ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                hosts.add(new Host(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("font_color"),
                    rs.getString("background_color")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hosts;
    }

    //? ----------- Update - PUT -----------

    public void update(Host host) {
        String sql = "UPDATE Host SET name=?, email=?, password=?, font_color=?, background_color=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            String password = host.getPassword();
            if (password == null || password.isEmpty()) {
                Host current = findById(host.getId());
                
                if (current != null) password = current.getPassword();
            
            } else {
                password = Encrypt.encrypt(password);
            }

            stmt.setString(1, host.getName());
            stmt.setString(2, host.getEmail());
            stmt.setString(3, password);
            stmt.setString(4, host.getFontColor());
            stmt.setString(5, host.getBackgroundColor());
            stmt.setInt(6, host.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //? ----------- Delete - DELETE -----------

    public void delete(int id) {
        String sql = "DELETE FROM Host WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}