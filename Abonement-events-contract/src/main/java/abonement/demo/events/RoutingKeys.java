package abonement.demo.events;

/**
 * Константы для маршрутизации событий в RabbitMQ.
 *
 * Routing key в topic exchange работает как почтовый индекс:
 * - "button.created" — конкретное событие
 * - "button.*"       — все события книг
 * - "#"            — все события вообще
 *
 * Вынесены в контракт, чтобы publisher и consumer использовали одни и те же строки.
 * Рассогласование routing key - частая ошибка, которую трудно отследить.
 */
public final class RoutingKeys {

    private RoutingKeys() {
        // утилитарный класс — экземпляры не создаём
    }

    // Имя общего topic exchange для доменных событий
    public static final String EXCHANGE = "button.events";

    // Routing keys для событий button
    public static final String BUTTON_CREATED = "button.created";
    public static final String BUTTON_UPDATED = "button.updated";
    public static final String BUTTON_DELETED = "button.deleted";
    public static final String BUTTON_ENRICHED = "button.enriched";

    // Routing keys для событий user
    public static final String USER_CREATED = "user.created";
    public static final String USER_UPDATED = "user.updated";
    public static final String USER_DELETED = "user.deleted";
    public static final String USER_ENRICHED = "user.enriched";

    // Паттерны для подписки (wildcard)
    public static final String ALL_BUTTON_EVENTS = "button.*";
    public static final String ALL_USER_EVENTS = "user.*";
    public static final String ALL_EVENTS = "#";
}
