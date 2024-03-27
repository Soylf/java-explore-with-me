package ru.practicum.stat.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.StatDto;
import ru.practicum.stat.mapper.EndpointHitMapper;
import ru.practicum.stat.mapper.StateMapper;
import ru.practicum.stat.model.Stat;
import ru.practicum.stat.repository.EndpointHitRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository repository;
    private final EndpointHitMapper hitMapper;
    private final StateMapper stateMapper;

    @Override
    public void addEndpointHit(EndpointHitDto endpointHitDto) {
        repository.save(hitMapper.fromEndpointHit(endpointHitDto));
    }

    @Override
    public List<StatDto> getStats(Stat stat) {
        return null;
    }
}
