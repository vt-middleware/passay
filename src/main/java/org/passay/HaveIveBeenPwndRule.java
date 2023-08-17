/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Duration;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates if the password against the online database of <code>haveivebeenpwnd.org</code>
 * optionally allowing the usage of found passwords, but returns the number of found matches in
 * the metadata.
 *
 * @author Wolfgang Jung (post@wolfgang-jung.net)
 */
public class HaveIveBeenPwndRule implements Rule
{
  /**
   * Error code for known passwords.
   */
  public static final String ERROR_CODE = "HAVEIVEBEENPWND_ERROR";

  /**
   * number of chars to use from the SHA1 digest for the api call.
   */
  private static final int PREFIX_LENGTH = 5;

  /**
   * Name of the app.
   */
  private final String applicationName;

  /**
   * Address of the API
   */
  private URL remoteAddress;

  /**
   * Does a negative result (password known) from the API
   * invalidating the password.
   */
  private boolean mandatory = true;

  /**
   * Maximum waiting time for connection and reading.
   */
  private Duration timeout;

  /**
   * Should passwords be allowed, if the API is inaccessible.
   */
  private boolean allowPasswordDuringTimeout = true;

  /**
   * Create the rule, appName is required by the
   * <a href="https://haveibeenpwned.com/API/v3#UserAgent">API</a>.
   *
   * @param appName must not be null
   */
  public HaveIveBeenPwndRule(final String appName)
  {
    if (appName == null) {
      throw new IllegalArgumentException("appName must be set");
    }
    this.applicationName = appName;
    try {
      remoteAddress = new URL("https://api.pwnedpasswords.com/range/");
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Is the check mandatory.
   *
   * @param mandatoryCheck true: the rule does not allow previously pwnd passwords,
   *                       false: pwnd passwords are allowed, but the number of matches is returned in the result.
   */
  public void setMandatory(final boolean mandatoryCheck)
  {
    this.mandatory = mandatoryCheck;
  }

  /**
   * Set the API-Endpoint to a different server than <code>https://api.pwnedpasswords.com/range/</code>, e.g. in
   * self-hosted environments.
   *
   * @param address the URL must end with a <code>/</code>.
   */
  public void setRemoteAddress(final URL address)
  {
    if (!address.getPath().endsWith("/")) {
      throw new IllegalArgumentException("remoteAddress must end with /");
    }
    this.remoteAddress = address;
  }

  /**
   * maximum Duration for connecting or reading from the API, defaults to the system defaults.
   *
   * @param duration null for system default.
   */
  public void setTimeout(final Duration duration)
  {
    this.timeout = duration;
  }

  /**
   * If a timeout occurs during accessing the api, the password will be allowed, if set to true.
   *
   * @param allow true: if the API is not accessible, any password is accepted.
   *              false: Default, API must answer in time to allow the password.
   */
  public void setAllowPasswordDuringTimeout(final boolean allow)
  {
    this.allowPasswordDuringTimeout = allow;
  }

  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final String hexDigest = getHexDigest(passwordData);
    try (LineNumberReader lnr = openApiConnectionForRange(hexDigest.substring(0, PREFIX_LENGTH))) {
      return searchResponse(hexDigest, lnr);
    } catch (IOException e) {
      return new RuleResult(allowPasswordDuringTimeout,
        new RuleResultDetail(ERROR_CODE, Collections.singletonMap("count", 0)),
        new RuleResultMetadata(RuleResultMetadata.CountCategory.Pwnd, 0));
    }
  }

  private RuleResult searchResponse(final String hexDigest, final LineNumberReader lnr) throws IOException
  {
    String line;
    final Pattern p = Pattern.compile("^(" + hexDigest.substring(PREFIX_LENGTH) + "):(\\d+)\\s*$");
    while ((line = lnr.readLine()) != null) {
      final Matcher m = p.matcher(line);
      if (m.matches()) {
        final int matchCount = Integer.parseInt(m.group(2));
        return new RuleResult(!this.mandatory,
          new RuleResultDetail(ERROR_CODE, Collections.singletonMap("count", matchCount)),
          new RuleResultMetadata(RuleResultMetadata.CountCategory.Pwnd, matchCount));
      }
    }
    return new RuleResult(true);
  }

  private static String toHexString(final byte[] digest)
  {
    final StringBuilder sb = new StringBuilder(40);
    for (int i = 0; i < digest.length; i++) {
      sb.append(String.format("%02x", digest[i]));
    }
    return sb.toString();
  }

  private static String getHexDigest(final PasswordData passwordData)
  {
    final String hexDigest;
    try {
      final MessageDigest md = MessageDigest.getInstance("SHA1");
      final byte[] digest = md.digest(passwordData.getPassword().getBytes(Charset.defaultCharset()));
      hexDigest = toHexString(digest).toUpperCase();
    } catch (GeneralSecurityException e) {
      throw new IllegalStateException("SHA1 not accessible", e);
    }
    return hexDigest;
  }

  private LineNumberReader openApiConnectionForRange(final String range) throws IOException
  {
    final URL url = new URL(remoteAddress, range);
    final URLConnection c = url.openConnection();
    c.setRequestProperty("User-Agent", applicationName);
    if (timeout != null) {
      c.setReadTimeout((int) timeout.toMillis());
      c.setConnectTimeout((int) timeout.toMillis());
    }
    c.connect();

    return new LineNumberReader(
      new InputStreamReader(
        c.getInputStream(),
        StandardCharsets.UTF_8));
  }
}
