package br.com.joserljdev.apicognito.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.joserljdev.apicognito.dto.ConfirmationRequest;
import br.com.joserljdev.apicognito.dto.LoginRequest;
import br.com.joserljdev.apicognito.dto.ResetPasswordRequest;
import br.com.joserljdev.apicognito.dto.UpdateEmailRequest;
import br.com.joserljdev.apicognito.dto.UserRequest;
import br.com.joserljdev.apicognito.service.CognitoService;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;

@RestController
@RequestMapping("/api/usuario")
public class UserController {

	@Value("${aws.cognito.clientId}")
	private String clientId;
	
	@Value("${aws.cognito.userPoolId}")
    private String userPoolId;

	@Autowired
	private CognitoService cognitoService;

	@PostMapping
	public ResponseEntity<String> criarUsuario(@RequestBody UserRequest userRequest) {
		try {
			cognitoService.criarUsuario(clientId, userRequest);
			return ResponseEntity.ok("Usuário criado com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Erro ao criar usuário: " + e.getMessage());
		}
	}
		
	@PostMapping("/confirmar-sem-codigo")
	public ResponseEntity<String> confirmarUsuarioSemCodigo(@RequestParam String username) {
		try {
			cognitoService.confirmarUsuarioSemCodigo(userPoolId, username);
			return ResponseEntity.ok("Usuário confirmado com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Erro ao confirmar usuário: " + e.getMessage());
		}
	}
	
	@PostMapping("/confirmar-com-codigo")
    public ResponseEntity<String> confirmarUsuarioComCodigo(@RequestBody ConfirmationRequest confirmationRequest) {
        try {
        	cognitoService.confirmarUsuarioComCodigo(clientId, confirmationRequest);
            return ResponseEntity.ok("Usuário confirmado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao confirmar usuário: " + e.getMessage());
        }
    }
		
   
	@PostMapping("/alterar-senha")
    public ResponseEntity<String> alterarSenhaUsuario(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            cognitoService.alterarSenhaUsuario(userPoolId, resetPasswordRequest);
            return ResponseEntity.ok("Senha alterada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao alterar senha: " + e.getMessage());
        }
    }
	
	@PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            InitiateAuthResponse response = cognitoService.login(clientId, loginRequest);
            return ResponseEntity.ok(response.authenticationResult().idToken());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao autenticar usuário: " + e.getMessage());
        }
    }
	
	@PutMapping("/alterar-email")
    public ResponseEntity<String> alterarEmail(@RequestBody UpdateEmailRequest updateEmailRequest) {
        try {
            cognitoService.alterarEmail(userPoolId, updateEmailRequest);
            return ResponseEntity.ok("E-mail do usuário alterado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao alterar e-mail do usuário: " + e.getMessage());
        }
    }
	
	@PostMapping("/confirmar-email-sem-codigo")
    public ResponseEntity<String> confirmarEmailSemCodigo(@RequestParam String username) {
        try {
            cognitoService.confirmarEmailSemCodigo(userPoolId, username);
            return ResponseEntity.ok("E-mail confirmado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao confirmar e-mail: " + e.getMessage());
        }
    }
	
	@PostMapping("/confirmar-email-com-codigo")
	public ResponseEntity<String> confirmarEmail(@RequestParam String accessToken, @RequestParam String codigoVerificacao) {
	    try {
	        cognitoService.confirmarCodigoEmail(accessToken, codigoVerificacao);
	        return ResponseEntity.ok("E-mail confirmado com sucesso!");
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Erro ao confirmar e-mail: " + e.getMessage());
	    }
	}
}