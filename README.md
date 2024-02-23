# appswitcher-vaadin

Provides a Vaadin Flow (24+) component (`Appswitcher`) to embed an `<iframe>` with the content of [appswitcher-server](https://github.com/it-at-m/appswitcher-server).

![](docs/component.png)

## Usage

### Available Versions

See GitHub [Releases](https://github.com/it-at-m/appswitcher-vaadin/releases).

### Dependency (Maven)

```xml
<dependency>
  <groupId>de.muenchen.oss.appswitcher</groupId>
  <artifactId>appswitcher-vaadin</artifactId>
  <version><!-- see Releases --></version>
</dependency>
```

### Use

Example:

```java
import de.muenchen.oss.appswitcher.vaadin.Appswitcher;

public class MyVaadinView extends Div {

    public MyVaadinView() {
        add(Appswitcher.builder("https://my-appswitcher-server.example.com")
        		// request tags according to your needs
            .tags("global", "finance")
            .build());
    }
}
```

## Development

### Deployment

Starting the test/demo server:

```bash
mvn jetty:run
```

This deploys demo at <http://localhost:8080>

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please open an issue with the tag "enhancement", fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Open an issue with the tag "enhancement"
2. Fork the Project
3. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
4. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
5. Push to the Branch (`git push origin feature/AmazingFeature`)
6. Open a Pull Request

We use [itm-java-codeformat](https://github.com/it-at-m/itm-java-codeformat), so please make sure to apply the correct code format for your contributions.


## License

Distributed under the MIT License. See [LICENSE](LICENSE) file for more information.

## Contact

it@M - opensource@muenchen.de
