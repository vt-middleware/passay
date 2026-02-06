/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.dictionary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.hsqldb.jdbc.JDBCDataSource;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

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
  @BeforeClass
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
  @AfterClass
  public void closeDictionary()
  {
    caseSensitive = null;
    caseInsensitive = null;
  }

  /**
   * Test search.
   */
  @Test
  public void search()
  {
    assertThat(caseSensitive.search("manipular")).isTrue();
    assertThat(caseSensitive.search(FALSE_SEARCH)).isFalse();
    assertThat(caseSensitive.search("z")).isTrue();
    assertThat(caseInsensitive.search("manipular")).isTrue();
    assertThat(caseInsensitive.search("manipular".toUpperCase())).isTrue();
    assertThat(caseInsensitive.search(FALSE_SEARCH)).isFalse();
    assertThat(caseInsensitive.search("z")).isTrue();
  }


  /**
   * This test is disabled by default. It produces a lot of testing report data which runs the process OOM.
   *
   * @param  word  to search for.
   */
  @Test(dataProvider = "all-web-words", enabled = false)
  public void searchAll(final String word)
  {
    assertThat(caseSensitive.search(word)).isTrue();
    assertThat(caseInsensitive.search(word)).isTrue();
    assertThat(caseInsensitive.search(word.toLowerCase())).isTrue();
    assertThat(caseInsensitive.search(word.toUpperCase())).isTrue();
  }


  /**
   * Test size.
   */
  @Test
  public void size()
  {
    assertThat(caseSensitive.size()).isEqualTo(dictionarySize);
    assertThat(caseInsensitive.size()).isEqualTo(dictionarySize);
  }
}
