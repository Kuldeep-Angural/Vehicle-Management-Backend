package com.navv.controller;

import com.navv.Dto.EmailDto;
import com.navv.model.User;
import com.navv.securityconfig.EmailSender;
import com.navv.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class EmailController {


    private final EmailSender emailSender;
    private final UserService userService;

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestBody EmailDto emailDto) throws MessagingException {
        String subject="Request for Password Reset ";
        User user=userService.findByEmail(emailDto.getTo()).get();
        String username=user.getFirstname();



        String body="Hi,"+username+" Please find Attached the link To generate a new Password  http://localhost:3000/";


        System.out.println(username);

        emailSender.sendmail(emailDto.getTo(),body+emailDto.getTo(),subject);
        return ResponseEntity.ok("email Sent Successfully");
    }



}
