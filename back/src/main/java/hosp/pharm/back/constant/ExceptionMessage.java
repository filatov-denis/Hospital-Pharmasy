package hosp.pharm.back.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {

    EXPIRED_TOKEN("Токен истёк"),
    INVALID_CREDENTIALS("Неправильный логин или пароль"),
    INVALID_TOKEN("Неправильный токен"),
    MISSING_TOKEN("Токен не найден"),
    NULL_IDENTIFIER("Отсутствует идентификатор"),
    ENTITY_NOT_FOUND("Сущность не найдена"),
    USER_ALREADY_EXIST("Пользователь с таким именем уже существует"),
    UNAVAILABLE_BATCH("Невозможно создать данный запрос с выбранной партией"),
    UNAVAILABLE_STATUS_EXCEPTION("Такой статус сейчас недоступен"),
//    WRONG_PHONE_DATA_LENGTH("Email length should be equals or less than 200 symbols"),
//    NEGATIVE_TRANSFER("Transfer sum should be a positive number"),
    UNEXPECTED_ERROR("Произошла непредвиденная ошибка");

    private String value;

}