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
package de.muenchen.oss.appswitcher.vaadin;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.vaadin.componentfactory.Popup;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class Appswitcher extends Div {

    private static final long serialVersionUID = 1L;

    private <T extends Component & ClickNotifier<?>> Appswitcher(String uriWithTags, T component, String id,
            String iframeId, String width, String height) {
        IFrame iframe = new IFrame(uriWithTags);
        iframe.setId(iframeId);
        iframe.getElement().setAttribute("frameborder", "0");
        iframe.setWidth(width);
        iframe.setHeight(height);

        Popup popup = new Popup();
        popup.setFor(id);
        popup.add(iframe);
        this.add(component, popup);
    }

    public static <T extends Component & ClickNotifier<?>> Builder<T> builder(String baseUrl) {
        return new Builder<>(baseUrl);
    }

    public static class Builder<T extends Component & ClickNotifier<?>> {
        private String baseUrl;
        private String[] tags = new String[] { "global" };
        private T comp;
        private Icon icon = VaadinIcon.GRID_SMALL.create();
        private String iframeId = "appswitcher-iframe";
        private String componentId = "appswitcher-component";
        private String width = "315px";
        private String heigth = "300px";

        public Builder(String baseUrl) {
            if (baseUrl == null || baseUrl.isBlank()) {
                throw new IllegalArgumentException("A base url must be provided");
            }
            this.baseUrl = baseUrl;
        }

        /**
         * @param baseUrl URL to appswitcher-server
         * @return the {@link Builder}
         */
        public Builder<T> baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * @param tags requested tags (default: ['global'])
         * @return the {@link Builder}
         */
        public Builder<T> tags(String... tags) {
            this.tags = tags;
            return this;
        }

        /**
         *
         * @param component component which will trigger the popup containing the appswitcher-server
         *            iframe. Default: {@link Button} with
         *            {@link ButtonVariant#LUMO_TERTIARY} and Icon {@link VaadinIcon#GRID_SMALL}
         * @return the {@link Builder}
         */
        public Builder<T> customComponent(T component) {
            this.comp = component;
            return this;
        }

        /**
         * @param iframeId ID of iframe element
         * @return the {@link Builder}
         */
        public Builder<T> iframeId(String iframeId) {
            this.iframeId = iframeId;
            return this;
        }

        /**
         *
         * @param componentId ID of component
         * @return the {@link Builder}
         */
        public Builder<T> componentId(String componentId) {
            this.componentId = componentId;
            return this;
        }

        /**
         * Sets the size of the iframe. Should be strings that are understood by browsers (e.g.
         * '100%', '1.5em', '200px').
         *
         * @param width (default: "315px")
         * @param height (default: "300px")
         * @return the {@link Builder}
         */
        public Builder<T> dimensions(String width, String height) {
            this.width = width;
            this.heigth = height;
            return this;
        }

        /**
         * @return a {@link Appswitcher} component
         */
        @SuppressWarnings("unchecked")
        public Appswitcher build() {
            if (this.comp == null) {
                Button b = new Button(this.icon);
                b.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                b.setId(this.componentId);
                this.comp = (T) b;
            }
            return new Appswitcher(getUrlWithTags(this.baseUrl, this.tags), this.comp, this.componentId,
                    this.iframeId, this.width, this.heigth);
        }

        private String getUrlWithTags(String baseUrl, String[] tags) {
            if (tags == null || tags.length == 0) {
                return baseUrl;
            } else {
                return baseUrl + "?tags=" + Arrays.asList(tags).stream().collect(Collectors.joining(","));
            }
        }
    }

}
