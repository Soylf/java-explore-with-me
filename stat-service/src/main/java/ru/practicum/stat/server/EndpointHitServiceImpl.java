package ru.practicum.stat.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.stat.dto.EndpointHitDto;
import ru.practicum.stat.dto.StatDto;
import ru.practicum.stat.mapper.HitMapper;
import ru.practicum.stat.mapper.Mapper;
import ru.practicum.stat.model.EndpointHit;
import ru.practicum.stat.model.Stat;
import ru.practicum.stat.repository.EndpointHitRepository;
import ru.practicum.stat.util.DateTimeFormatterCustom;

import javax.transaction.Transactional;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository repository;
    private final DateTimeFormatterCustom formatterCustom;
    private final HitMapper mapper; //много пунктов, подели на классы и используй наследования


    @Override
    public void addEndpointHit(EndpointHitDto endpointHitDto) {
        repository.save(mapper.fromEndpointHit(endpointHitDto));
    }

    @Override
    public List<StatDto> getStats(Stat stat) {
        List<EndpointHit> endpointHits;
        List<StatDto> newStats;
        stat.setStart(setDateDecoder(stat.getStart()));
        stat.setEnd(setDateDecoder(stat.getEnd()));

        LocalDateTime start = formatterCustom.StringToFormatDate(stat.getStart());
        LocalDateTime end = formatterCustom.StringToFormatDate(stat.getEnd());


        if (!stat.isUnique() && stat.getUris() == null) {
            endpointHits = repository.findAllByTimestampBetweenAndUriIn(start, end,
                    List.of(stat.getUris()));
        } else if (stat.isUnique()) {
            if (stat.getUris() != null) {
                endpointHits = repository.findAllByUniqueIp(start,end);
            } else {
                endpointHits = repository.findAllByUrisAndUniqueIp(start,end,List.of(stat.getUris()));
            }
        } else {
            endpointHits = repository.findAllByTimestampBetween(start,end);
        }
        List<String> uris = mapper.StringToUris(endpointHits);


        return newStats;
    }

    private String setDateDecoder(String date) {
        return URLDecoder.decode(date, StandardCharsets.UTF_8);
    }
}
