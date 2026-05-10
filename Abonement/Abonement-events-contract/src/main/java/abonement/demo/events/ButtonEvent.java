package abonement.demo.events;

import java.time.LocalDate;

/**
 * Семейство событий, связанных с книгами.
 *
 * Sealed interface ограничивает набор наследников — компилятор гарантирует,
 * что BookEvent может быть только Created, Updated или Deleted.
 * Это делает switch/pattern matching исчерпывающим, нет риска забыть обработать какой-то тип события.:
 *
 *   switch (event) {
 *       case ButtonEvent.Created c -> ...
 *       case ButtonEvent.Updated u -> ...
 *       case ButtonEvent.Deleted d -> ...
 *       // компилятор знает, что других вариантов нет
 *   }
 *
 * Полиморфная десериализация выполняется не через Jackson-аннотации,
 * а через поле eventType в EventMetadata — consumer определяет конкретный тип
 * по routing key и десериализует payload в нужный record напрямую.
 */
public sealed interface ButtonEvent {

    /**
     * Button создана. Содержит все ключевые атрибуты новой книги.
     */
    record Created(
            Long userId,
            Long requestId,
            String rejectionReason,
            String  process,
            Integer value,
            Integer visitsHall,
            LocalDate subscriptionActivation,
            LocalDate endOfSubscription


    ) implements ButtonEvent {}

    /**
     * Button обновлена. Содержит актуальное состояние после обновления.
     * В промышленных системах здесь могут быть и старые значения (before/after),
     * но для демонстрации достаточно нового состояния.
     */
    record Updated(
            Integer value,
            Integer visitsHall,
            LocalDate subscriptionActivation,
            LocalDate endOfSubscription
    ) implements ButtonEvent {}

    /**
     * Button удалена. Достаточно идентификатора — после удаления данных нет.
     */
    record Deleted(
            Long userId,
            Long requestId
    ) implements ButtonEvent {}

    /**
     * Button обогащена аналитикой — результат gRPC-вызова к analytics-серверу.
     *
     * Событие публикуется grpc-enrichment-client после получения ответа
     * от grpc-analytics-server. Содержит вычисленные метрики книги.
     */
    record Enriched(
            Long userId,
            Long requestId,
            int estimatedReadingMinutes,
            String difficultyLevel,
            double recommendationScore,
            String eraClassification
    ) implements ButtonEvent {}
}
