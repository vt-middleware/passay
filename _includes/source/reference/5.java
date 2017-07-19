// The historical data would be obtained from an authentication store in a
// real-world scenario. Each item consists of a label and the encoded password
// data. A common use case for labels is multiple password encodings where each
// label identifies a particular encoding.
// Salt=86ffd2e3521b5b169ec9a75678c92eed
List<PasswordData.Reference> history = Arrays.asList(
  // Password=P@ssword1
  new PasswordData.HistoricalReference(
    "SHA256",
    "j93vuQDT5ZpZ5L9FxSfeh87zznS3CM8govlLNHU8GRWG/9LjUhtbFp7Jp1Z4yS7t"),

  // Password=P@ssword2
  new PasswordData.HistoricalReference(
    "SHA256",
    "mhR+BHzcQXt2fOUWCy4f903AHA6LzNYKlSOQ7r9np02G/9LjUhtbFp7Jp1Z4yS7t"),

  // Password=P@ssword3
  new PasswordData.HistoricalReference(
    "SHA256",
    "BDr/pEo1eMmJoeP6gRKh6QMmiGAyGcddvfAHH+VJ05iG/9LjUhtbFp7Jp1Z4yS7t")
);

// Cryptacular components:
// org.cryptacular.bean.EncodingHashBean;
// org.cryptacular.spec.CodecSpec;
// org.cryptacular.spec.DigestSpec;
EncodingHashBean hasher = new EncodingHashBean(
  new CodecSpec("Base64"), // Handles base64 encoding
  new DigestSpec("SHA256"), // Digest algorithm
  1, // Number of hash rounds
  false); // Salted hash == false

List<Rule> rules = Arrays.asList(
  // ...
  // Insert other rules as needed
  // ...
  new DigestHistoryRule(hasher));

PasswordValidator validator = new PasswordValidator(rules);
PasswordData data = new PasswordData("username", "P@ssword1");
data.setPasswordReferences(history);
RuleResult result = validator.validate(data);
