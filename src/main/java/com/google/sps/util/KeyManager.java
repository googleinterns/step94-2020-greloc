package com.google.sps.util;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import java.io.IOException;

public final class KeyManager {

  private static final String PROJECT_ID = "greloc";
  private static final String VERSION = "1";

  /**
   * Gets the value for the specified `secretId` from Google Secret Manager
   *
   * @param secretId: The ID of the key or secret stored in Google Secret Manager
   *     (https://console.cloud.google.com/security/secret-manager)
   * @return String with the value of `secretId`
   */
  public static String accessSecretVersion(String secretId) throws IOException {
    try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
      SecretVersionName secretVersionName = SecretVersionName.of(PROJECT_ID, secretId, VERSION);
      AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
      return response.getPayload().getData().toStringUtf8();
    }
  }
}
