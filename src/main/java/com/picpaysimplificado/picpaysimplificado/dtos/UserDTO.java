package com.picpaysimplificado.picpaysimplificado.dtos;

import com.picpaysimplificado.picpaysimplificado.domain.user.UserType;

import javax.print.DocFlavor;
import java.math.BigDecimal;

public record UserDTO(String fistName, String lastName, String document, BigDecimal balance, String email,UserType userType, String password) {

}
