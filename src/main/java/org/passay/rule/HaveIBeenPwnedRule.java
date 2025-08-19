/* See LICENSE for licensing and NOTICE for copyright. */
package org.passay.rule;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cryptacular.codec.HexEncoder;
import org.cryptacular.util.CodecUtil;
import org.cryptacular.util.HashUtil;
import org.passay.FailureRuleResult;
import org.passay.PassayUtils;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;
import org.passay.RuleResultMetadata;
import org.passay.SuccessRuleResult;

/**
 * Validates the password against the online database of <code>haveibeenpwned.com</code>
 * optionally allowing the usage of found passwords, but returns the number of found matches in
 * the metadata.
 *
 * @author Wolfgang Jung (post@wolfgang-jung.net)
 */
public class HaveIBeenPwnedRule implements Rule
{

  /** Error code for exposed passwords. */
  public static final String ERROR_CODE = "EXPOSED_HAVEIBEENPWNED";

  /** Error code for API IO errors. */
  public static final String IO_ERROR_CODE = "IO_ERROR_HAVEIBEENPWNED";

  /** URL for pwned passwords. */
  private static final String DEFAULT_URL = "https://api.pwnedpasswords.com/range/";

  /** number of chars to use from the SHA1 digest for the api call. */
  private static final int PREFIX_LENGTH = 5;

  /** Default time to wait for HTTP connect. */
  private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(5);

  /** Default time to wait for HTTP response. */
  private static final Duration DEFAULT_READ_TIMEOUT = Duration.ofSeconds(30);

  /** Name of the app. */
  private final String applicationName;

  /** URL of the API. */
  private final URL apiUrl;

  /** Should password be allowed if it is found in the API results. */
  private final boolean allowExposed;

  /** Should password be allowed if API calls throw exceptions. */
  private final boolean allowOnException;

  /** Maximum waiting time for established connection. Default is 5 seconds. */
  private Duration connectTimeout = DEFAULT_CONNECT_TIMEOUT;

  /** Maximum waiting time for reading all data. Default is 30 seconds. */
  private Duration readTimeout = DEFAULT_READ_TIMEOUT;


  /**
   * Create the rule, appName is required by the
   * <a href="https://haveibeenpwned.com/API/v3#UserAgent">API</a>.
   *
   * @param appName must not be null
   */
  public HaveIBeenPwnedRule(final String appName)
  {
    this(appName, DEFAULT_URL);
  }


  /**
   * Create the rule, appName is required by the
   * <a href="https://haveibeenpwned.com/API/v3#UserAgent">API</a>.
   *
   * @param appName must not be null
   * @param address the URL must end with a <code>/</code>.
   */
  public HaveIBeenPwnedRule(final String appName, final String address)
  {
    this(appName, address, false, false);
  }


  /**
   * Create the rule, appName is required by the
   * <a href="https://haveibeenpwned.com/API/v3#UserAgent">API</a>.
   *
   * @param appName must not be null
   * @param address the URL must end with a <code>/</code>.
   * @param allowExposed false: the rule does not allow previously pwned passwords,
   *                     true: pwned passwords are allowed, but the number of matches is returned in the result.
   * @param allowOnException true: if the API is not accessible, any password is accepted.
   *                         false: Default, API must answer in time to allow the password.
   */
  public HaveIBeenPwnedRule(
    final String appName, final String address, final boolean allowExposed, final boolean allowOnException)
  {
    this.applicationName = PassayUtils.assertNotNullArg(appName, "App name cannot be null");
    if (!address.endsWith("/")) {
      throw new IllegalArgumentException("Address must end with '/'");
    }
    try {
      this.apiUrl = new URL(address);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
    this.allowExposed = allowExposed;
    this.allowOnException = allowOnException;
  }


  /**
   * maximum Duration for connecting to the API.
   *
   * @param timeout for connecting.
   */
  public void setConnectTimeout(final Duration timeout)
  {
    connectTimeout = timeout;
  }


  /**
   * maximum Duration for reading from the API.
   *
   * @param timeout for reading.
   */
  public void setReadTimeout(final Duration timeout)
  {
    readTimeout = timeout;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    PassayUtils.assertNotNullArg(passwordData, "Password data cannot be null");
    final String hexDigest = getHexDigest(passwordData);
    try (LineNumberReader lnr = openApiConnectionForRange(hexDigest.substring(0, PREFIX_LENGTH))) {
      return searchResponse(hexDigest, lnr);
    } catch (IOException e) {
      return allowOnException ?
        new SuccessRuleResult() :
        new FailureRuleResult(new RuleResultDetail(IO_ERROR_CODE, Collections.singletonMap("url", apiUrl)));
    }
  }


  /**
   * Reads the supplied reader line by line until a match is found against the supplied hex digest.
   *
   * @param hexDigest to match
   * @param reader to read
   *
   * @return rule result whose validity is determined by whether a match was found
   *
   * @throws IOException if an error occurs reading from the reader
   */
  private RuleResult searchResponse(final String hexDigest, final LineNumberReader reader) throws IOException
  {
    String line;
    final Pattern p = Pattern.compile("^(" + hexDigest.substring(PREFIX_LENGTH) + "):(\\d+)\\s*$");
    while ((line = reader.readLine()) != null) {
      final Matcher m = p.matcher(line);
      if (m.matches()) {
        final int matchCount = Integer.parseInt(m.group(2));
        return allowExposed ?
          new SuccessRuleResult(new RuleResultMetadata(RuleResultMetadata.CountCategory.Pwned, matchCount)) :
          new FailureRuleResult(
            new RuleResultMetadata(RuleResultMetadata.CountCategory.Pwned, matchCount),
            new RuleResultDetail(ERROR_CODE, Collections.singletonMap("count", matchCount)));
      }
    }
    return new SuccessRuleResult();
  }


  /**
   * Returns an uppercase, hex encoded, SHA1 hash of the password.
   *
   * @param passwordData to hash
   *
   * @return hex encoded hash
   */
  private static String getHexDigest(final PasswordData passwordData)
  {
    final byte[] digest = HashUtil.sha1(passwordData.getPassword().getBytes(Charset.defaultCharset()));
    return CodecUtil.encode(new HexEncoder(false, true), digest);
  }


  /**
   * Opens a connection to the API and returns a reader for the input stream.
   *
   * @param range to request from the API
   *
   * @return reader for the connection
   *
   * @throws IOException if a connection cannot be opened to the API
   */
  private LineNumberReader openApiConnectionForRange(final String range)
    throws IOException
  {
    final URL url = new URL(apiUrl, range);
    final URLConnection c = url.openConnection();
    c.setRequestProperty("User-Agent", applicationName);
    if (connectTimeout != null) {
      c.setConnectTimeout((int) connectTimeout.toMillis());
    }
    if (readTimeout != null) {
      c.setReadTimeout((int) readTimeout.toMillis());
    }
    c.connect();
    return new LineNumberReader(new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8));
  }
}
