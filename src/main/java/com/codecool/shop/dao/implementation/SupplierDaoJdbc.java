package com.codecool.shop.dao.implementation;

import com.codecool.shop.connection.ConnectionManager;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Customer;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJdbc implements SupplierDao{

    private Logger logger = LoggerFactory.getLogger(SupplierDaoJdbc.class);


    private static SupplierDaoJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    private SupplierDaoJdbc() {
    }

    public static SupplierDaoJdbc getInstance() {
        if (instance == null) {
            instance = new SupplierDaoJdbc();
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        try {
            PreparedStatement ps = (ConnectionManager.getConnection()).prepareStatement("INSERT INTO supplier (name, description) VALUES(?,?);");
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getDescription());
            ps.execute();
            logger.info("New supplier: name={} added to supplier table in the database", supplier.getName());

            ps =(ConnectionManager.getConnection()).prepareStatement("SELECT MAX(id) as id FROM supplier;");
            ResultSet rs = ps.executeQuery();
            rs.next();
            supplier.setId(rs.getInt("id"));
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error while adding supplier with id={}, name={} to the database. Message: {}", supplier.getId(), supplier.getName(), e.getMessage());


        }
    }

    @Override
    public Supplier find(int id) {
        Supplier supplier = null;
        String name;
        String description;

        try {
            PreparedStatement ps = (ConnectionManager.getConnection()).prepareStatement("SELECT * FROM supplier WHERE id = ?;");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                name = rs.getString(1);
                description = rs.getString(2);

                supplier = new Supplier(name, description);
                supplier.setId(id);
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error while looking for supplier with id: {} in the database. Message: {}", id, e.getMessage());

        }
        return supplier;
    }


    @Override
    public void remove(int id) {
        try {
            PreparedStatement ps = (ConnectionManager.getConnection()).prepareStatement("DELETE FROM supplier WHERE id = ?;");
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error while removing supplier with id: {} from the database. Message: {}", id, e.getMessage());

        }
    }


    @Override
    public List<Supplier> getAll() {
        List<Supplier> listOfSuppliers = new ArrayList<>();
        Supplier supplier = null;
        String name;
        String description;

        try {
            PreparedStatement ps = (ConnectionManager.getConnection()).prepareStatement("SELECT * FROM supplier");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                name = rs.getString(2);
                description = rs.getString(3);

                supplier = new Supplier(name, description);
                supplier.setId(rs.getInt(1));
                listOfSuppliers.add(supplier);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error while querying all supplier from the database. Message: {}", e.getMessage());

        }
        return listOfSuppliers;
    }
 }
