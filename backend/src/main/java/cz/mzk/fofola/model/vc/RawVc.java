package cz.mzk.fofola.model.vc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawVc {
    public String pid;
    public Map<String, String> names = new HashMap<>();
    public Map<String, String> descriptions = new HashMap<>();
    public Map<String, List<String>> keywords = new HashMap<>();
    public Map<String, String> contents = new HashMap<>();
    public LocalDateTime created;
    public LocalDateTime modified;
    public Boolean standalone;
    public String author;
}
