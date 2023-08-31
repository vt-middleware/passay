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
import java.time.Duration;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.cryptacular.codec.HexEncoder;
import org.cryptacular.util.CodecUtil;
import org.cryptacular.util.HashUtil;

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
  private URL apiUrl;

  /** Should password be allowed if it is found in the API results. */
  private boolean allowExposed;

  /** Maximum waiting time for established connection. Default is 5 seconds. */
  private Duration connectTimeout = DEFAULT_CONNECT_TIMEOUT;

  /** Maximum waiting time for reading all data. Default is 30 seconds. */
  private Duration readTimeout = DEFAULT_READ_TIMEOUT;

  /** Should password be allowed if API calls throw exceptions. */
  private boolean allowOnException;


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
    if (appName == null) {
      throw new IllegalArgumentException("appName cannot be null");
    }
    applicationName = appName;
    if (!address.endsWith("/")) {
      throw new IllegalArgumentException("address must end with '/'");
    }
    try {
      apiUrl = new URL(address);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }


  /**
   * Whether passwords found in the API should be considered valid.
   *
   * @param allow false: the rule does not allow previously pwned passwords,
   *              true: pwned passwords are allowed, but the number of matches is returned in the result.
   */
  public void setAllowExposed(final boolean allow)
  {
    allowExposed = allow;
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


  /**
   * If an exception occurs during accessing the api, the password will be allowed, if set to true.
   *
   * @param allow true: if the API is not accessible, any password is accepted.
   *              false: Default, API must answer in time to allow the password.
   */
  public void setAllowOnException(final boolean allow)
  {
    allowOnException = allow;
  }


  @Override
  public RuleResult validate(final PasswordData passwordData)
  {
    final String hexDigest = getHexDigest(passwordData);
    try (LineNumberReader lnr = openApiConnectionForRange(hexDigest.substring(0, PREFIX_LENGTH))) {
      return searchResponse(hexDigest, lnr);
    } catch (IOException e) {
      return new RuleResult(allowOnException,
        new RuleResultDetail(IO_ERROR_CODE, Collections.singletonMap("url", apiUrl)));
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
        return new RuleResult(allowExposed,
          new RuleResultDetail(ERROR_CODE, Collections.singletonMap("count", matchCount)),
          new RuleResultMetadata(RuleResultMetadata.CountCategory.Pwned, matchCount));
      }
    }
    return new RuleResult(true);
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
