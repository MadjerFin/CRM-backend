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
        if (segmentRepo.existsByName(req.getName()))
            throw new RuntimeException("Segmento com este nome já existe");
        return segmentRepo.save(Segment.builder()
            .name(req.getName())
            .description(req.getDescription())
            .createdById(operator.getId())
            .build());
    }

    public Segment update(String id, SegmentRequest req) {
        Segment s = findById(id);
        s.setName(req.getName());
        s.setDescription(req.getDescription());
        return segmentRepo.save(s);
    }

    public void delete(String id) { segmentRepo.deleteById(id); }

    public List<Segment> findAll() { return segmentRepo.findAll(); }

    public Segment findById(String id) {
        return segmentRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));
    }
}
