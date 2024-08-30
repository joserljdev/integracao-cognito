package br.com.joserljdev.apicognito;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
public class AwsCognitoConfig {
	@Value("${aws.accessKey}")
	private String accesKey;
	
	@Value("${aws.secretKey}")
	private String secretKey;

	@Bean
	public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
		CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder().region(Region.US_EAST_1)
				.credentialsProvider(() -> AwsBasicCredentials.create(accesKey, secretKey)).build();
		return cognitoClient;
	}
}
