/*
 * (c) Copyright 1998-2023, ANS. All rights reserved.
 */
package fr.ans.keycloak.utils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Security;
import java.util.Optional;

import javax.ws.rs.core.UriInfo;

import org.apache.http.impl.client.CloseableHttpClient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.keycloak.broker.provider.AuthenticationRequest;
import org.keycloak.broker.provider.util.IdentityBrokerState;
import org.keycloak.common.crypto.CryptoIntegration;
import org.keycloak.connections.httpclient.HttpClientProvider;
import org.keycloak.http.HttpRequest;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.services.DefaultKeycloakSession;
import org.keycloak.vault.DefaultVaultStringSecret;
import org.keycloak.vault.VaultTranscriber;

public final class KeycloakFixture {

  public static final String CLIENT_ID = "fakeClientId";
  public static final String CLIENT_SECRET = "fakeClientSecretAtLeast256bitsNeededForHS";
  public static final String REDIRECT_URI = "https://tryecps.henix.asipsante.fr/*";
  public static final String STATE_VALUE = "fakeState";
  public static final String DEFAULT_SCOPE = "openid";

  private KeycloakFixture() {
  }

  public static AuthenticationRequest givenAuthenticationRequest(KeycloakSession session) {

    var authenticationSessionModel = new MockAuthenticationSessionModel();
    var identityBrokerState = mock(IdentityBrokerState.class);

    when(identityBrokerState.getEncoded())
        .thenReturn(STATE_VALUE);

    return new AuthenticationRequest(
        session,
        mock(RealmModel.class),
        authenticationSessionModel,
        mock(HttpRequest.class),
        mock(UriInfo.class),
        identityBrokerState,
        REDIRECT_URI
    );
  }

  public static KeycloakSession givenKeycloakSession(HttpClientProvider httpClientProvider, CloseableHttpClient httpClient) {
    var session = mock(DefaultKeycloakSession.class);
    var vault = mock(VaultTranscriber.class);

    when(session.vault()).thenReturn(vault);
    when(session.getProvider(HttpClientProvider.class))
        .thenReturn(httpClientProvider);

    when(httpClientProvider.getHttpClient())
        .thenReturn(httpClient);

    when(vault.getStringSecret(anyString()))
        .thenAnswer(answer -> DefaultVaultStringSecret.forString(Optional.ofNullable(answer.getArgument(0, String.class))));

    // Add ECDSA Provider
    Security.addProvider(new BouncyCastleProvider());
    CryptoIntegration.init(ClassLoader.getSystemClassLoader());

    return session;
  }
}
