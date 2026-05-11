package com.wtc.service;

import com.wtc.dto.request.SegmentRequest;
import com.wtc.entity.Segment;
import com.wtc.entity.WtcUser;
import com.wtc.repository.SegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SegmentService {

    private final SegmentRepository segmentRepo;

    public Segment create(SegmentRequest req, WtcUser operator) {
        Segment s = Segment.builder()
            .name(req.getName())
            .description(req.getDescription())
            .createdBy(operator)
            .build();
        return segmentRepo.save(s);
    }

    public Segment update(Long id, SegmentRequest req) {
        Segment s = segmentRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));
        s.setName(req.getName());
        s.setDescription(req.getDescription());
        return segmentRepo.save(s);
    }

    public void delete(Long id) {
        segmentRepo.deleteById(id);
    }

    public List<Segment> findAll() { return segmentRepo.findAll(); }

    public Segment findById(Long id) {
        return segmentRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));
    }
}
