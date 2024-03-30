package ru.practicum.stat.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.StatDto;
import ru.practicum.stat.error.NoValidParameter;
import ru.practicum.stat.mapper.HitMapper;
import ru.practicum.stat.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository repository;
    private final HitMapper mapper;

    @Override
    public void addEndpointHit(EndpointHitDto endpointHitDto) {
        repository.save(mapper.fromEndpointHit(endpointHitDto)); // Сохранение данных о запросе после их преобразования
    }

    @Override
    public List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        checkEndIsAfterStart(start, end);

        if (uris == null || uris.isEmpty()) {
            return repository.findAllWithoutUris(start, end); // Получение статистики без учета URI
        }
        if (unique) {
            return repository.findAllUnique(start, end, uris); // Получение уникальной статистики по запросам с учетом URI
        }
        return repository.findAllNotUnique(start, end, uris); // Получение статистики по запросам без учета уникальности и с указанными URI
    }

    private void checkEndIsAfterStart(LocalDateTime start, LocalDateTime end) { // Метод проверки корректности даты окончания
        if (!end.isAfter(start)) {
            throw new NoValidParameter("Что пошло не так");
        }
    }
}