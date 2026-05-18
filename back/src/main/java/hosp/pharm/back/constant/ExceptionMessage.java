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
    NOT_ENOUGH_PRODUCT("Недостаточное количества продукта для выполнения операции"),
    NOT_ALLOWED_ROLE("Эта роль надоступна"),
    NOT_ALLOWED_FOR_USER("Операция недоступна для этого пользователя"),
    WRONG_STORAGE_TYPE("Неправильный тип склада"),
    WRONG_BATCH("Неправильный тип партии для запроса"),
    BATCH_ALREADY_EXPIRED("Партия уже имеет истёкший срок годности"),
    UNEXPECTED_ERROR("Произошла непредвиденная ошибка");

    private String value;

}