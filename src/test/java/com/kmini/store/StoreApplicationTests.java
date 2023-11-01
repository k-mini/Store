package com.kmini.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmini.store.domain.User;
import com.kmini.store.domain.type.UserRole;
import com.kmini.store.domain.type.UserStatus;
import com.kmini.store.dto.SignupDto;
import com.kmini.store.repository.UserRepository;
import com.kmini.store.service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StoreApplicationTests {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	AuthService authService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ObjectMapper objectMapper;

	@AfterEach
	public

	// 회원가입 테스트
	@Test
	void signup() {
		// given
		SignupDto signupDto = new SignupDto("kmini", "1234", "kmini@gmail.com", null);

		// when
		authService.signup(signupDto);

		// then  (아이디가 있어야 성공)
		User user = userRepository.findById(1L).orElseThrow(() -> new IllegalStateException("아이디가 없습니다."));
		Assertions.assertThat(user.getUsername()).isEqualTo(signupDto.getUsername());
		Assertions.assertThat(user.getPassword()).isEqualTo(signupDto.getPassword());
		Assertions.assertThat(user.getEmail()).isEqualTo(signupDto.getEmail());
		Assertions.assertThat(user.getUserStatus()).isEqualTo(UserStatus.SIGNUP);
		Assertions.assertThat(user.getRole()).isEqualTo(UserRole.USER);
	}

}
