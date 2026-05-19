package com.app.medical_support.common.sequence;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Types;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OracleSequenceIdService implements SequenceIdService {

    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall spNextNoCall;

    @PostConstruct
    public void init() {
        this.spNextNoCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("HOSPITAL")
                .withProcedureName("SP_NEXT_NO")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_SEQ_TYPE", Types.VARCHAR),
                        new SqlOutParameter("P_NO", Types.VARCHAR)
                );
    }

    @Override
    public String nextId(SequenceIdType sequenceIdType) {
        Map<String, Object> result = spNextNoCall.execute(Map.of("P_SEQ_TYPE", sequenceIdType.name()));
        Object rawValue = result.get("P_NO");
        if (rawValue == null) {
            rawValue = result.get("p_no");
        }
        String nextId = rawValue != null ? rawValue.toString() : null;

        if (nextId == null || nextId.trim().isEmpty()) {
            throw new IllegalStateException("SP_NEXT_NO did not return a valid ID. seqType=" + sequenceIdType.name());
        }

        return nextId;
    }
}
