package ru.akaleganov.weather.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.akaleganov.weather.model.Weather;
import ru.akaleganov.weather.services.WeatherService;

import java.time.Duration;

@RestController
public class WeatherControl {
    @Autowired
    private final WeatherService weathers;

    public WeatherControl(WeatherService weathers) {
        this.weathers = weathers;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        Flux<Weather> data = weathers.all();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weathers.findById(id);
    }
   @GetMapping(value = "/cityGreatThen/{t}")
    public Mono<Weather> cityGreatThen(@PathVariable Integer t) {
        return weathers.cityGreatThen(t);
    }

    /**
     * вернёт среднюю
     */
    @GetMapping(value = "/hottest", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> hottest() {
        Flux<Weather> data =  weathers.hottest();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }
}