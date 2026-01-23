/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.passay.support.Origin;
import org.passay.support.Reference;

/**
 * Contains password related information used by rules to perform password validation.
 *
 * @author  Middleware Services
 */
public class PasswordData
{

  /** Stores the password. */
  private final UnicodeString password;

  /** Stores the username. */
  private final UnicodeString username;

  /** Password references. */
  private final List<Reference> passwordReferences = new ArrayList<>();

  /** Origin of this password. */
  private final Origin origin;


  /**
   * Creates a new password data. The origin of this data is {@link Origin#User} by default.
   *
   * @param  password  password
   */
  public PasswordData(final CharSequence password)
  {
    this(new UnicodeString(password));
  }


  /**
   * Creates a new password data. The origin of this data is {@link Origin#User} by default.
   *
   * @param  password  password
   */
  public PasswordData(final UnicodeString password)
  {
    this(null, password, Origin.User, Collections.emptyList());
  }


  /**
   * Creates a new password data. The origin of this data is {@link Origin#User} by default.
   *
   * @param  username  username
   * @param  password  password
   */
  public PasswordData(final CharSequence username, final CharSequence password)
  {
    this(new UnicodeString(username), new UnicodeString(password));
  }


  /**
   * Creates a new password data. The origin of this data is {@link Origin#User} by default.
   *
   * @param  username  username
   * @param  password  password
   */
  public PasswordData(final UnicodeString username, final UnicodeString password)
  {
    this(username, password, Origin.User, Collections.emptyList());
  }


  /**
   * Creates a new password data.
   *
   * @param  password  password
   * @param  origin  origin
   */
  public PasswordData(final CharSequence password, final Origin origin)
  {
    this(new UnicodeString(password), origin);
  }


  /**
   * Creates a new password data.
   *
   * @param  password  password
   * @param  origin  origin
   */
  public PasswordData(final UnicodeString password, final Origin origin)
  {
    this(null, password, origin, Collections.emptyList());
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  origin  origin
   */
  public PasswordData(final CharSequence username, final CharSequence password, final Origin origin)
  {
    this(new UnicodeString(username), new UnicodeString(password), origin);
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  origin  origin
   */
  public PasswordData(final UnicodeString username, final UnicodeString password, final Origin origin)
  {
    this(username, password, origin, Collections.emptyList());
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  references  references
   */
  public PasswordData(final CharSequence username, final CharSequence password, final Reference... references)
  {
    this(new UnicodeString(username), new UnicodeString(password), references);
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  references  references
   */
  public PasswordData(final UnicodeString username, final UnicodeString password, final Reference... references)
  {
    this(username, password, Origin.User, Arrays.asList(references));
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  references  references
   */
  public PasswordData(final CharSequence username, final CharSequence password, final List<Reference> references)
  {
    this(new UnicodeString(username), new UnicodeString(password), references);
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  references  references
   */
  public PasswordData(final UnicodeString username, final UnicodeString password, final List<Reference> references)
  {
    this(username, password, Origin.User, references);
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  origin  origin
   * @param  references  references
   */
  public PasswordData(
    final CharSequence username, final CharSequence password, final Origin origin, final Reference... references)
  {
    this(new UnicodeString(username), new UnicodeString(password), origin, references);
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  origin  origin
   * @param  references  references
   */
  public PasswordData(
    final UnicodeString username, final UnicodeString password, final Origin origin, final Reference... references)
  {
    this(username, password, origin, Arrays.asList(references));
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  origin  origin
   * @param  references  references
   */
  public PasswordData(
    final CharSequence username, final CharSequence password, final Origin origin, final List<Reference> references)
  {
    this(new UnicodeString(username), new UnicodeString(password), origin, references);
  }


  /**
   * Creates a new password data.
   *
   * @param  username  username
   * @param  password  password
   * @param  origin  origin
   * @param  references  references
   */
  public PasswordData(
    final UnicodeString username, final UnicodeString password, final Origin origin, final List<Reference> references)
  {
    this.username = username;
    this.password = PassayUtils.assertNotNullArg(password, "Password cannot be null");
    this.origin = PassayUtils.assertNotNullArg(origin, "Origin cannot be null");
    if (references != null) {
      this.passwordReferences.addAll(
        PassayUtils.assertNotNullArgOr(
          references,
          v -> v.stream().anyMatch(Objects::isNull),
          "Reference values cannot be null or contain null"));
    }
  }


  /**
   * Returns the password.
   *
   * @return  password
   */
  public UnicodeString getPassword()
  {
    return password;
  }


  /**
   * Returns the origin.
   *
   * @return  origin
   */
  public Origin getOrigin()
  {
    return origin;
  }


  /**
   * Returns the username.
   *
   * @return  username
   */
  public UnicodeString getUsername()
  {
    return username;
  }


  /**
   * Returns the password references.
   *
   * @return  password references
   */
  public List<Reference> getPasswordReferences()
  {
    return Collections.unmodifiableList(passwordReferences);
  }


  /**
   * Returns the password references that match the supplied reference type.
   *
   * @param  <T>  type of password reference
   * @param  type  of reference to match
   *
   * @return  unmodifiable list of password references
   */
  @SuppressWarnings("unchecked")
  public <T extends Reference> List<T> getPasswordReferences(final Class<T> type)
  {
    final List<T> l = passwordReferences.stream().filter(
      type::isInstance).map(r -> (T) r).collect(Collectors.toList());
    return Collections.unmodifiableList(l);
  }


  /**
   * Clears the memory of the underlying objects in this password data. See {@link UnicodeString#clear()}.
   */
  public void clear()
  {
    if (password != null) {
      password.clear();
    }
    if (username != null) {
      username.clear();
    }
    passwordReferences.forEach(Reference::clear);
  }


  /**
   * Returns a new password data initialized with the supplied data.
   *
   * @param  data  password data to read properties from
   *
   * @return  new password data
   */
  public static PasswordData copy(final PasswordData data)
  {
    return new PasswordData(
      data.getUsername(), data.getPassword(), data.getOrigin(), data.getPasswordReferences());
  }


  @Override
  public String toString()
  {
    return getClass().getName() + "@" + hashCode() + "::" +
      "username=" + username + ", " +
      "password=" + password + ", " +
      "origin=" + origin + ", " +
      "passwordReferences=" + passwordReferences;
  }
}
