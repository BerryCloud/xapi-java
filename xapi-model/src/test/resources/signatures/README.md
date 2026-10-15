These fixtures were generated once using JJWT 0.13.0's unmodified `jjwt-jackson`
provider with Jackson 2.21.5. The JWS contains a minimal xAPI statement and an
RS512 signature. The Base64 file contains the corresponding X.509 RSA public key;
the private key was discarded. Verification with the Jackson 3 adapter must accept
the original bytes, without reserialising the payload.
