package com.app.medical_support.common.integration.claims.service;

import com.app.medical_support.common.integration.claims.dto.ClaimsItemRequest;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClaimsStagingService {

    private final Map<Long, VisitClaimsStage> stages = new ConcurrentHashMap<>();

    public void stageItem(Long visitId, Long patientId, ClaimsItemRequest item) {
        if (visitId == null || visitId <= 0 || patientId == null || patientId <= 0 || item == null) {
            return;
        }
        VisitClaimsStage stage = stages.computeIfAbsent(visitId, key -> new VisitClaimsStage(visitId, patientId));
        stage.setPatientId(patientId);
        stage.upsert(item);
    }

    public VisitClaimsStage snapshot(Long visitId) {
        VisitClaimsStage stage = stages.get(visitId);
        return stage == null ? null : stage.copy();
    }

    public void markDispatched(Long visitId, String eventId) {
        VisitClaimsStage stage = stages.get(visitId);
        if (stage == null) {
            return;
        }
        stage.setDispatched(true);
        stage.setLastEventId(eventId);
        stage.setDispatchedAt(LocalDateTime.now());
    }

    @Getter
    public static class VisitClaimsStage {
        private final Long visitId;
        private Long patientId;
        private final List<ClaimsItemRequest> items = new ArrayList<>();
        private boolean dispatched;
        private String lastEventId;
        private LocalDateTime dispatchedAt;
        private LocalDateTime updatedAt;

        public VisitClaimsStage(Long visitId, Long patientId) {
            this.visitId = visitId;
            this.patientId = patientId;
            this.updatedAt = LocalDateTime.now();
        }

        public synchronized void setPatientId(Long patientId) {
            this.patientId = patientId;
            this.updatedAt = LocalDateTime.now();
        }

        public synchronized void setDispatched(boolean dispatched) {
            this.dispatched = dispatched;
            this.updatedAt = LocalDateTime.now();
        }

        public synchronized void setLastEventId(String lastEventId) {
            this.lastEventId = lastEventId;
            this.updatedAt = LocalDateTime.now();
        }

        public synchronized void setDispatchedAt(LocalDateTime dispatchedAt) {
            this.dispatchedAt = dispatchedAt;
            this.updatedAt = LocalDateTime.now();
        }

        public synchronized void upsert(ClaimsItemRequest item) {
            String key = uniqueKey(item);
            int idx = -1;
            for (int i = 0; i < items.size(); i++) {
                if (uniqueKey(items.get(i)).equals(key)) {
                    idx = i;
                    break;
                }
            }
            if (idx >= 0) {
                items.set(idx, item);
            } else {
                items.add(item);
            }
            items.sort(Comparator.comparing(VisitClaimsStage::uniqueKey));
            updatedAt = LocalDateTime.now();
        }

        public synchronized VisitClaimsStage copy() {
            VisitClaimsStage copy = new VisitClaimsStage(this.visitId, this.patientId);
            copy.items.clear();
            copy.items.addAll(this.items);
            copy.dispatched = this.dispatched;
            copy.lastEventId = this.lastEventId;
            copy.dispatchedAt = this.dispatchedAt;
            copy.updatedAt = this.updatedAt;
            return copy;
        }

        private static String uniqueKey(ClaimsItemRequest item) {
            return (item.getOrderType() == null ? "" : item.getOrderType())
                    + "|" + (item.getSourceType() == null ? "" : item.getSourceType())
                    + "|" + (item.getSourceId() == null ? "0" : item.getSourceId().toString());
        }
    }
}
