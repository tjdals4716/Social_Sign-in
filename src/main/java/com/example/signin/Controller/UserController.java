package com.example.signin.Controller;

import com.example.signin.DTO.JWTDTO;
import com.example.signin.DTO.OAuth2CodeDTO;
import com.example.signin.DTO.UserDTO;
import com.example.signin.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원 가입
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<JWTDTO> login(@RequestBody UserDTO userDTO) {
        JWTDTO login = userService.login(userDTO.getUid(), userDTO.getPassword());
        return ResponseEntity.ok(login);
    }

    //유저 조회
    @GetMapping("/{uid}")
    public ResponseEntity<UserDTO> getUserByUid(@PathVariable String uid) {
        UserDTO user = userService.getUserByUid(uid);
        return ResponseEntity.ok(user);
    }

    //아이디 중복 확인
    @GetMapping("/check-uid")
    public ResponseEntity<Boolean> isUidDuplicate(@RequestBody String uid) {
        boolean isDuplicate = userService.isUidDuplicate(uid);
        return ResponseEntity.ok(isDuplicate);
    }

    //닉네임 중복 확인
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> isNicknameDuplicate(@RequestBody String nickname) {
        boolean isDuplicate = userService.isNicknameDuplicate(nickname);
        return ResponseEntity.ok(isDuplicate);
    }

    //회원 정보 수정
    @PutMapping("/{uid}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String uid, @RequestBody UserDTO userDTO, @AuthenticationPrincipal UserDetails userDetails) {
        UserDTO updatedUser = userService.updateUser(uid, userDTO, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    //회원 탈퇴
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uid, @AuthenticationPrincipal UserDetails userDetails) {
        userService.deleteUser(uid, userDetails);
        return ResponseEntity.noContent().build();
    }

    //토큰 유효 시간 확인
    @GetMapping("/token-remaining-time")
    public ResponseEntity<Long> getTokenRemainingTime(@AuthenticationPrincipal UserDetails userDetails) {
        Long remainingTime = userService.getTokenRemainingTime(userDetails);
        return ResponseEntity.ok(remainingTime);
    }

    //토큰 연장(오류나는 중)
    @PostMapping("/extend-token")
    public ResponseEntity<Long> refreshToken(@AuthenticationPrincipal UserDetails userDetails) {
        Long remainingTime = userService.refreshToken(userDetails);
        return ResponseEntity.ok(remainingTime);
    }

    //유저 토큰 정보 조회
    @GetMapping("/token/{uid}")
    public ResponseEntity<JWTDTO> getUserWithTokenInfo(@PathVariable String uid, @RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        JWTDTO userWithTokenInfo = userService.getUserWithTokenInfo(uid, token);
        return ResponseEntity.ok(userWithTokenInfo);
    }

    //닉네임 수정
    @PutMapping("/nickname/{uid}")
    public ResponseEntity<UserDTO> updateNickname(@PathVariable String uid, @RequestBody String nickname) {
        UserDTO updatedUser = userService.updateNickname(uid, nickname);
        return ResponseEntity.ok(updatedUser);
    }

    //카카오 유저 닉네임 설정
    @PostMapping("/nickname/{uid}")
    public ResponseEntity<UserDTO> updateNickname(@PathVariable String uid, @RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        UserDTO updatedUser = userService.updateNickname(uid, nickname);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    //카카오 로그인 성공 시 호출되는 엔드포인트 (GET)
    @GetMapping("/oauth2/code/kakao")
    public ResponseEntity<JWTDTO> kakaoCallback(@RequestParam String code) {
        JWTDTO jwtDto = userService.loginWithOAuth2(code);
        return ResponseEntity.ok(jwtDto);
    }

    //카카오 로그인 성공 시 호출되는 엔드포인트 (POST)
    @PostMapping("/oauth2/code/kakao")
    public ResponseEntity<JWTDTO> kakaoLoginPost(@RequestBody OAuth2CodeDTO codeDTO) {
        JWTDTO jwtDto = userService.loginWithOAuth2(codeDTO.getCode());
        return ResponseEntity.ok(jwtDto);
    }

    //네이버 로그인 성공 시 호출되는 엔드포인트 (GET)
    @GetMapping("/oauth2/code/naver")
    public ResponseEntity<JWTDTO> naverCallback(@RequestParam String code) {
        JWTDTO jwtDto = userService.loginWithNaverOAuth2(code);
        return ResponseEntity.ok(jwtDto);
    }

    //네이버 로그인 성공 시 호출되는 엔드포인트 (POST)
    @PostMapping("/oauth2/code/naver")
    public ResponseEntity<JWTDTO> naverLoginPost(@RequestBody OAuth2CodeDTO codeDTO) {
        JWTDTO jwtDto = userService.loginWithNaverOAuth2(codeDTO.getCode());
        return ResponseEntity.ok(jwtDto);
    }

    //구글 로그인 성공 시 호출되는 엔드포인트 (GET)
    @GetMapping("/oauth2/code/google")
    public ResponseEntity<JWTDTO> googleCallback(@RequestParam String code) {
        JWTDTO jwtDto = userService.loginWithGoogleOAuth2(code);
        return ResponseEntity.ok(jwtDto);
    }

    //구글 로그인 성공 시 호출되는 엔드포인트 (POST)
    @PostMapping("/oauth2/code/google")
    public ResponseEntity<JWTDTO> googleLoginPost(@RequestBody OAuth2CodeDTO codeDTO) {
        JWTDTO jwtDto = userService.loginWithGoogleOAuth2(codeDTO.getCode());
        return ResponseEntity.ok(jwtDto);
    }

    //페이스북 로그인 성공 시 호출되는 엔드포인트 (GET)
    @GetMapping("/oauth2/code/facebook")
    public ResponseEntity<JWTDTO> facebookCallback(@RequestParam String code) {
        JWTDTO jwtDto = userService.loginWithFacebookOAuth2(code);
        return ResponseEntity.ok(jwtDto);
    }

    //페이스북 로그인 성공 시 호출되는 엔드포인트 (POST)
    @PostMapping("/oauth2/code/facebook")
    public ResponseEntity<JWTDTO> facebookLoginPost(@RequestBody OAuth2CodeDTO codeDTO) {
        JWTDTO jwtDto = userService.loginWithFacebookOAuth2(codeDTO.getCode());
        return ResponseEntity.ok(jwtDto);
    }

    //깃허브 로그인 성공 시 호출되는 엔드포인트 (GET)
    @GetMapping("/oauth2/code/github")
    public ResponseEntity<JWTDTO> githubCallback(@RequestParam String code) {
        JWTDTO jwtDto = userService.loginWithGithubOAuth2(code);
        return ResponseEntity.ok(jwtDto);
    }

    //깃허브 로그인 성공 시 호출되는 엔드포인트 (POST)
    @PostMapping("/oauth2/code/github")
    public ResponseEntity<JWTDTO> githubLoginPost(@RequestBody OAuth2CodeDTO codeDTO) {
        JWTDTO jwtDto = userService.loginWithGithubOAuth2(codeDTO.getCode());
        return ResponseEntity.ok(jwtDto);
    }
}

