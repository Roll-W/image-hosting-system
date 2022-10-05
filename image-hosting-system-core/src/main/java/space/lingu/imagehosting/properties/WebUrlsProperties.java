/*
 * Copyright (C) 2022 Lingu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.imagehosting.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import java.util.Objects;

/**
 * @author RollW
 */
@ConfigurationProperties(value = "web-url")
public final class WebUrlsProperties {
    private final String backendUrl;
    private final String frontendUrl;
    private final List<String> allowedOrigins;

    @ConstructorBinding
    public WebUrlsProperties(String backendUrl,
                             String frontendUrl,
                             List<String> allowedOrigins) {
        this.backendUrl = backendUrl;
        this.frontendUrl = frontendUrl;
        this.allowedOrigins = allowedOrigins;
    }

    public String getBackendUrl() {
        return backendUrl;
    }

    public String getFrontendUrl() {
        return frontendUrl;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (WebUrlsProperties) obj;
        return Objects.equals(this.backendUrl, that.backendUrl) &&
                Objects.equals(this.frontendUrl, that.frontendUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backendUrl, frontendUrl);
    }

    @Override
    public String toString() {
        return "WebUrls[" +
                "backendUrl=" + backendUrl + ", " +
                "frontendUrl=" + frontendUrl + ']';
    }

}
