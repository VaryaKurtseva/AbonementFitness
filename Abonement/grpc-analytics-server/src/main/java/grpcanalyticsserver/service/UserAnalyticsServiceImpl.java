package grpcanalyticsserver.service;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import abonement.demo.grpc.AnalyzeUserRequest;
import abonement.demo.grpc.UserAnalysisResponse;
import abonement.demo.grpc.UserAnalyticsGrpc;
/**
 * Реализация gRPC-сервиса UserAnalytics.
 *
 * Наследует сгенерированный базовый класс BookAnalyticsImplBase —
 * аналог того, как REST-контроллер реализует интерфейс контракта:
 *
 *   REST:    AuthorController implements AuthorApi
 *   GraphQL: BookDataFetcher с @DgsQuery
 *   gRPC:    BookAnalyticsServiceImpl extends BookAnalyticsGrpc.BookAnalyticsImplBase
 *
 * Ключевые отличия от REST/GraphQL:
 * - Бинарный протокол (protobuf) вместо JSON — компактнее и быстрее
 * - Строго типизированный контракт (.proto) — несовместимость обнаруживается при компиляции
 * - HTTP/2 с мультиплексированием — несколько запросов в одном TCP-соединении
 * - Поддержка streaming (server, client, bidirectional) — здесь используем unary (простой запрос-ответ)
 */
public class UserAnalyticsServiceImpl extends UserAnalyticsGrpc.UserAnalyticsImplBase {
    private static final Logger log = LoggerFactory.getLogger(UserAnalyticsServiceImpl.class);
    /**
     * Обрабатывает запрос на анализ user.
     *
     * Паттерн gRPC: метод получает request и StreamObserver для ответа.
     * StreamObserver — это callback-интерфейс:
     *   - onNext(response) — отправить ответ (для unary RPC вызывается один раз)
     *   - onCompleted()    — завершить RPC
     *   - onError(t)       — сообщить об ошибке
     *
     * Для unary RPC (один запрос → один ответ) всегда:
     *   responseObserver.onNext(response);
     *   responseObserver.onCompleted();
     */
    @Override
    public void analyzeUser(AnalyzeUserRequest request,
                            StreamObserver<UserAnalysisResponse> responseObserver) {

        log.info("gRPC запрос: анализ user id={} «{} {}» (email: {}, visitsHall: {})",
                request.getUserId(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getVisitsHall());

        // Вычисление метрик (демонстрационная логика)
        int activityMinutes = estimateActivityTime(request.getVisitsHall());
        String fitnessLevel = classifyFitnessLevel(request.getVisitsHall());
        double score = calculateRecommendationScore(request.getVisitsHall());
        String subscriptionTier = classifySubscriptionTier(request.getVisitsHall());

        // Формируем ответ
        UserAnalysisResponse response = UserAnalysisResponse.newBuilder()
                .setUserId(request.getUserId())
                .setEstimatedActivityMinutes(activityMinutes)
                .setFitnessLevel(fitnessLevel)
                .setRecommendationScore(score)
                .setSubscriptionTier(subscriptionTier)
                .build();

        log.info("gRPC ответ: user id={}, активность={}мин, уровень={}, балл={}, категория={}",
                response.getUserId(), activityMinutes, fitnessLevel, score, subscriptionTier);
        // Отправляем ответ клиенту и завершаем RPC
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }



    private int estimateActivityTime(Integer visitsHall) {
        if (visitsHall <= 0) return 0;
        return visitsHall * 60;  // каждый поход = 60 минут активности
    }
    /**
     * Классификация усилий спортсмена на по количеству посещений
     * BEGINNER, INTERMEDIATE, ADVANCED
     */
    private String classifyFitnessLevel(int visitsHall) {
        if (visitsHall < 10) return "BEGINNER";
        if (visitsHall < 20) return "INTERMEDIATE";
        if (visitsHall < 30) return "ADVANCED";
        return "ELITE";
    }
    /**
     * Рекомендательный балл (0.1—10.0).
     * Демонстрационная формула: большое количество посещений получает высокий балл.
     */
    private double calculateRecommendationScore(int visitsHall) {
        double base = 7.0;
        if (visitsHall >= 40) base += 1.5;
        if (visitsHall >= 30 && visitsHall < 39) base += 0.5;
        if (visitsHall >= 20 && visitsHall < 30) base += 1.0;
        if (visitsHall >= 10 && visitsHall < 20) base += 0.5;
        if (visitsHall >= 1 && visitsHall < 10) base += 0.1;
        return Math.min(base, 10.0);
    }
    /**
     * Классификация эпохи по количеству посещений.
     * GOLD, SILVER, BRONZE
     */
    private String classifySubscriptionTier(int visitsHall) {
        if (visitsHall <= 0) return "UNKNOWN";
        if (visitsHall >= 10) return "BRONZE";
        if (visitsHall >= 25) return "SILVER";
        if (visitsHall >= 35) return "GOLD";
        return "PLATINUM";
    }


}
