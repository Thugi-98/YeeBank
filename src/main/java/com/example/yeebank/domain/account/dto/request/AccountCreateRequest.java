package com.example.yeebank.domain.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequest {

    @NotBlank(message = "계좌 별칭은 필수입니다")
    @Size(max = 50, message = "별칭은 50자 이하여야 합니다")
    private String alias;

    @NotNull(message = "비밀번호는 필수입니다")
    private String password;
}
