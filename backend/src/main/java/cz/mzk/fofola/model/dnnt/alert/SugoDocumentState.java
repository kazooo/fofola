package cz.mzk.fofola.model.dnnt.alert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class SugoDocumentState {
    private String uuid;
    private String path;
    private String title;
    private String root;
    private List<String> currentLabels;
    private List<String> nextLabels;
}
