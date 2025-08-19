/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.passay.PassayUtils;

/**
 * Provides a {@link Dictionary} backed by a database table.
 *
 * @author  Middleware Services
 */
public class JDBCDictionary implements Dictionary
{

  /** JDBC data source. */
  protected final DataSource dataSource;

  /** SQL search statement. */
  protected final String searchStatement;

  /** SQL size statement. */
  protected final String sizeStatement;


  /**
   * Creates a new JDBC dictionary.
   *
   * @param  source  connection data source
   * @param  searchSql  prepared statement to query for words; first parameter is the word
   * @param  sizeSql  prepared statement to query for size; no parameters are provided
   */
  public JDBCDictionary(final DataSource source, final String searchSql, final String sizeSql)
  {
    dataSource = PassayUtils.assertNotNullArg(source, "Data source cannot be null");
    searchStatement = PassayUtils.assertNotNullArg(searchSql, "Search SQL cannot be null");
    sizeStatement = PassayUtils.assertNotNullArg(sizeSql, "Size SQL cannot be null");
  }


  @Override
  public boolean search(final String word)
  {
    try {
      return executeStatement(searchStatement, String.class, word) != null;
    } catch (SQLException e) {
      throw new RuntimeException("Error executing SQL", e);
    }
  }


  @Override
  public long size()
  {
    try {
      return executeStatement(sizeStatement, Long.class, (Object[]) null);
    } catch (SQLException e) {
      throw new RuntimeException("Error executing SQL", e);
    }
  }


  /**
   * Executes a prepared statement against the database. Only the first result is returned.
   *
   * @param  statement  to execute
   * @param  type  of object to return
   * @param  params  to set on the prepared statement
   *
   * @param  <T>  return type
   *
   * @return  first result of the prepared statement
   *
   * @throws  SQLException  if the statement execution fails
   */
  protected <T> T executeStatement(final String statement, final Class<T> type, final Object... params)
    throws SQLException
  {
    try (Connection c = getConnection()) {
      try (PreparedStatement stmt = c.prepareStatement(statement)) {
        if (params != null && params.length > 0) {
          for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
          }
        }
        try (ResultSet rs = stmt.executeQuery()) {
          if (rs.next()) {
            return rs.getObject(1, type);
          }
        }
      }
    }
    return null;
  }


  /**
   * Returns a connection that is ready for use.
   *
   * @return  database connection.
   *
   * @throws  SQLException  if a connection is not available
   */
  protected Connection getConnection() throws SQLException
  {
    return dataSource.getConnection();
  }
}
