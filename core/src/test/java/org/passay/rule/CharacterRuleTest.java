/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import org.passay.PasswordData;
import org.passay.data.EnglishCharacterData;
import org.testng.annotations.DataProvider;

/**
 * Unit test for {@link CharacterRule}.
 *
 * @author  Middleware Services
 */
public class CharacterRuleTest extends AbstractRuleTest
{


  /**
   * @return  Test data.
   */
  @DataProvider(name = "passwords")
  public Object[][] passwords()
  {
    return
      new Object[][] {

        // test valid password
        {new CharacterRule(EnglishCharacterData.LowerCase, 5), new PasswordData("bo3p$elW3ZZ"), null, },
        {new CharacterRule(EnglishCharacterData.UpperCase, 2), new PasswordData("bo3p$elW3ZZ"), null, },
        {new CharacterRule(EnglishCharacterData.Digit, 1), new PasswordData("bo3p$elW3ZZ"), null, },
        {new CharacterRule(EnglishCharacterData.SpecialAscii, 1), new PasswordData("bo3p$elW3ZZ"), null, },
        // test invalid password
        {
          new CharacterRule(EnglishCharacterData.LowerCase, 15),
          new PasswordData("bo3p$elW3ZZ"),
          EnglishCharacterData.LowerCase.getErrorCode(),
        },
        {
          new CharacterRule(EnglishCharacterData.UpperCase, 5),
          new PasswordData("bo3p$elW3ZZ"),
          EnglishCharacterData.UpperCase.getErrorCode(),
        },
        {
          new CharacterRule(EnglishCharacterData.Digit, 3),
          new PasswordData("bo3p$elW3ZZ"),
          EnglishCharacterData.Digit.getErrorCode(),
        },
        {
          new CharacterRule(EnglishCharacterData.SpecialAscii, 2),
          new PasswordData("bo3p$elW3ZZ"),
          EnglishCharacterData.SpecialAscii.getErrorCode(),
        },
        // test unicode
        {new CharacterRule(EnglishCharacterData.LowerCase, 4), new PasswordData("bo3p$ečW3ZZ"), null, },
        {
          new CharacterRule(EnglishCharacterData.LowerCase, 15),
          new PasswordData("bo3p$ečW3ZZ"),
          EnglishCharacterData.LowerCase.getErrorCode(),
        },
        {new CharacterRule(EnglishCharacterData.LowerCase, 4), new PasswordData("bo3p$e\u16C8W3ZZ"), null, },
        {
          new CharacterRule(EnglishCharacterData.LowerCase, 15),
          new PasswordData("bo3p$e\u16C8W3ZZ"),
          EnglishCharacterData.LowerCase.getErrorCode(),
        },
        {new CharacterRule(EnglishCharacterData.LowerCase, 4), new PasswordData("bo3p$e\uD808\uDF34W3ZZ"), null, },
        {
          new CharacterRule(EnglishCharacterData.LowerCase, 15),
          new PasswordData("bo3p$e\uD808\uDF34W3ZZ"),
          EnglishCharacterData.LowerCase.getErrorCode(),
        },
        {
          new CharacterRule(EnglishCharacterData.LowerCase, 4),
          new PasswordData("bo3p$e\uD83C\uDDEE\uD83C\uDDF8W3ZZ"),
          null,
        },
        {
          new CharacterRule(EnglishCharacterData.LowerCase, 15),
          new PasswordData("bo3p$e\uD83C\uDDEE\uD83C\uDDF8W3ZZ"),
          EnglishCharacterData.LowerCase.getErrorCode(),
        },
      };
  }


  /**
   * @return  Test data.
   */
  @DataProvider(name = "messages")
  public Object[][] messages()
  {
    return
      new Object[][] {
        {
          new CharacterRule(EnglishCharacterData.LowerCase, 15),
          new PasswordData("bo3p$elW3ZZ"),
          new String[] {String.format("Password must contain %s or more lowercase characters.", "15"), },
        },
        {
          new CharacterRule(EnglishCharacterData.UpperCase, 5),
          new PasswordData("bo3p$elW3ZZ"),
          new String[] {String.format("Password must contain %s or more uppercase characters.", "5"), },
        },
        {
          new CharacterRule(EnglishCharacterData.Digit, 3),
          new PasswordData("bo3p$elW3ZZ"),
          new String[] {String.format("Password must contain %s or more digit characters.", "3"), },
        },
        {
          new CharacterRule(EnglishCharacterData.SpecialAscii, 2),
          new PasswordData("bo3p$elW3ZZ"),
          new String[] {String.format("Password must contain %s or more special characters.", "2"), },
        },
      };
  }
}
