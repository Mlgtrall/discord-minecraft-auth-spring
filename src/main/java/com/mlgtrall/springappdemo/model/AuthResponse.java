package com.mlgtrall.springappdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class AuthResponse {
    private boolean allowed;
    private ResponseCode responseCode; // Например: "NOT_FOUND", "NOT_VERIFIED", "BANNED"

    public enum ResponseCode {
        NOT_FOUND("Account not found."),
        NOT_VERIFIED("Account not verified in Discord"),
        BANNED("Account banned!"),
        UUID_MISMATCH("This nickname belongs to another account!"),
        FIRST_LOGIN_SUCCESS("First login success! UUID linked."),
        REGISTERED_ALREADY_DISCORD("Account already registered in Discord!"),
        SERVER_ERROR("Server Error: "),
        REQUEST_PARAMS_MISSING("Request parameters missing!"),
        NAME_TAKEN("Minecraft account name already taken!"),
        REGISTER_SUCCESS("Account registered successfully!"),
        SUCCESS("Account is verified in Discord!");

        @Getter
        private String message;
        private ResponseCode() {}
        private ResponseCode(String message) {
            this.message = message;
        }

        public ResponseCode joinMessage(String messageToJoin) {
            this.message = this.message + " " + messageToJoin;
            return this;
        }
    }
}
