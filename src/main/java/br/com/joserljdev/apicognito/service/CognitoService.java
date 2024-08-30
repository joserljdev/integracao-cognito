package br.com.joserljdev.apicognito.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.joserljdev.apicognito.dto.ConfirmationRequest;
import br.com.joserljdev.apicognito.dto.LoginRequest;
import br.com.joserljdev.apicognito.dto.ResetPasswordRequest;
import br.com.joserljdev.apicognito.dto.UpdateEmailRequest;
import br.com.joserljdev.apicognito.dto.UserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserPasswordResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.VerifyUserAttributeRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.VerifyUserAttributeResponse;

@Service
public class CognitoService {
	
	@Autowired
	private CognitoIdentityProviderClient cognitoClient;

    public SignUpResponse criarUsuario(String clientId, UserRequest userRequest) {
    	SignUpRequest signUpRequest = SignUpRequest.builder()
    			  .clientId(clientId)
    			  .username(userRequest.getUsername())
    			  .password(userRequest.getPassword())
    			  .userAttributes(
    					  AttributeType.builder().name("email").value(userRequest.getEmail()).build(),
    					  AttributeType.builder().name("name").value(userRequest.getName()).build()
    			  )
    			  .build();
    	SignUpResponse signUpResponse = cognitoClient.signUp(signUpRequest);
    	
    	return signUpResponse;
        
    }
    
    public AdminConfirmSignUpResponse confirmarUsuarioSemCodigo(String userPoolId, String username) {
    	AdminConfirmSignUpRequest adminConfirmSignUpRequest = AdminConfirmSignUpRequest.builder()
    			  .userPoolId(userPoolId)
    			  .username(username)
    			  .build();

        return cognitoClient.adminConfirmSignUp(adminConfirmSignUpRequest);
    }
    
    public ConfirmSignUpResponse confirmarUsuarioComCodigo(String clientId, ConfirmationRequest confirmationRequest) {
        ConfirmSignUpRequest confirmSignUpRequest = ConfirmSignUpRequest.builder()
                .clientId(clientId)
                .username(confirmationRequest.getUsername())
                .confirmationCode(confirmationRequest.getConfirmationCode())
                .build();

        return cognitoClient.confirmSignUp(confirmSignUpRequest);
    }
    
    public AdminSetUserPasswordResponse alterarSenhaUsuario(String userPoolId, ResetPasswordRequest resetPasswordRequest) {
        AdminSetUserPasswordRequest adminSetUserPasswordRequest = AdminSetUserPasswordRequest.builder()
                .userPoolId(userPoolId)
                .username(resetPasswordRequest.getUsername())
                .password(resetPasswordRequest.getNewPassword())
                .permanent(true)
                .build();

        return cognitoClient.adminSetUserPassword(adminSetUserPasswordRequest);
    }
    
    public InitiateAuthResponse login(String clientId, LoginRequest loginRequest) {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", loginRequest.getUsername());
        authParams.put("PASSWORD", loginRequest.getPassword());

        InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                .clientId(clientId)
                .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .authParameters(authParams)
                .build();

        return cognitoClient.initiateAuth(authRequest);
    }
    
    public AdminUpdateUserAttributesResponse alterarEmail(String userPoolId, UpdateEmailRequest updateEmailRequest) {
        AdminUpdateUserAttributesRequest request = AdminUpdateUserAttributesRequest.builder()
                .userPoolId(userPoolId)
                .username(updateEmailRequest.getUsername())
                .userAttributes(
                		AttributeType.builder().name("email").value(updateEmailRequest.getNewEmail()).build(), 
                        AttributeType.builder().name("email_verified").value("true").build()
                 )
                .build();

        return cognitoClient.adminUpdateUserAttributes(request);
    }
    
    public void confirmarEmailSemCodigo(String userPoolId, String username) {
        AdminUpdateUserAttributesRequest request = AdminUpdateUserAttributesRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .userAttributes(
                    AttributeType.builder()
                        .name("email_verified")
                        .value("true")
                        .build()
                )
                .build();

        cognitoClient.adminUpdateUserAttributes(request);
    }
    
    public VerifyUserAttributeResponse confirmarCodigoEmail(String accessToken, String codigoVerificacao) {
        VerifyUserAttributeRequest request = VerifyUserAttributeRequest.builder()
                .accessToken(accessToken)
                .attributeName("email")
                .code(codigoVerificacao)
                .build();

        return cognitoClient.verifyUserAttribute(request);
    }
}