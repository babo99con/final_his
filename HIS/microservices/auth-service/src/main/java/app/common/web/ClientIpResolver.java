package app.common.web;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class ClientIpResolver {

    private static final String UNKNOWN = "unknown";
    private static final String[] IP_HEADER_NAMES = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_CLIENT_IP"
    };

    public String resolveClientIp(HttpServletRequest request) {
        String headerIp = resolveFromHeaders(request);
        if (StringUtils.hasText(headerIp)) {
            return headerIp;
        }

        return normalizeIp(request.getRemoteAddr());
    }

    public String resolveProxyIp(HttpServletRequest request) {
        return normalizeIp(request.getRemoteAddr());
    }

    private String resolveFromHeaders(HttpServletRequest request) {
        for (String headerName : IP_HEADER_NAMES) {
            String headerValue = request.getHeader(headerName);
            String extractedIp = extractFirstIp(headerValue);
            if (StringUtils.hasText(extractedIp)) {
                return extractedIp;
            }
        }

        return extractForwardedIp(request.getHeader("Forwarded"));
    }

    private String extractForwardedIp(String forwardedHeader) {
        if (!StringUtils.hasText(forwardedHeader)) {
            return null;
        }

        String[] entries = forwardedHeader.split(",");
        for (String entry : entries) {
            String[] directives = entry.split(";");
            for (String directive : directives) {
                String trimmed = directive.trim();
                if (!trimmed.regionMatches(true, 0, "for=", 0, 4)) {
                    continue;
                }

                String value = trimmed.substring(4).trim();
                if (value.startsWith("\"") && value.endsWith("\"") && value.length() > 1) {
                    value = value.substring(1, value.length() - 1);
                }

                if (value.startsWith("[") && value.endsWith("]") && value.length() > 1) {
                    value = value.substring(1, value.length() - 1);
                }

                if (value.startsWith("_")) {
                    continue;
                }

                int portDelimiterIndex = value.lastIndexOf(':');
                if (portDelimiterIndex > -1 && value.indexOf(':') == portDelimiterIndex) {
                    value = value.substring(0, portDelimiterIndex);
                }

                String normalized = normalizeIp(value);
                if (StringUtils.hasText(normalized)) {
                    return normalized;
                }
            }
        }

        return null;
    }

    private String extractFirstIp(String rawValue) {
        if (!StringUtils.hasText(rawValue)) {
            return null;
        }

        String[] values = rawValue.split(",");
        for (String value : values) {
            String normalized = normalizeIp(value);
            if (StringUtils.hasText(normalized)) {
                return normalized;
            }
        }

        return null;
    }

    private String normalizeIp(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        String normalized = value.trim();
        if (!StringUtils.hasText(normalized) || UNKNOWN.equalsIgnoreCase(normalized)) {
            return null;
        }

        return normalized;
    }
}
