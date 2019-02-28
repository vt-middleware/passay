/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.Arrays;
import java.util.List;

/**
 * Contains convenience methods for testing.
 *
 * @author  Middleware Services
 */
public final class TestUtils
{


  /** Default constructor. */
  private TestUtils() {}


  /**
   * See {@link #newPasswordData(String, String, PasswordData.Origin, List)}.
   *
   * @param  p  password
   * @param  u  username
   *
   * @return  password data
   */
  public static PasswordData newPasswordData(final String p, final String u)
  {
    return newPasswordData(p, u, null, (PasswordData.Reference[]) null);
  }


  /**
   * See {@link #newPasswordData(String, String, PasswordData.Origin, List)}.
   *
   * @param  p  password
   * @param  u  username
   * @param  r  references
   *
   * @return  password data
   */
  public static PasswordData newPasswordData(final String p, final String u, final PasswordData.Reference... r)
  {
    return newPasswordData(p, u, r != null ? Arrays.asList(r) : null);
  }


  /**
   * See {@link #newPasswordData(String, String, PasswordData.Origin, List)}.
   *
   * @param  p  password
   * @param  u  username
   * @param  r  references
   *
   * @return  password data
   */
  public static PasswordData newPasswordData(final String p, final String u, final List<PasswordData.Reference> r)
  {
    return newPasswordData(p, u, PasswordData.Origin.User, r);
  }


  /**
   * See {@link #newPasswordData(String, String, PasswordData.Origin, List)}.
   *
   * @param  p  password
   * @param  u  username
   * @param  o  origin
   * @param  r  references
   *
   * @return  password data
   */
  public static PasswordData newPasswordData(final String p, final String u,
    final PasswordData.Origin o, final PasswordData.Reference... r)
  {
    return newPasswordData(p, u, o, r != null ? Arrays.asList(r) : null);
  }


  /**
   * Convenience method for creating a password data with all of its properties. Properties are ignored if they are
   * null.
   *
   * @param  p  password
   * @param  u  username
   * @param  o  origin
   * @param  r  references
   *
   * @return  password data
   */
  public static PasswordData newPasswordData(final String p, final String u,
    final PasswordData.Origin o, final List<PasswordData.Reference> r)
  {
    final PasswordData pd = new PasswordData();
    if (p != null) {
      pd.setPassword(p);
    }
    if (u != null) {
      pd.setUsername(u);
    }
    if (o != null) {
      pd.setOrigin(o);
    }
    if (r != null) {
      pd.setPasswordReferences(r);
    }
    return pd;
  }
}
