package hosp.pharm.back.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {

    EXPIRED_TOKEN("Your token has expired"),
    INVALID_CREDENTIALS("Wrong credentials"),
    INVALID_TOKEN("Wrong token"),
    MISSING_TOKEN("Token not found"),
//    INVALID_RECEIVER("Can't found receiver with current ID"),
//    NEGATIVE_BALANCE("Transfer sum should be lesser than the balance of sender"),
//    WRONG_EMAIL_DATA_LENGTH("Phone length should be equals or less than 13 symbols"),
//    WRONG_PHONE_DATA_LENGTH("Email length should be equals or less than 200 symbols"),
//    NEGATIVE_TRANSFER("Transfer sum should be a positive number"),
    UNEXPECTED_ERROR("An unexpected error occurred");

    private String value;

}