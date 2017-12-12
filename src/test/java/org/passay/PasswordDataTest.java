/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Map;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Unit test for {@link PasswordData}.
 *
 * @author  Middleware Services
 */
public class PasswordDataTest
{


  /**
   * Test for null input.
   */
  @Test(groups = {"passtest"})
  public void nullChecks()
  {
    try {
      new PasswordData(null);
      AssertJUnit.fail("Should have thrown NPE");
    } catch (NullPointerException e) {
      AssertJUnit.assertEquals(NullPointerException.class, e.getClass());
    }

    try {
      new PasswordData("username", (String) null);
      AssertJUnit.fail("Should have thrown NPE");
    } catch (NullPointerException e) {
      AssertJUnit.assertEquals(NullPointerException.class, e.getClass());
    }

    try {
      new PasswordData(null, "password");
      AssertJUnit.fail("Should have thrown NPE");
    } catch (NullPointerException e) {
      AssertJUnit.assertEquals(NullPointerException.class, e.getClass());
    }

    try {
      new PasswordData(null, PasswordData.Origin.User);
      AssertJUnit.fail("Should have thrown NPE");
    } catch (NullPointerException e) {
      AssertJUnit.assertEquals(NullPointerException.class, e.getClass());
    }

    try {
      new PasswordData("password", (PasswordData.Origin) null);
      AssertJUnit.fail("Should have thrown NPE");
    } catch (NullPointerException e) {
      AssertJUnit.assertEquals(NullPointerException.class, e.getClass());
    }

    try {
      new PasswordData(null, "password", PasswordData.Origin.User);
      AssertJUnit.fail("Should have thrown NPE");
    } catch (NullPointerException e) {
      AssertJUnit.assertEquals(NullPointerException.class, e.getClass());
    }

    try {
      new PasswordData("username", null, PasswordData.Origin.User);
      AssertJUnit.fail("Should have thrown NPE");
    } catch (NullPointerException e) {
      AssertJUnit.assertEquals(NullPointerException.class, e.getClass());
    }

    try {
      new PasswordData("username", "password", null);
      AssertJUnit.fail("Should have thrown NPE");
    } catch (NullPointerException e) {
      AssertJUnit.assertEquals(NullPointerException.class, e.getClass());
    }
  }


  /**
   * Unit test for {@link PasswordData#getPasswordReferences(Class)}.
   */
  @Test(groups = {"passtest"})
  public void references()
  {
    final PasswordData data = new PasswordData();
    data.setPasswordReferences(
      new PasswordData.HistoricalReference("history", "history01"),
      new PasswordData.SourceReference("source-a", "sourceA"),
      new PasswordData.SourceReference("source-b", "sourceB"));
    AssertJUnit.assertEquals(3, data.getPasswordReferences().size());
    AssertJUnit.assertEquals(1, data.getPasswordReferences(PasswordData.HistoricalReference.class).size());
    AssertJUnit.assertEquals(2, data.getPasswordReferences(PasswordData.SourceReference.class).size());
  }


  /**
   * Unit test for {@link PasswordData#getCounts(CharacterData...)}.
   */
  @Test(groups = {"passtest"})
  public void getCounts()
  {
    final PasswordData data = new PasswordData("user01", "p@SSw0rd#!1");
    final Map<CharacterData, Long> counts = data.getCounts(
      EnglishCharacterData.LowerCase,
      EnglishCharacterData.UpperCase,
      EnglishCharacterData.Alphabetical,
      EnglishCharacterData.Digit,
      EnglishCharacterData.Special);
    AssertJUnit.assertEquals(Long.valueOf(4), counts.get(EnglishCharacterData.LowerCase));
    AssertJUnit.assertEquals(Long.valueOf(2), counts.get(EnglishCharacterData.UpperCase));
    AssertJUnit.assertEquals(Long.valueOf(6), counts.get(EnglishCharacterData.Alphabetical));
    AssertJUnit.assertEquals(Long.valueOf(2), counts.get(EnglishCharacterData.Digit));
    AssertJUnit.assertEquals(Long.valueOf(3), counts.get(EnglishCharacterData.Special));
  }
}
