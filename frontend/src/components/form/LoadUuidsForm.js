import {LoadStringForm} from "./LoadStringForm";
import {LoadStringsFromFileForm} from "./LoadStringsFromFileForm";
import {Box, Grid} from "@material-ui/core";

export const LoadUuidsForm = ({addUuids, className = null}) => {

    const loadOneUuid = (uuid) => {
        loadUuids([uuid]);
    }

    const loadUuids = (uuids) => {
        addUuids(uuids);
    }

    return <Box>
        <Grid container
              direction="row"
              alignItems={"center"}
              justifyContent={"center"}
              className={className}
              spacing={3}
        >
            <Grid item>
                <LoadStringForm
                    size="small"
                    label="UUID"
                    button="Načíst uuid"
                    placeholder="uuid:..."
                    submitFunc={loadOneUuid}
                />
            </Grid>
            <Grid item>
                <LoadStringsFromFileForm
                    label="Vyberte soubor s UUID"
                    submitFunc={loadUuids}
                />
            </Grid>
        </Grid>
    </Box>;
};

