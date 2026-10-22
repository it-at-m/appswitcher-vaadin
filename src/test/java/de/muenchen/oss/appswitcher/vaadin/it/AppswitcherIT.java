/*
 * The MIT License
 * Copyright © 2023 Landeshauptstadt München | it@M
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.muenchen.oss.appswitcher.vaadin.it;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import in.virit.mopo.Mopo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AppswitcherIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppswitcherIT.class);

    @Value("${local.server.port}")
    private int port;

    @Container
    public GenericContainer<?> container = new GenericContainer(DockerImageName.parse("ghcr.io/it-at-m/appswitcher-server:1.3.0"))
            .withExposedPorts(8080)
            .withEnv("SPRING_PROFILES_ACTIVE", "demo")
            .withLogConsumer(new Slf4jLogConsumer(LOGGER))
            .waitingFor(Wait.forHttp("/actuator/health/readiness").forStatusCode(200));

    static Playwright playwright = Playwright.create();

    private Browser browser;
    private Page page;
    private Mopo mopo;

    @BeforeEach
    public void setup() {
        browser = playwright.chromium().launch();

        page = browser.newPage();
        page.setDefaultTimeout(10_000); // die faster if needed
        mopo = new Mopo(page);
    }

    @AfterEach
    public void closePlaywright() {
        page.close();
        browser.close();
    }

    @Test
    public void smokeTestAllTestUIs() {
        String rootUrl = "http://localhost:" + port + "/";
        mopo.getViewsReportedByDevMode(browser, rootUrl).forEach(viewName -> {
            String url = rootUrl + viewName;
            page.navigate(url);
            mopo.failOnClientSideErrors();
        });

    }

    @Test
    public void testAppswitcher() {
        String rootUrl = "http://localhost:" + port + "/testui";
        page.navigate(rootUrl);

        Locator appswitcherUrlText = page.locator("[data-testid='appswitcher-url'] > input ");
        appswitcherUrlText.waitFor();
        appswitcherUrlText.fill("http://localhost:" + container.getMappedPort(8080) + "?tags=global");
        page.locator("id=appswitcher-activator").click();

        page.locator("#appswitcher-iframe").contentFrame().getByText("Example App").waitFor();
    }
}
