import axios from "axios";

declare global {
  interface Window {
    __hisFetchCredentialsConfigured?: boolean;
    __hisAxiosCredentialsConfigured?: boolean;
  }
}

// 브라우저에서 다른 포트의 microservice를 호출해도 JSESSIONID 쿠키가 함께 가도록 기본값을 맞춥니다.
axios.defaults.withCredentials = true;

if (typeof window !== "undefined" && !window.__hisAxiosCredentialsConfigured) {
  const AxiosClass = axios.Axios;
  const axiosPrototype = AxiosClass.prototype as unknown as {
    request: (...args: unknown[]) => Promise<unknown>;
  };
  const originalAxiosRequest = axiosPrototype.request;

  axiosPrototype.request = function requestWithSessionCookie(
    configOrUrl: unknown,
    config?: unknown,
  ) {
    if (typeof configOrUrl === "string") {
      const nextConfig = {
        ...((config as Record<string, unknown> | undefined) ?? {}),
      };

      if (nextConfig.withCredentials === undefined) {
        nextConfig.withCredentials = true;
      }

      return originalAxiosRequest.call(this, configOrUrl, nextConfig);
    }

    const nextConfig = {
      ...((configOrUrl as Record<string, unknown> | undefined) ?? {}),
    };

    if (nextConfig.withCredentials === undefined) {
      nextConfig.withCredentials = true;
    }

    return originalAxiosRequest.call(this, nextConfig);
  };

  window.__hisAxiosCredentialsConfigured = true;
}

if (typeof window !== "undefined" && !window.__hisFetchCredentialsConfigured) {
  const originalFetch = window.fetch.bind(window);

  window.fetch = (input: RequestInfo | URL, init?: RequestInit) => {
    const nextInit: RequestInit = {
      ...init,
    };

    if (nextInit.credentials === undefined) {
      nextInit.credentials = "include";
    }

    return originalFetch(input, nextInit);
  };

  window.__hisFetchCredentialsConfigured = true;
}
