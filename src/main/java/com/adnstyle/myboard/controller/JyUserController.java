package com.adnstyle.myboard.controller;

import com.adnstyle.myboard.auth.PrincipalDetails;
import com.adnstyle.myboard.model.domain.JyUser;
import com.adnstyle.myboard.model.service.JyUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@SessionAttributes("jyUserSession")
@Controller
@RequiredArgsConstructor
@Slf4j
public class JyUserController {

    private final JyUserService jyUserService;

    /**
     * 회원가입화면으로 이동
     */
    @GetMapping("/joinForm")
    public String newMember() {
        return "joinForm";
    }

//    /**
//     * 회원 가입(원본)
//     */
//    @PostMapping("/join")
//    public String insertNewUser(JyUser jyUser) {
//        jyUserService.insertNewUser(jyUser);
//
//        return "/auth/loginForm";//회원가입 완료후 로그인화면으로 이동
//    }

    /**
     * 회원 가입 (사진포함)
     */
    @PostMapping("/join")
    public String insertNewUser(MultipartFile uploadFile, JyUser jyUser) {
        System.out.println("uploadFile : " + uploadFile);
        System.out.println("jyUser : " + jyUser);
        jyUserService.insertNewUser(uploadFile, jyUser);

        return "/auth/loginForm";//회원가입 완료후 로그인화면으로 이동
    }


    /**
     * 아이디 중복체크(회원가입시)
     */
    @PostMapping("/checkId")
    @ResponseBody
    public int checkId(@RequestParam(value = "userId") String userId) {
        int no = jyUserService.checkId(userId);

        return no;
    }

    /**
     * 이메일 중복체크(회원가입시)
     */
    @PostMapping("/checkEmail")
    @ResponseBody
    public int checkEmail(@RequestParam(value = "userEmail") String userEmail) {
        int no = jyUserService.checkEmail(userEmail);

        return no;

    }

    /**
     * 로그인화면으로 이동
     */
    @GetMapping("/loginForm")
    public String loginForm() {
        return "/auth/loginForm";
    }

    /**
     * 로그인 성공시이동할 화면 로그인을 성공하고 넘어가는 화면에서 세션을 생성!
     * 일반로그인, 소셜로그인 둘다! 부모클래스인 PrincipalDetails 사용했기때문
     */

    // @AuthenticationPrincipal 어노테이션을 사용하면 PrincipalDetails에서  return한 객체를 받아서 파라미터로 사용할수있다.
    @GetMapping("/user/userLogin")
    public String UserLogin(@AuthenticationPrincipal PrincipalDetails principalDetails, HttpSession session) {
        JyUser jyUserSession = principalDetails.getJyUser();
        session.setAttribute("jyUserSession", jyUserSession);

        return "jyHome";
    }

    /**
     * 로그인 실패시
     */
    @GetMapping("/failLogin")
    public String failLogin() {
        return "auth/failLoginForm";
    }

    /**
     * 아이디 비밀번호 찾기(아이디찾기)
     */
    @GetMapping("/findIdPw")
    public String findIdPw() {
        return "findIdPw";
    }

    /**
     * 입력정보와 일치하는 아이디가 있는지
     */
    @PostMapping("/findId")
    @ResponseBody
    public String findId(@RequestBody Map<String, String> jyUser) {
        String userId = jyUserService.findId(jyUser);
        if (userId != null && userId != "" && userId.length() > 0) {
            return userId;
        } else {
            return null;
        }
    }


    /**
     * 비밀번호 찾기
     */
    @GetMapping("/findPw")
    public String findPw() {
        return "findPw";
    }


//    /**
//     * 마이페이지 화면으로 이동
//     */
//    @GetMapping("/user/myPage")
//    public String myPage(@ModelAttribute("jyUserSession") JyUser jyUser) {
//        log.debug("마이페이지 세션 : " + jyUser);
//        return "myPage";
//    }


//    /**
//     * 마이페이지 화면 접속시 바로 이미지 파일뜸
//     */
//    @GetMapping("/user/myPage")
//    public ResponseEntity<Resource> myPage(@ModelAttribute("jyUserSession") JyUser jyUser) {
//        log.debug("마이페이지 세션 : " + jyUser);
//        Resource resource = new FileSystemResource(jyUser.getUploadPath() + "\\" + jyUser.getChangeName());
//
//        HttpHeaders headers = new HttpHeaders();
//
//        Path filePath = null;
//
//        try {
//            filePath = Paths.get(jyUser.getUploadPath() + "\\" + jyUser.getChangeName());
//            headers.add("Content-Type", Files.probeContentType(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
//    }
    /**
     //     * 마이페이지 화면으로 이동
     //     */
    @GetMapping("/user/myPage")
    public String myPage() {
        return "myPage";
    }

    //마이페이지 접속후 실행할 ajax
    @PostMapping("/user/myPageProfile")
    @ResponseBody
    public ResponseEntity<Resource> myPage(@ModelAttribute("jyUserSession") JyUser jyUser) {
        log.debug("마이페이지 세션 : " + jyUser);
        Resource resource = new FileSystemResource(jyUser.getUploadPath() + "\\" + jyUser.getChangeName());

        HttpHeaders headers = new HttpHeaders();

        Path filePath = null;

        try {
            filePath = Paths.get(jyUser.getUploadPath() + "\\" + jyUser.getChangeName());
            headers.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
        ResponseEntity responseEntity = new ResponseEntity<>(resource, headers, HttpStatus.OK);

        log.info("responseEntity는? "+responseEntity);
        return responseEntity;
    }

}