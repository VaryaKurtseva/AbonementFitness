package abonement.demo.events;

import java.time.LocalDate;

/**
 * Семейство событий, связанных с авторами.
 *
 * Аналогично ButtonEvent — sealed interface гарантирует полный перечень вариантов.
 * Десериализация по eventType, а не через Jackson-аннотации.
 */
public sealed interface UserEvent {

    /**
     * User создан. Содержит основные атрибуты нового автора.
     */
    record Created(
            Long id,
            String firstName,
            String lastName,
            String email,
            LocalDate subscriptionActivation,
            LocalDate  endOfSubscription,
            Integer visitsHall,
            String numberPhone
    ) implements UserEvent {}
    record Updated(
            String firstName,
            String lastName,
            String email,
            LocalDate subscriptionActivation,
            LocalDate  endOfSubscription,
            Integer visitsHall,
            String numberPhone
    ) implements UserEvent {}

    /**
     * User удалён. В нашей системе удаление каскадное — вместе с книгами.
     */
    record Deleted(
            Long id,
            String firstName,
            String lastName,
            int deletedButtonCounts
    ) implements UserEvent {}
    record Enriched(
            Long id,
            String firstName,
            String lastName,
            int estimatedReadingMinutes,
            String difficultyLevel,
            double recommendationScore,
            String eraClassification
    ) implements UserEvent {}
}
