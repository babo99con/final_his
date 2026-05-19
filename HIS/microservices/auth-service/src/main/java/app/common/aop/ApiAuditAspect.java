package app.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class ApiAuditAspect {

    // Audit log for write operations (POST/PUT/DELETE).
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object auditWrite(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = resolveRequest();
        if (request == null) {
            return joinPoint.proceed();
        }

        String method = request.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            return joinPoint.proceed();
        }

        String uri = request.getRequestURI();
        String agent = request.getHeader("User-Agent");
        String maskedParams = PiiMaskingUtil.toMaskedQueryString(request.getParameterMap());

        try {
            Object result = joinPoint.proceed();
            log.info("[AUDIT] {} {} ua={} params={}", method, uri, agent, maskedParams);
            return result;
        } catch (ResponseStatusException ex) {
            log.warn("[AUDIT] {} {} ua={} params={} status={}", method, uri, agent, maskedParams, ex.getStatus());
            throw ex;
        } catch (Throwable ex) {
            log.warn("[AUDIT] {} {} ua={} params={} status={}", method, uri, agent, maskedParams, HttpStatus.INTERNAL_SERVER_ERROR);
            throw ex;
        }
    }

    private HttpServletRequest resolveRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (!(attrs instanceof ServletRequestAttributes)) return null;
        return ((ServletRequestAttributes) attrs).getRequest();
    }
}

