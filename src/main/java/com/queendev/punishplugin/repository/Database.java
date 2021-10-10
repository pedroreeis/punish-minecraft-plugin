package com.queendev.punishplugin.repository;

import java.sql.Connection;

public interface Database {

    void createTable();
    void open();
    void close();

    Connection getConnection();
}
