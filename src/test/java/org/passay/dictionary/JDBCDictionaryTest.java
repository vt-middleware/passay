/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.hsqldb.jdbc.JDBCDataSource;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link JDBCDictionary}.
 *
 * @author  Middleware Services
 */
public class JDBCDictionaryTest extends AbstractDictionaryTest
{

  /** Test dictionary. */
  private JDBCDictionary caseSensitive;

  /** Test dictionary. */
  private JDBCDictionary caseInsensitive;

  /** Size of the dictionary. */
  private long dictionarySize;

  /** @throws  Exception  On test failure. */
  @BeforeClass(groups = {"jdbcdicttest"})
  public void createDictionary() throws Exception
  {
    Class.forName("org.hsqldb.jdbcDriver");
    final JDBCDataSource dataSource = new JDBCDataSource();
    dataSource.setDatabase("jdbc:hsqldb:mem:passay");
    dataSource.setUser("sa");
    dataSource.setPassword("");

    try (Connection c = dataSource.getConnection()) {
      final Statement s = c.createStatement();
      s.executeUpdate("create table words (word varchar(255))");
      final PreparedStatement ps = c.prepareStatement("insert into words (word) values (?)");
      for (Object[] word : createAllWebWords()) {
        ps.setObject(1, word[0]);
        ps.executeUpdate();
        dictionarySize++;
      }
    }

    caseSensitive = new JDBCDictionary(
      dataSource,
      "select word from words where word = ?",
      "select count(*) from words");
    caseInsensitive = new JDBCDictionary(
      dataSource,
      "select lower(word) from words where word = lower(?)",
      "select count(*) from words");
  }

  /**
   * Close test resources.
   */
  @AfterClass(groups = {"jdbcdicttest"})
  public void closeDictionary()
  {
    caseSensitive = null;
    caseInsensitive = null;
  }

  /**
   * Test search.
   */
  @Test(groups = {"jdbcdicttest"})
  public void search()
  {
    AssertJUnit.assertTrue(caseSensitive.search("manipular"));
    AssertJUnit.assertFalse(caseSensitive.search(FALSE_SEARCH));
    AssertJUnit.assertTrue(caseSensitive.search("z"));
    AssertJUnit.assertTrue(caseInsensitive.search("manipular"));
    AssertJUnit.assertTrue(caseInsensitive.search("manipular".toUpperCase()));
    AssertJUnit.assertFalse(caseInsensitive.search(FALSE_SEARCH));
    AssertJUnit.assertTrue(caseInsensitive.search("z"));
  }


  /**
   * This test is disabled by default. It produces a lot of testing report data which runs the process OOM.
   *
   * @param  word  to search for.
   */
  @Test(groups = {"jdbcdicttest"}, dataProvider = "all-web-words", enabled = false)
  public void searchAll(final String word)
  {
    AssertJUnit.assertTrue(caseSensitive.search(word));
    AssertJUnit.assertTrue(caseInsensitive.search(word));
    AssertJUnit.assertTrue(caseInsensitive.search(word.toLowerCase()));
    AssertJUnit.assertTrue(caseInsensitive.search(word.toUpperCase()));
  }


  /**
   * Test size.
   */
  @Test(groups = {"jdbcdicttest"})
  public void size()
  {
    AssertJUnit.assertEquals(dictionarySize, caseSensitive.size());
    AssertJUnit.assertEquals(dictionarySize, caseInsensitive.size());
  }
}
