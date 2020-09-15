package ru.akaleganov.weather.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.akaleganov.weather.model.Weather;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final Map<Integer, Weather> weathers = new ConcurrentHashMap<>();
    {
        weathers.put(1, new Weather(1, "Msc", 20));
        weathers.put(2, new Weather(2, "SPb", 15));
        weathers.put(3, new Weather(3, "Bryansk", 15));
        weathers.put(4, new Weather(4, "Smolensk", 15));
        weathers.put(5, new Weather(5, "Kiev", 15));
        weathers.put(6, new Weather(6, "Minsk", 15));
    }

    public Mono<Weather> findById(Integer id) {
        return Mono.justOrEmpty(weathers.get(id));
    }
    public Flux<Weather> hottest() {
        int avg = (int)this.weathers.values().stream()
                .mapToInt(Weather::getTemperature).summaryStatistics().getAverage();
        return Flux.fromIterable(this.weathers.values().stream().filter(e-> e.getTemperature() == avg).collect(Collectors.toList()));
    }

    public Flux<Weather> all() {
        return Flux.fromIterable(weathers.values());
    }

    public Mono<Weather> cityGreatThen(Integer t) {
        return Mono.justOrEmpty(weathers.values().stream().filter(e->e.getTemperature() == t).findFirst().orElse(null));
    }
}
