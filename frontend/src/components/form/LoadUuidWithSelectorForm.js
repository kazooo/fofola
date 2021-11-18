import {Box, Grid, TextField} from "@material-ui/core";
import {Selector} from "./Selector";

export const LoadUuidWithSelectorForm = ({
                                             onLoad,
                                             selectOptions,
                                             selectLabel,
                                             selectedOption,
                                             onSelectOptionChange
}) => {
    return <Box>
        <Grid container
              direction="row"
              alignItems={"center"}
              justifyContent={"center"}
              spacing={3}
        >
            <Grid item>
                <TextField
                    label="Zadejte UUID"
                    variant="outlined"
                    placeholder="uuid:..."
                    onChange={e => onLoad(e.target.value)}
                    size="small"
                    inputProps={{ maxLength: 40 }}
                />
            </Grid>
            <Grid item>
                <Selector
                    selectLabel={selectLabel}
                    selectOptions={selectOptions}
                    selectedOption={selectedOption}
                    onSelectOptionChange={onSelectOptionChange}
                />
            </Grid>
        </Grid>
    </Box>;
};
