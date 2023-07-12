package com.navv.Dto;

import com.navv.model.Token;
import com.navv.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Demo {

    private User user;
    private List< Token> token;
}
