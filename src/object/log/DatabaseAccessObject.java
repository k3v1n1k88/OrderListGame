/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object.log;

import database.connection.DatabaseConnection;
import exception.DatabaseException;

/**
 *
 * @author root
 */
public interface DatabaseAccessObject {
    public boolean access(DatabaseConnection dbcnn) throws DatabaseException;
}
