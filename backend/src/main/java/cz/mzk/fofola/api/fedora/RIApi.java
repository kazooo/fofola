package cz.mzk.fofola.api.fedora;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public class RIApi {

    private final FedoraApi fedoraApi;

    public List<RITriple> query(final RIQuery query) throws IOException {
        final List<RITriple> triples = new ArrayList<>();
        final Optional<String> response = fedoraApi.queryRi(query.toString());
        response.ifPresent(body -> {
            triples.addAll(
                    Stream.of(body.split("\n"))
                            .map(line -> {
                                final String[] parts = line.split(" ");
                                return new RITriple(parts[0], parts[1], parts[2]);
                            }).toList()
            );
        });
        return triples;
    }
}
